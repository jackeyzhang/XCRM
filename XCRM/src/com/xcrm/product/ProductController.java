package com.xcrm.product;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.WebRecord;
import com.xcrm.common.util.Constant;

@Before(ProductInterceptor.class)
public class ProductController extends Controller {

	public void index() {
		setAttr("model", "product");
		setAttr("page_header", "创建或修改产品相关信息");
		setAttr("toolbar_create", "创建产品");
		render("product.html");
	}

	public void list() {
		List<Product> Products = Product.dao.find("select * from product");
		WebRecord<Product> record = new WebRecord<Product>();
		record.setRows(Products);
		record.setTotal(Products.size());
		this.renderJson(Products);
	}

	public void save() {
		this.getModel(Product.class, "").save();
		this.forwardAction("/product/index");
	}

	public void delupload() {
		System.out.println(333);
	}

	public void upload() {
		UploadFile uploadFile = this.getFile();
		String filename = null;
		if (uploadFile != null) {
			File file = uploadFile.getFile();
			File dest = new File(file.getAbsolutePath() + Constant.UNDER_LINE
					+ ((Map) this.getSession().getAttribute("currentUser")).get("id"));
			if (dest.exists()) {
				dest.delete();
			}
			file.renameTo(dest);
			file.delete();
			filename = file.getName();
		}
		if (filename == null) {
			this.renderJson();
		} else {
			this.renderJson("[\"" + filename + "\"]");
		}
	}

	public void update() {
		this.getModel(Product.class, "").update();
		this.forwardAction("/product/index");
	}

	public void remove() {
		Product.dao.deleteById(this.getParaToInt(0));
		this.forwardAction("/product/index");
	}
}
