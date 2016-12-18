package com.xcrm.order;

import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeFinder;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.util.Constant;

public class OrderController extends AbstractController {

	public void list() {
		String sql = "select oi.id id,p.name name,o.orderno orderno,bi.price price,bi.num num,GROUP_CONCAT(pic.fielname) filename,oi.date date";
		String sqlExcept = " from orderitem oi left join bookitem bi on oi.bookitem=bi.id left join `order` o on o.id=oi.order left join product p on bi.product=p.id left join productpic pic on p.id=pic.productid where bi.user=? group by oi.id,p.name,o.orderno,bi.price,bi.num,oi.date order by oi.id desc";
		int pagenumber = Integer.parseInt(this.getPara("pageNumber"));
		int pagesize = Integer.parseInt(this.getPara("pageSize"));
		Page<Record> page = Db.paginate(pagenumber, pagesize, sql, sqlExcept, getCurrentUserId());
		Pager pager = new Pager(page.getTotalRow(), page.getList());
		this.renderJson(pager);

	}

	@Override
	public String getModalName() {
		return "order";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "我的订单";
	}

	@Override
	public String getIndexHtml() {
		return "order.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_ORDER;
	}
}
