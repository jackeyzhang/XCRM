package com.xcrm.order;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.util.Constant;


public class OrderController extends AbstractController {

  public void list() {
    //price是原价  deal price是成交价  
    String sql = "select cust.company company, GROUP_CONCAT(p.name) name,o.orderno orderno,round(o.totalprice,2) price,round(o.price,2) dealprice,sum(bi.num) num,oi.date date,contract.name contractname,contract.id contractid,(select round(sum(paid),2) from payment where orderno= o.orderno) paid,(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,o.status";
    String sqlExcept = " from orderitem oi " + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join `order` o on o.id=oi.order " 
        + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id "
        + "left join customer cust on cust.id=bi.customer "
        + "where bi.user=? "+ this.getSearchStatement(true, "cust.") +"group by o.orderno order by o.orderno desc";
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept, getCurrentUserId() );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );

  }
  
  private void processRecords( List<Record> records ){
    for( Record record : records ){
      BigDecimal paid = record.getBigDecimal( "paid" );
      BigDecimal dealprice =record.getBigDecimal( "dealprice" );
      Float amount = (dealprice == null ? 0 : dealprice.floatValue() ) - (paid == null? 0 : paid.floatValue()) ;
      String status = "";
      if( paid==null || paid.floatValue() == 0){
        status = "已下订单";
      }else{
        status = amount <= 0 ? "已付全款" : "已付定金";
      }
      record.set( "status",  status);
    }
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

  @Override
  protected String searchWord() {
    return "company";
  }
  
  
}
