package com.xcrm.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeID;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.Tuple;

public class BookController extends AbstractController {

	public void index() {
		super.index();
		Product product = Product.dao.findById(this.getPara("id"));
		List<Productpic> productpics = Productpic.dao.find("select * from productpic where productid=?",
				this.getPara("id"));
		this.setAttr("prd", product);
		this.setAttr("prdpics", productpics);
		setAttr("prdimg_path", getPrdImgBaseUrl());
		setAttr("attrs", getAttrs());
	}

	private List<Tuple<AttributeID, List<String>>> getAttrs() {
		List<Attributevalue> list = Attributevalue.dao
				.find("select * from attributevalue where category=1 and objectid=?", this.getPara("id"));
		List<Tuple<AttributeID, List<String>>> attrs = new ArrayList<Tuple<AttributeID, List<String>>>();
		for (Attributevalue attributevalue : list) {
			String attrValue = attributevalue.getValue();
			if (!StringUtils.isEmpty(attrValue)) {
				attrs.add(new Tuple<AttributeID, List<String>>(AttributeID.getById(attributevalue.getAttributeid()),
						Arrays.asList(attrValue.split(Constant.COMMA))));
			}

		}
		return attrs;
	}

	@Override
	public String getModalName() {
		return "book";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "产品预订";
	}

	@Override
	public String getIndexHtml() {
		return "book.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_BOOK;
	}
}
