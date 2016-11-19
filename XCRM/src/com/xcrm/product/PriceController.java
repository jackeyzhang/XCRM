package com.xcrm.product;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;

@Before(ProductInterceptor.class)
public class PriceController extends AbstractController {
  
	public void index() {
		super.index();
	}


	@Override
	public String getModalName() {
		return "price";
	}

	@Override
	public String getPageHeader() {
		return "价格设置";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "";
	}

	@Override
	public String getIndexHtml() {
		return "price.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_PRODUCT;
	}
}
