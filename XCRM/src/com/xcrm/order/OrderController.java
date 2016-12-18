package com.xcrm.order;

import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;

public class OrderController extends AbstractController {
	
	
	public void list(){/*
		String sql = "select oi.id id,p.name name,o.orderno orderno,bi.price price,bi.num num,GROUP_CONCAT(pic.fielname) filename from orderitem oi left join bookitem bi on oi.bookitem=bi.id left join `order` o on o.id=oi.order left join product p on bi.product=p.id left join productpic pic on p.id=pic.productid where bi.user=? group by oi.id,p.name,o.orderno,bi.price,bi.num";
		List<Record> records = Db.find(sql,getCurrentUserId());
		Pager pager = new Pager(records.size(), records);
		this.renderJson(records);

		Pager pager = new Pager();
		if (this.getPara("pageNumber") != null) {
			int pagenumber = Integer.parseInt(this.getPara("pageNumber"));
			int pagesize = Integer.parseInt(this.getPara("pageSize"));
			Page<Record> page = Db.paginate(pagenumber, pagesize, "select * ", "from " + getModalName() + "");
			pager = new Pager(page.getTotalRow(), page.getList());
			List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList(getCategory());
			String searchword = this.getPara("searchword");
			Iterator<Record> iter = pager.getRows().iterator();
			this.renderJson(pager);
		} else {
			List<Record> records = Db.find("select * from " + getModalName());
			pager = new Pager(records.size(), records);
			List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList(getCategory());
			for (Record record : pager.getRows()) {
				for (Attribute attribute : attributes) {
					Attributevalue av = Attributevalue.dao.findFirst(
							"select * from attributevalue where attributeid=? and objectid=? and category=?",
							attribute.getAttributeid(), record.getInt("id"), getCategory());
					if (av == null)
						continue;
					record.set("attribute-" + getCategory() + av.getAttributeid(), av.getValue());
				}
				preRenderJsonForList(record);
			}
			this.renderJson(records);
		}
	
	*/}

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
