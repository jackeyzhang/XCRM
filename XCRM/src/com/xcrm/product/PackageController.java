package com.xcrm.product;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;

@Before(ProductInterceptor.class)
public class PackageController extends AbstractController {
  
	public void index() {
		super.index();
	}


	@Override
	public String getModalName() {
		return "package";
	}

	@Override
	public String getPageHeader() {
		return "套餐设置";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "";
	}

	@Override
	public String getIndexHtml() {
		return "package.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_PRODUCT;
	}
}
