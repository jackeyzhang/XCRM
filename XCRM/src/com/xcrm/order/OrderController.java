package com.xcrm.order;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Orderitem;
import com.xcrm.common.model.User;
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
        + "where " + getSqlForUserRole()
        + this.getSearchStatement(true, "cust.") +"group by o.orderno order by o.orderno desc";
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept);
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
  
  private String getSqlForUserRole(){
    User user = User.dao.findById( this.getCurrentUserId() );
    if(user.isRoot()){
      return " bi.user is not null ";
    }else{
      return " bi.user=" +  this.getCurrentUserId() + " ";
    }
  }
  
  public void wxsubmitorder() {
    String oostr = this.getParaMap().get( "orderitems" )[0];
    String totalcomments = this.getParaMap().get( "totalcomments" )[0];
    BigDecimal totalprice = new BigDecimal( this.getParaMap().get( "totalprice" )[0] );
    BigDecimal totalpricetopay = new BigDecimal( this.getParaMap().get( "totalpricetopay" )[0] );
    Integer paymenttype = Integer.parseInt( this.getParaMap().get( "paymenttype" )[0] );
    Integer customerid = Integer.parseInt( this.getParaMap().get( "customerid" )[0] );
    String deliverydate = this.getParaMap().get( "deliverydate" )[0];
    if ( !oostr.isEmpty() ) {
      JSONArray jsonArray = new JSONArray( oostr );
      List<Integer> bookitems = new ArrayList< >();
      for ( int i = 0; i < jsonArray.length(); i++ ) {
        int productid = jsonArray.getJSONObject( i ).getInt( "productid" );
        BigDecimal totalPrice = new BigDecimal( jsonArray.getJSONObject( i ).getInt( "totalprice" ) );
        int count = jsonArray.getJSONObject( i ).getInt( "count" );
        String attributes = jsonArray.getJSONObject( i ).getString( "attributes" );
        int discount = jsonArray.getJSONObject( i ).getInt( "discount" );
        String comments = jsonArray.getJSONObject( i ).getString( "comments" );

        Bookitem bookitem = new Bookitem();
        bookitem.setDate( new Date() );
        bookitem.setStatus( Bookitem.STATUS_ACTIVE );
        bookitem.setDiscount( discount );
        bookitem.setNum( count );
        bookitem.setProduct( productid );
        bookitem.setPrdattrs( attributes );
        bookitem.setPrice( totalPrice );
        bookitem.setComments( comments );
        bookitem.setUser( 1 );
        bookitem.setCustomer( customerid );
        bookitem.save();
        bookitems.add( bookitem.getId() );
      }

      Order order = new Order();
      Date date = new Date();
      order.setDate( date );
      order.setOrderno( date.getTime() );
      order.setPrice( totalpricetopay );
      order.setTotalprice( totalprice );
      order.setComments( totalcomments );
      order.setPaymenttype( paymenttype );
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
      Date deliveryDate = new Date();
      try {
        deliveryDate = sdf.parse( deliverydate );
      }
      catch ( ParseException e ) {
        e.printStackTrace();
      }
      order.setDeliverytime( deliveryDate );
      order.save();

      // save order items
      List<Orderitem> orderitems = new ArrayList<>();
      for ( int bookitemid : bookitems ) {
        Orderitem orderitem = new Orderitem();
        orderitem.setBookitem(bookitemid);
        orderitem.setDate( date );
        orderitem.setOrder( order.getId() );
        orderitems.add( orderitem );
      }
      Db.batchSave( orderitems, orderitems.size() );
    }

    this.renderJson( "success" );
  }
  
}
