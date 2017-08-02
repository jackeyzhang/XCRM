package com.xcrm.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Order;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.RecordUtil;
import com.xcrm.common.util.StrUtil;
import com.xcrm.common.util.Tuple;


public class OrderViewController extends AbstractController {

  public void index() {
    String orderno = this.getPara( "orderno" );
    this.setSessionAttr( "orderno", orderno );
    this.setAttr( "orderno", orderno );

    Record record = Db.findFirst( "select o.comments,o.paymentcomments,o.price,o.totaldiscount from `order` o where o.orderno=" + orderno );
    BigDecimal dealPrice = record.getBigDecimal( "price" );
    BigDecimal totaldiscount = record.getBigDecimal( "totaldiscount" );
    Record paymentrecord = Db.findFirst( "select sum(paid) paid, customerid customer from payment where orderno=" + orderno + " group by orderno" );
    BigDecimal paid = paymentrecord == null ? new BigDecimal(0) : paymentrecord.getBigDecimal( "paid" );
    Record customerrecord = Db.findFirst( "select bi.customer from bookitem bi left join orderitem oi on oi.bookitem = bi.id left join `order` ord on oi.order = ord.id where ord.orderno = " + orderno );
    Integer customerid = customerrecord == null ? null : customerrecord.getInt( "customer" );
    this.setAttr( "ordercomments", record.getStr( "comments" ) );
    this.setAttr( "customer", customerid );
    this.setAttr( "paymentcomments", record.getStr( "paymentcomments" ) );
    this.setAttr( "dealprice", StrUtil.formatPrice( dealPrice ) );
    this.setAttr( "paid", StrUtil.formatPrice( paid ) );
    this.setAttr( "totaldiscount", StrUtil.formatNum(totaldiscount) );
    BigDecimal due = dealPrice.subtract( paid );
    String dues = StrUtil.formatPrice( due );
    if ( due.floatValue() > 0.001 )
      this.setAttr( "dueinfo1", "还需支付:" + dues );
    else if ( due.floatValue() < -0.001 )
      this.setAttr( "dueinfo2", "挂账:" + dues );
    else
      this.setAttr( "dueinfo3", "支付完成" );
    Tuple<Number,Number> tuple = this.getTotalDueAndBillAndBalance( customerid );
    this.setAttr( "own", StrUtil.formatPrice(  tuple.getFirst() ) );
    this.setAttr( "bill", StrUtil.formatPrice(   tuple.getSecond()  ) );
    Number own = tuple.getFirst() == null ? 0 :   tuple.getFirst();
    Number bill = tuple.getSecond() == null ? 0 :  tuple.getSecond();
    this.setAttr( "balance", StrUtil.formatPrice( own.floatValue() + bill.floatValue()  ) );
    super.index();
  }

  public void loadingPayment() {
    String orderno = this.getPara( "orderno" );
    this.renderJson( getPayment( orderno ) );
  }

  public void list() {
    String orderno = this.getSessionAttr( "orderno" );
    this.renderJson( getOrderItemViews( orderno ) );
  }
  
  public void loadingAllPayments(){
    String customerid = this.getPara( "customerid" );
    String showall = this.getPara("showall");
    Boolean showAll = Boolean.valueOf( showall );
    if( customerid == null || customerid.isEmpty() ) {
      this.renderNull();
    }else{
      this.renderJson( getAllPayments( customerid, showAll ) );
    }
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
  
  public void wxvieworder() {
    String orderno = this.getPara( "orderno" );
    Pager pager1 = getOrderItemViews( orderno );
    Pager pager2 = getPayment( orderno );
    List<Record> records1 = pager1.getRows();
    List<Record> records2 = pager2.getRows();
    Map<String, Object> json = new HashMap<String, Object>();
    json.put( "orderitems", records1 );
    if ( !records2.isEmpty() ) {
      json.put( "payments", records2 );
    }
    this.renderJson( json );
  }
  
  
  private Pager getOrderItemViews( String orderno ){
    //price是原价  deal price是成交价  
    String imgpath = getPrdImgBaseUrl() ;
    String sql = "select concat('"+imgpath+"',CAST(p.id AS CHAR),'/',(select ppic.fielname from productpic ppic where ppic.productid=p.id limit 1)) filename," 
        +" o.orderno,o.status,p.name name,bi.prdattrs attr,round(bi.price,2) price,round(o.price,2) dealprice,round(bi.price*bi.num*bi.discount/100*o.totaldiscount/100,2) itemamount,"
        + "round(o.paid,2) paid,bi.num num,bi.discount discount,oi.date date,o.deliverytime,contract.name contractname,contract.id contractid,bi.comments comments,bi.additionfee afee";
    String sqlExcept = " from orderitem oi " 
        + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join `order` o on o.id=oi.order and o.status != " + Order.STATUS_CANCELLED + " " 
        + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id " + "where bi.status = 1 and o.orderno = " + orderno + " order by o.orderno desc";
    Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), RecordUtil.fillRowNumber( page.getList() ) );
    return pager;
  }
  
  private Pager getPayment( String orderno ){
    String sql = "select CONCAT(cust.name, '-' ,cust.company) customer,pay.comments,round(pay.paid,2) paid,pay.paymenttime,pay.paymenttype";
    String sqlExcept = " from payment pay " + "left join customer cust on cust.id=pay.customerid " + "where pay.orderno = " + orderno + " order by pay.paymenttime desc";
    Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    return pager;
  }
  
  private Pager getAllPayments( String customerid, Boolean showAll ){
    String sql = 
        "select  ord.orderno, "
        + "oo.customer,"
        + "oo.saler,"
        + "ord.date orderdate, "
        + "round(ord.totalprice,2) price, "
        + "round(ord.price,2) dealprice,"
        + "round(ifnull(sum(pay.paid),0),2) paid, "
        + "round((ord.price- ifnull(sum(pay.paid),0)),2) own ";
    String sqlExcept = 
        " from `order` ord "
        + "left join payment pay on ord.orderno = pay.orderno "
        + "join ( select distinct(oi.order) oid,concat(cust.name, '-',cust.company ) customer, u.username saler "
        + "from orderitem oi "
        + "left join bookitem bi on oi.bookitem = bi.id "
        + "left join customer cust on cust.id= bi.customer "
        + "left join user u on u.id = bi.user "
        + "where bi.customer= "+ customerid +") oo on oo.oid = ord.id "
        + "where ord.status !=" + Order.STATUS_CANCELLED + " "
        + "group by ord.orderno order by ord.orderno desc ";
    Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
    List<Record> records = page.getList().stream().filter( p -> {
      if(showAll) return true;
      return p.getBigDecimal( "own" ).floatValue() != 0;
    } ).collect( Collectors.toList() );
    Pager pager = new Pager( page.getTotalRow(), records );
    return pager;
  }
  
  private Tuple<Number,Number> getTotalDueAndBillAndBalance( int customerid ){
  
    String sql = 
        "select ifnull(sum(ooo.own),0) own from ( select "
        + "round((ord.price- ifnull(sum(pay.paid),0)),2) own "
        + " from `order` ord "
        + "left join payment pay on ord.orderno = pay.orderno "
        + "join ( select distinct(oi.order) oid,concat(cust.name, '-',cust.company ) customer "
        + "from orderitem oi "
        + "left join bookitem bi on oi.bookitem = bi.id "
        + "left join customer cust on cust.id= bi.customer "
        + "where bi.customer= "+ customerid +") oo on oo.oid = ord.id "
        + "where ord.status !=" + Order.STATUS_CANCELLED + " "
        + "group by ord.orderno "
        + ") ooo where ooo.own > 0 ";
    Record record = Db.findFirst( sql );
    
    String sql1 = 
            "select ifnull(sum(ooo.own),0) own from ( select "
            + "round((ord.price- ifnull(sum(pay.paid),0)),2) own "
            + " from `order` ord "
            + "left join payment pay on ord.orderno = pay.orderno "
            + "join ( select distinct(oi.order) oid,concat(cust.name, '-',cust.company ) customer "
            + "from orderitem oi "
            + "left join bookitem bi on oi.bookitem = bi.id "
            + "left join customer cust on cust.id= bi.customer "
            + "where bi.customer= "+ customerid +") oo on oo.oid = ord.id "
            + "where ord.status !=" + Order.STATUS_CANCELLED + " "
            + "group by ord.orderno "
            + ") ooo where ooo.own < 0 ";
    Record record1 = Db.findFirst( sql1 );
    Tuple<Number,Number> number = new Tuple<Number,Number>(  record.getBigDecimal( "own" )  ,record1.getBigDecimal( "own" ) );
    return number;
  }
}
