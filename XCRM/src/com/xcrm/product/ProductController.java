package com.xcrm.product;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.PropertiesUtil;

@Before(ProductInterceptor.class)
public class ProductController extends AbstractController {

	public void index() {
		super.index();
		this.setAttr("imgMaxCount", PropertiesUtil.getProductImgMaxSize());
	}

	public void save() {
		Product product = this.getModel(Product.class, "", true);
		product.save();
		saveImgs(product.getId());
		forwardIndex(product);
	}

	public void saveImgs(int prdid) {
		String imgs = this.getPara("imgs");
		if (!StringUtils.isEmpty(imgs)) {
			String[] imgArray = imgs.split(Constant.COMMA);
			Db.update("delete from productpic where productid=" + prdid);
			String srcDirPath = getTempPath(
					getRequest().getServletContext().getRealPath("/") + Constant.SLASH + Constant.TEMP_IMG);
			String destDirPath = getDir(PropertiesUtil.getProductImgPath()) + prdid + Constant.SLASH;
			File destDir = getFileByStr(destDirPath);
			if (destDir.exists()) {
				destDir.delete();
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
				// TODO Auto-generated catch block
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

	public String getTempPath(String tempPath) {
		return tempPath + Constant.SLASH + getCurrentUserId() + Constant.SLASH;
	}

	private File getFileByStr(String str) {
		return new File(str);
	}

	public void upload() {
		UploadFile uploadFile = this.getFile();
		if (uploadFile != null) {
			String destDir = getTempPath(uploadFile.getFile().getParent());
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
		this.getModel(Product.class, "").update();
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
