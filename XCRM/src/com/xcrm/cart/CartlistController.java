package com.xcrm.cart;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.util.Constant;

public class CartlistController extends AbstractController {

	public void index() {
		super.index();
		List<Record> list = Db.find("select bi.id, bi.num num,bi.price price,bi.product pid,p.name name,GROUP_CONCAT(pic.fielname) filename from bookitem bi left join product p on bi.product=p.id left join productpic pic on pic.productid=p.id where  bi.user=? and bi.status=0 group by bi.id, bi.num,bi.price,bi.product,p.name", getCurrentUserId());
		setAttr("list", list);
		setAttr("prdimg_path", getPrdImgBaseUrl());
	}
	
	public void remove() {
		Bookitem.dao.deleteById(this.getPara("id"));
	}

	@Override
	public String getModalName() {
		return "cartlist";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "Go Shopping";
	}

	@Override
	public String getIndexHtml() {
		return "cartlist.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_CARTLIST;
	}
}
