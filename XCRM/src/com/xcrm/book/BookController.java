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
		return "订单结算";
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
