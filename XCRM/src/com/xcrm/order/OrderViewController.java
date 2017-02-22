package com.xcrm.order;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.StrUtil;

public class OrderViewController extends AbstractController {
  
    public void index(){
      String orderno = this.getPara( "orderno" );
      this.setSessionAttr( "orderno", orderno );
      this.setAttr( "orderno", orderno );
      
      Record record = Db.findFirst( "select o.comments,o.paymentcomments,o.price from xcrm.order o where o.orderno=" + orderno );
      Float dealPrice =  record.getFloat( "price" );
      Record paymentrecord = Db.findFirst( "select sum(paid) paid from payment where orderno=" + orderno + " group by orderno" );
      Double paid = paymentrecord.getDouble( "paid" );
      this.setAttr( "ordercomments", record.getStr( "comments" ) );
      this.setAttr( "paymentcomments", record.getStr( "paymentcomments" ) );
      this.setAttr( "dealprice", StrUtil.formatPrice( dealPrice ) );
      this.setAttr( "paid", StrUtil.formatPrice( paid ) );
      this.setAttr( "due", StrUtil.formatPrice( dealPrice - paid ) );
      super.index();
    }
    
    public void loadingPayment(){
      String orderno = this.getPara( "orderno" );
      String sql = "select CONCAT(cust.name, '-' ,cust.company) customer,pay.comments,pay.paid,pay.paymenttime,pay.paymenttype";
      String sqlExcept = " from payment pay "
          + "left join customer cust on cust.id=pay.customerid "
          + "where pay.orderno = "+ orderno +" order by pay.paymenttime desc";
      Page<Record> page = Db.paginate(1, 100, sql, sqlExcept);
      Pager pager = new Pager(page.getTotalRow(), page.getList());
 
      this.renderJson(pager);
    }
  
	public void list() {
	  String orderno = this.getSessionAttr( "orderno" );
      //price是原价  deal price是成交价  
      String sql = "select o.orderno,p.name name,bi.price price,o.price dealprice,o.paid paid,bi.num num,oi.date date,contract.name contractname,contract.id contractid";
      String sqlExcept = " from orderitem oi "
          + "left join bookitem bi on oi.bookitem=bi.id "
          + "left join `order` o on o.id=oi.order "
          + "left join product p on bi.product=p.id "
          + "left join contract contract on bi.contract=contract.id "
          + "where o.orderno = "+ orderno +" order by o.orderno desc";
      Page<Record> page = Db.paginate(1, 100, sql, sqlExcept);
      Pager pager = new Pager(page.getTotalRow(), page.getList());
		this.renderJson(pager);
	}

	@Override
	public String getModalName() {
		return "orderview";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "订单详情如下";
	}

	@Override
	public String getIndexHtml() {
		return "orderview.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_ORDERVIEW;
	}
}
