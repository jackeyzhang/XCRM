package com.xcrm.order;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;

import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Orderitem;
import com.xcrm.common.model.Payment;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;


public class OrderController extends AbstractController {

  public void list() {
    //price是原价  deal price是成交价  
    String sql = "select ur.username username, concat(cust.name, '-' , cust.company) company, "
        + "GROUP_CONCAT(p.name) name,"
        + "o.orderno orderno,"
        + "round(o.totalprice,2) price,"
        + "round(o.price,2) dealprice,"
        + "sum(bi.num) num,"
        + "oi.date date,"
        + "contract.name contractname,"
        + "contract.id contractid,"
        + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"
        + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,"
        + "o.status";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order "
        + "left join bookitem bi on oi.bookitem=bi.id "  
        + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id " 
        + "left join customer cust on cust.id=bi.customer left join user ur on bi.user=ur.id " 
        + "where " + getSqlForUserRole()
        + " and o.status != " + Order.STATUS_CANCELLED + " "
        + this.getSearchStatement( true, "" ) + " group by o.orderno order by o.orderno desc";
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );

  }

  private void processRecords( List<Record> records ) {
    for ( Record record : records ) {
      BigDecimal paid = record.getBigDecimal( "paid" );
      BigDecimal dealprice = record.getBigDecimal( "dealprice" );
      Float amount = ( dealprice == null ? 0 : dealprice.floatValue() ) - ( paid == null ? 0 : paid.floatValue() );
      String status = "";
      if ( paid == null || paid.floatValue() == 0 ) {
        status = "已下订单";
      }
      else {
        status = amount <= 0 ? "已付全款" : "已付定金";
      }
      record.set( "status", status );
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
    return "cust.company,cust.name,o.orderno";
  }

  private String getSqlForUserRole() {
    User user = User.dao.findById( this.getCurrentUserId() );
    if ( user.isRoot() ) {
      return " bi.user is not null ";
    }
    else {
      return " bi.user=" + this.getCurrentUserId() + " ";
    }
  }
  
  private String getSqlForUserRole( String userid) {
    User user = User.dao.findById( userid );
    if ( user.isRoot() ) {
      return " bi.user is not null ";
    }
    else {
      return " bi.user=" + userid + " ";
    }
  }

  public void wxsubmitorder() {
    try{
    String postData=HttpKit.readData(this.getRequest());
    Map dataFromWX = (Map) new JSON().parse( postData );
    Object[] orderitems = (Object[])dataFromWX.get( "orderitems" );
    String totalcomments = dataFromWX.get( "totalcomments" ).toString();
    BigDecimal totalprice = new BigDecimal( dataFromWX.get( "totalprice" ).toString() );
    BigDecimal totalpricetopay = new BigDecimal( dataFromWX.get( "totalpricetopay" ).toString() );
    BigDecimal totaldiscount = new BigDecimal( dataFromWX.get( "totaldiscount" ).toString() );
    Integer paymenttype = Integer.parseInt( dataFromWX.get( "paymenttype" ).toString() );
    Integer customerid = Integer.parseInt( dataFromWX.get( "customerid" ).toString() );
    Integer contractid = Integer.parseInt( dataFromWX.get( "contractid" ).toString() );
    Integer userid = Integer.parseInt( dataFromWX.get( "userid" ).toString() );
    String deliverydate = dataFromWX.get( "deliverydate" ).toString();
    Boolean ispay = Boolean.valueOf( dataFromWX.get( "ispay" ).toString() );
    BigDecimal paid = new BigDecimal( dataFromWX.get( "paid" ).toString() );
    if ( orderitems.length > 0 ) {
      List<Integer> bookitems = new ArrayList<>();
      for ( Object orderitem : orderitems ) {
        int productid = Integer.parseInt( ( (Map)orderitem ).get( "productid" ).toString() );
        BigDecimal price = new BigDecimal( ( (Map)orderitem ).get( "price" ).toString() );
        BigDecimal afee = new BigDecimal( 0 );
        if ( ( (Map)orderitem ).get( "afee" ) != null ) {
          afee = new BigDecimal( ( (Map)orderitem ).get( "afee" ).toString() );
        }
        int count =  Integer.parseInt( ( (Map)orderitem ).get( "count" ).toString() );
        String attributes =  ( (Map)orderitem ).get( "attributes" ).toString();
        int discount = Integer.parseInt( ( (Map)orderitem ).get( "discount" ).toString() );
        String comments =  ( (Map)orderitem ).get( "comments" ).toString() ;

        Bookitem bookitem = new Bookitem();
        bookitem.setDate( new Date() );
        bookitem.setStatus( Bookitem.STATUS_ACTIVE );
        bookitem.setDiscount( discount );
        bookitem.setNum( count );
        bookitem.setProduct( productid );
        bookitem.setPrdattrs( attributes );
        bookitem.setPrice( price );
        bookitem.setComments( comments );
        bookitem.setUser( userid );
        bookitem.setCustomer( customerid );
        bookitem.setAdditionfee( afee );
        bookitem.setContract( contractid );
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
      order.setTotaldiscount( totaldiscount );
      order.save();
      
      
      if ( ispay ) {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date deliveryDate = new Date();
        try {
          deliveryDate = sdf.parse( deliverydate );
        }
        catch ( ParseException e ) {
          e.printStackTrace();
        }
        order.setDeliverytime( deliveryDate );
        
        //persist payment
        if(paid.floatValue() > 0){
          Payment payment = new Payment();
          payment.setPaymenttype( paymenttype );
          payment.setPaid( paid );
          payment.setComments( totalcomments );
          payment.setCustomerid( customerid );
          payment.setPaymenttime( new Date() );
          payment.setOrderno( order.getOrderno() );
          payment.save();
          if(order.getPrice().floatValue() - paid.floatValue() > 0.01){
            order.setStatus( Order.STATUS_PARTPAID );//2 means 已付定金
          }else{
            order.setStatus( Order.STATUS_ALLPAID );//3 means 支付完成
          }
          order.update();
        }
      }
      

      // save order items
      List<Orderitem> orderitemList = new ArrayList<>();
      for ( int bookitemid : bookitems ) {
        Orderitem orderitem = new Orderitem();
        orderitem.setBookitem( bookitemid );
        orderitem.setDate( date );
        orderitem.setOrder( order.getId() );
        orderitemList.add( orderitem );
      }
      Db.batchSave( orderitemList, orderitemList.size() );
    }
      this.renderJson( true );
    }catch( Exception e ){
      logError(e);
      this.renderJson( false );
    }
    
  }
  
  public void wxlistorders( ){
    //price是原价  deal price是成交价  
    String user = this.getPara( "user" );
    String sql = "select concat(cust.name, '-' , cust.company) company, GROUP_CONCAT(p.name) name,o.orderno orderno,"
        + "round(o.totalprice,2) price,round(o.price,2) dealprice,sum(bi.num) num,oi.date date,"
        + "contract.name contractname,contract.id contractid,"
        + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"
        + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,"
        + "o.status";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order "
        + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join product p on bi.product=p.id "
        + " left join contract contract on bi.contract=contract.id " 
        + "left join customer cust on bi.customer=cust.id " 
        + "where "+ getSqlForUserRole( user)
        + " and o.status != " + Order.STATUS_CANCELLED + " "
        + " group by o.orderno order by o.orderno desc";
    Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );
  }
  
  public void wxsearchorders( ){
    //price是原价  deal price是成交价  
    String user = this.getPara( "user" );
    String searchword = this.getPara( "searchword" );
    if( searchword == null || searchword.isEmpty() ){
      wxlistorders();
    }else{
      String sql = "select concat(cust.name, '-' , cust.company) company, GROUP_CONCAT(p.name) name,o.orderno orderno,"
          + "round(o.totalprice,2) price,round(o.price,2) dealprice,sum(bi.num) num,oi.date date,"
          + "contract.name contractname,contract.id contractid,"
          + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"
          + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,"
          + "o.status";
      String sqlExcept = " from `order` o  " 
          + "left join orderitem oi on o.id=oi.order "
          + "left join bookitem bi on oi.bookitem=bi.id " 
          + "left join product p on bi.product=p.id "
          + " left join contract contract on bi.contract=contract.id " 
          + "left join customer cust on bi.customer=cust.id " 
          + "where "+ getSqlForUserRole( user)
          + " and ( o.orderno = '" + searchword + "' "
          + " or cust.name like '%" + searchword + "%' "
          + " or cust.company like '%" + searchword + "%' )  "
          + " and o.status != " + Order.STATUS_CANCELLED + " "
          + " group by o.orderno order by o.orderno desc";
      Page<Record> page = Db.paginate( 1, 100, sql, sqlExcept );
      Pager pager = new Pager( page.getTotalRow(), page.getList() );
      this.renderJson( pager );
    }
  }
  
  public void wxbuyorderagain( ){
    String orderno = this.getPara( "orderno" );
    String sql = "select bi.product prdid, p.name prdname, bi.price price,bi.prdattrs attr,bi.num num,bi.discount, "
        + " (select ppic.fielname from productpic ppic where ppic.productid=p.id limit 1) filename, "
        + " round(bi.price*bi.discount/100,2) totalprice, "
        + " bi.comments, "
        + " bi.additionfee afee, "
        + " o.totaldiscount totaldiscount, "
        + " bi.customer custid, " 
        + " concat(cust.name, '-' , cust.company)  custname " 
        + " from orderitem oi "
        + " left join `order` o on o.id = oi.order "
        + " left join bookitem bi on oi.bookitem=bi.id " 
        + " left join product p on bi.product=p.id "
        + " left join customer cust on cust.id=bi.customer "
        + " where o.orderno  = " + orderno;
    this.renderJson( Db.find( sql ));
  }

}
