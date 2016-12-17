package com.xcrm.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Orderitem;
import com.xcrm.common.util.Constant;

public class OrderController extends AbstractController {
	
	public void index() {
		super.index();
		List<Record> list = Db.find("select bi.id, bi.num num,bi.price price,bi.product pid,p.name name,GROUP_CONCAT(pic.fielname) filename from bookitem bi left join product p on bi.product=p.id left join productpic pic on pic.productid=p.id where  bi.user=? and bi.status=0 group by bi.id, bi.num,bi.price,bi.product,p.name", getCurrentUserId());
		setAttr("list", list);
		setAttr("prdimg_path", getPrdImgBaseUrl());
	}
	
	public void save() {
		String ids = this.getPara("ids");
		String[] idarray = ids.split(",");
		Order order = new Order();
		Date date = new Date();
		order.setDate(date);
		order.setOrderno(date.getTime());
		order.save();
		List<Orderitem> orderitems = new ArrayList<Orderitem>();
		for (String id : idarray) {
			Orderitem orderitem = new Orderitem();
			orderitem.setBookitem(Integer.valueOf(id));
			orderitem.setDate(date);
			orderitem.setOrder(order.getId());
		}
		Db.batchSave(orderitems, orderitems.size());
		this.forwardAction(getIndexHtml());
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
