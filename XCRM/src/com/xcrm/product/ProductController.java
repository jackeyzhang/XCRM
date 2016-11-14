package com.xcrm.product;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.AbstractController;
import com.xcrm.common.barcode.QRCodeUtil;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.MD5Util;
import com.xcrm.common.util.PropertiesUtil;

@Before(ProductInterceptor.class)
public class ProductController extends AbstractController {
	public void index() {
		super.index();
		this.setAttr("imgMaxCount", PropertiesUtil.getProductImgMaxSize());
	}

	public void save() {
		Product product = this.getModel(Product.class, "", true).set( "barcode", MD5Util.getSystemKey()).set( "createuser", this.getCurrentUserId() ).set( "createdate", new Date() );
		product.save();
		saveImgs(product.getId());
		QRCodeUtil.generator(product.getId(), this.getRequest().getServletContext().getRealPath("/"));
		forwardIndex(product);
	}

	public void loadimgs() {
		String destDirPath = getTempPath(getRequest());
		File destDir = getFileByStr(destDirPath);
		if (destDir.exists()) {
			try {
				FileUtils.deleteDirectory(destDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		destDir.mkdirs();
		String prdid = this.getPara("prdid");
		File srcDir = getFileByStr(getDir(PropertiesUtil.getProductImgPath()) + prdid + Constant.SLASH);
		String imgs = "";
		if (srcDir.exists()) {
			for (File record : srcDir.listFiles()) {
				try {
					FileUtils.copyFile(record, getFileByStr(destDirPath + record.getName()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				imgs += record.getName() + ",";

			}
		}
		this.renderJson("{\"imgs\":\"" + imgs + "\"}");
	}

	public void saveImgs(int prdid) {
		String imgs = this.getPara("imgs");
		if (!StringUtils.isEmpty(imgs)) {
			String[] imgArray = imgs.split(Constant.COMMA);
			Db.update("delete from productpic where productid=" + prdid);
			String srcDirPath = getTempPath(getRequest());
			String destDirPath = getDir(PropertiesUtil.getProductImgPath()) + prdid + Constant.SLASH;
			File destDir = getFileByStr(destDirPath);
			if (destDir.exists()) {
				try {
					FileUtils.deleteDirectory(destDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			destDir.mkdir();
			for (String img : imgArray) {
				if (StringUtils.isEmpty(img))
					continue;
				Productpic pic = new Productpic();
				pic.setProductid(prdid);
				pic.setFielname(img);
				pic.save();
				try {
					FileUtils.moveFile(getFileByStr(srcDirPath + img), getFileByStr(destDirPath + img));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				FileUtils.deleteDirectory(getFileByStr(srcDirPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getDir(String path) {
		if (StringUtils.isEmpty(path)) {
			throw new RuntimeException("path is empty");
		} else {
			if (!path.endsWith(Constant.SLASH)) {
				return path + Constant.SLASH;
			} else {
				return path;
			}
		}
	}

	public String getTempPath(HttpServletRequest request) {
		return request.getServletContext().getRealPath("/") + Constant.SLASH + Constant.TEMP_IMG + Constant.SLASH
				+ getCurrentUserId() + Constant.SLASH;
	}

	private File getFileByStr(String str) {
		return new File(str);
	}

	public void upload() {
		UploadFile uploadFile = this.getFile();
		if (uploadFile != null) {
			String destDir = getTempPath(getRequest());
			synchronized (Integer.valueOf(getCurrentUserId())) {
				getFileByStr(destDir).mkdirs();
			}
			File destFile = getFileByStr(destDir + uploadFile.getFileName());
			if (destFile.exists()) {
				destFile.delete();
			}
			try {
				FileUtils.moveFile(uploadFile.getFile(), destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.renderJson();
	}

	public void update() {
		Product product = this.getModel(Product.class, "", true).set( "edituser", this.getCurrentUserId() ).set( "editdate", new Date() );
		if(product.getStr( "barcode" ).isEmpty()){
		  product.set( "barcode", MD5Util.getSystemKey() );
		}
		product.update();
		saveImgs(product.getId());
		forwardIndex();
	}

	public void remove() {
		Product.dao.deleteById(this.getParaToInt(0));
		forwardIndex();
	}

	@Override
	public String getModalName() {
		return "product";
	}

	@Override
	public String getPageHeader() {
		return "创建或修改产品相关信息";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "创建产品";
	}

	@Override
	public String getIndexHtml() {
		return "product.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_PRODUCT;
	}
}
