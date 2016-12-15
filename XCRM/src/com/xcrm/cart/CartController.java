package com.xcrm.cart;

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

public class CartController extends AbstractController {

	public void index() {
		super.index();
		Product product = Product.dao.findById(this.getPara("pid"));
		List<Productpic> productpics = Productpic.dao.find("select * from productpic where productid=?",
				this.getPara("pid"));
		this.setAttr("prd", product);
		this.setAttr("prdpics", productpics.size()==0?null:productpics);
		setAttr("prdimg_path", getPrdImgBaseUrl());
		setAttr("attrs", getAttrs());
	}

	private List<Tuple<AttributeID, List<String>>> getAttrs() {
		List<Attributevalue> list = Attributevalue.dao
				.find("select * from attributevalue where category=1 and objectid=?", this.getPara("pid"));
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
		return "cart";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "返回产品列表";
	}

	@Override
	public String getIndexHtml() {
		return "cart.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_CART;
	}
}