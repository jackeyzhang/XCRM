package com.xcrm.product;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeFinder;
import com.xcrm.common.AttributeID;
import com.xcrm.common.Pager;
import com.xcrm.common.barcode.QRCodeUtil;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.MD5Util;

@Before(ProductInterceptor.class)
public class ProductController extends AbstractController {
	public void index() {
		super.index();
<<<<<<< HEAD
		this.setAttr("imgMaxCount", PropKit.get("product.img.maxsize"));
	}

	public void detail() {
		setAttribute();
		Product product = Product.dao.findFirst("select * from product where barcode =?", this.getPara("barcode"));
		List<Productpic> pics = Productpic.dao.find("select * from productpic where productid=?", product.getId());
		List<Attributevalue> attributevalues = Attributevalue.dao.find("select * from attributevalue where objectid=?",
				product.getId());
		setAttr("page_header", "产品详细信息");
		setAttr("product_color", AttributeID.getValue(attributevalues, AttributeID.PRD_COLOR));
		setAttr("product_size", AttributeID.getValue(attributevalues, AttributeID.PRD_SIZE));
		setAttr("product_depatment", AttributeID.getValue(attributevalues, AttributeID.PRD_DEPARTMENT));
		setAttr("product_material", AttributeID.getValue(attributevalues, AttributeID.PRD_MATERIAL));
		setAttr("prdimages", pics);
		setAttr("product", product);
		render("productdetail.html");
=======
	}
	
	public void list() {
	    Pager pager = new Pager();
	    if(this.getPara("pageNumber") != null){
	      int pagenumber = Integer.parseInt(this.getPara("pageNumber"));
	      int pagesize = Integer.parseInt(this.getPara("pageSize"));
	      int level1 = this.getParaToInt( "level1", 0 );
	      int level2 = this.getParaToInt( "level2", 0 );
	      Page<Record> page = Db.paginate(pagenumber, pagesize, "select * ", " from product " + ( level1 > 0? "where level1category= "+level1 : "") + ( level2 > 0? " and level2category= "+level2 : ""));
	      String searchword = this.getPara( "searchword" );
	      Iterator<Record> iter = page.getList().iterator();
	      for(;iter.hasNext();){
	        Record record = iter.next();
	        if(record.getStr( "name" ) != null && searchword != null && !record.getStr( "name" ).contains( searchword )){
	          iter.remove();
	          continue;
	        }
	      }
	      pager = new Pager(page.getTotalRow(), page.getList());
	    }else{
	      List<Record> records = Db.find( "select * from " + getModalName() );
	      pager = new Pager(records.size(), records);
	    }
	    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
	    for(Record record : pager.getRows()){
	      for(Attribute attribute : attributes){
	        Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),record.getInt( "id" ), getCategory() );
	        if(av == null) continue;
	        record.set( "attribute-" + av.getAttributeid(), av.getValue() );
	      }
	    }
	    this.renderJson( pager );
	  
	  }
	
	public void detail(){
	  setAttribute();
	  Product product = Product.dao.findFirst( "select * from product where barcode =?", this.getPara("barcode" ) );
	  List<Productpic> pics = Productpic.dao.find( "select * from productpic where productid=?", product.getId() );
	  List<Attributevalue> attributevalues = Attributevalue.dao.find( "select * from attributevalue where objectid=?", product.getId() );
	  setAttr( "page_header", "产品详细信息" );
	  setAttr( "product_color", AttributeID.getValue( attributevalues, AttributeID.PRD_COLOR ) );
	  setAttr( "product_size", AttributeID.getValue( attributevalues, AttributeID.PRD_SIZE ) );
	  setAttr( "product_depatment", AttributeID.getValue( attributevalues, AttributeID.PRD_DEPARTMENT ) );
	  setAttr( "product_material", AttributeID.getValue( attributevalues, AttributeID.PRD_MATERIAL ) );
	  setAttr( "prdimages", pics );
	  setAttr( "product", product );
	  render( "productdetail.html" );
>>>>>>> de65cc6e17c0d53992bb7f8a2fc44469f76636e2
	}

	public void save() {
		Product product = this.getModel(Product.class, "", true).set("barcode", MD5Util.getSystemKey())
				.set("createuser", this.getCurrentUserId()).set("createdate", new Date());
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
		File srcDir = getFileByStr(getDir(PropKit.get("product.img.path")) + prdid + Constant.SLASH);
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
			String destDirPath = getDir(PropKit.get("product.img.path")) + prdid + Constant.SLASH;
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
		Product product = this.getModel(Product.class, "", true).set("edituser", this.getCurrentUserId())
				.set("editdate", new Date());
		if (product.getStr("barcode").isEmpty()) {
			product.set("barcode", MD5Util.getSystemKey());
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
