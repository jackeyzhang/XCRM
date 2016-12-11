package com.xcrm.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productcategory;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.util.Constant;

public class SearchController extends AbstractController {
	public static final int PAGE_SIZE = 24;

	public void index() {
		super.index();
		this.setAttr("level1s", Productcategory.dao.find("select * from productcategory where level=1"));
		this.setAttr("page", search(1, PAGE_SIZE));
	}

	private Page<Record> search(int pageNum, int pageSize) {
		String level1 = this.getPara("level1");
		String level2 = this.getPara("level2");
		String keyword = this.getPara("kw");
		StringBuffer sb = new StringBuffer(" from product p left join productpic pp on p.id=pp.productid where 1=1");
		List<String> params = new ArrayList<String>();
		if (!StringUtils.isEmpty(level1)) {
			sb.append(" and p.level1category=?");
			params.add(level1);
		}
		if (!StringUtils.isEmpty(level2)) {
			sb.append(" and p.level2category=?");
			params.add(level2);
		}
		if (!StringUtils.isEmpty(keyword)) {
			sb.append(" and p.name like ?");
			params.add(keyword.trim() + "%");
		}
		sb.append(" group by p.id,p.name");
		Page<Record> page = Db.paginate(pageNum, PAGE_SIZE,
				"select p.id id,p.name name,GROUP_CONCAT(pp.fielname) filename ", sb.toString(), params.toArray());
		setAttr("prdimg_path", getPrdImgBaseUrl());
		return page;
	}

	public void prdnames() {
		List<Product> products = Product.dao.find("select * from product");
		List<String> names = new ArrayList<String>();
		for (Product product : products) {
			names.add(product.getStr("name"));
		}
		renderJson(names);
	}


	public void getPage() {
		String pageNum = this.getPara("pageNum");
		this.setAttr("page", search(StringUtils.isEmpty(pageNum) ? 1 : Integer.valueOf(pageNum), PAGE_SIZE));
		this.render("productlist.html");
	}

	public void level2() {
		List<Productcategory> level2s = Productcategory.dao
				.find("select * from productcategory where level=2 and pid=?", this.getPara("pid"));
		renderJson(level2s);
	}


	@Override
	public String getModalName() {
		return "search";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "产品搜索";
	}

	@Override
	public String getIndexHtml() {
		return "search.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_SEARCH;
	}
}
