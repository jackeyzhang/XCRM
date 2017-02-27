package com.xcrm.order;

import java.math.BigDecimal;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.StrUtil;


public class OrderViewController extends AbstractController {

  public void index() {
    String orderno = this.getPara( "orderno" );
    this.setSessionAttr( "orderno", orderno );
    this.setAttr( "orderno", orderno );

    Record record = Db.findFirst( "select o.comments,o.paymentcomments,o.price from xcrm.order o where o.orderno=" + orderno );
    BigDecimal dealPrice = record.getBigDecimal( "price" );
    Record paymentrecord = Db.findFirst( "select sum(paid) paid from payment where orderno=" + orderno + " group by orderno" );
    BigDecimal paid = paymentrecord.getBigDecimal( "paid" );
    this.setAttr( "ordercomments", record.getStr( "comments" ) );
    this.setAttr( "paymentcomments", record.getStr( "paymentcomments" ) );
    this.setAttr( "dealprice", StrUtil.formatPrice( dealPrice ) );
    this.setAttr( "paid", StrUtil.formatPrice( paid ) );
    BigDecimal due = dealPrice.subtract( paid );
    String dues = StrUtil.formatNum( due );
    if ( due.floatValue() > 0.01 )
      this.setAttr( "dueinfo1", "还需支付:" + dues );
    else if ( due.floatValue() < -0.01 )
      this.setAttr( "dueinfo2", "挂账:" + dues );
    else
      this.setAttr( "dueinfo3", "支付完成" );
    super.index();
  }

  public void loadingPayment() {
    String orderno = this.getPara( "orderno" );
    String sql = "select CONCAT(cust.name, '-' ,cust.company) customer,pay.comments,round(pay.paid,2) paid,pay.paymenttime,pay.paymenttype";
    String sqlExcept = " from payment pay " + "left join customer cust on cust.id=pay.customerid " + "where pay.orderno = " + orderno + " order by pay.paymenttime desc";
    Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );

    this.renderJson( pager );
  }

  public void list() {
    String orderno = this.getSessionAttr( "orderno" );
    //price是原价  deal price是成交价  
    String sql = "select o.orderno,p.name name,round(bi.price,2) price,round(o.price,2) dealprice,round(o.paid,2) paid,bi.num num,oi.date date,contract.name contractname,contract.id contractid";
    String sqlExcept = " from orderitem oi " + "left join bookitem bi on oi.bookitem=bi.id " + "left join `order` o on o.id=oi.order " + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id " + "where o.orderno = " + orderno + " order by o.orderno desc";
    Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );
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
