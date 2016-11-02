package com.xcrm.product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.User;
import com.xcrm.common.model.WebRecord;

@Before(ProductInterceptor.class)
public class ProductController extends Controller {

	public void index() {
		setAttr("model", "product");
		setAttr("page_header", "创建或修改产品相关信息");
		setAttr("toolbar_create", "创建产品");
		User user = (User) getSession().getAttribute("currentUser");
		setAttr("login_user", "欢迎你, " + user.get("username"));
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

	public void upload() {
		UploadFile uploadFile = this.getFile();
		File file = uploadFile.getFile();
		File dest = new File(uploadFile.getUploadPath() + "/product/" + uploadFile.getOriginalFileName());
		file.renameTo(dest);
		file.delete();
		this.renderJson();
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
