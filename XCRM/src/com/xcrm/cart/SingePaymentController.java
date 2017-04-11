package com.xcrm.cart;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Payment;
import com.xcrm.common.util.Constant;


public class SingePaymentController extends AbstractController {

  @Override
  protected void preSetAttribute() {
    String orderno = this.getPara( "orderno" );
    String sql = "select CONCAT(cust.name, '-' ,cust.company) customer,cust.id customerid,(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due " + "from `order` o "
        + "left join orderitem oi on oi.order=o.id " + "left join bookitem bi on oi.bookitem=bi.id  " + "left join customer cust on cust.id=bi.customer "
        + "where o.orderno=" + orderno;
    Record record = Db.findFirst( sql );
    this.setAttr( "customer", record.getStr( "customer" ) );
    this.setAttr( "due", record.getBigDecimal( "due" ) );
    this.setSessionAttr( "customerid", record.getInt( "customerid" ) );
    this.setSessionAttr( "spay-orderno", orderno );
    this.setSessionAttr( "due", record.getBigDecimal( "due" ) );
  }

  public void submitorder() {
    String orderno = this.getSessionAttr( "spay-orderno" );
    Integer paymenttype = getParaToInt( "paymenttype" );
    BigDecimal paid = new BigDecimal ( Float.parseFloat( getPara( "paid" ).isEmpty() ? "0.0" : getPara( "paid" ) ));
    String paymentcomments = getPara( "paymentcomments" );
    //persist payment
    if ( paid.floatValue() > 0 ) {
      Payment payment = new Payment();
      payment.setPaymenttype( paymenttype );
      payment.setPaid( paid );
      payment.setComments( paymentcomments );
      Integer customerid = this.getSessionAttr( "customerid" );
      payment.setCustomerid( customerid );
      payment.setPaymenttime( new Date() );
      payment.setOrderno( Long.parseLong( orderno ) );
      payment.save();
      BigDecimal due = getSessionAttr( "due" );
      if( due.floatValue() - paid.floatValue() <= 0.001 ){
        Order ord = Order.dao.findFirst( "select * from `order` where orderno=?", Long.parseLong( orderno )  );
        if(ord != null){
          ord.setStatus( 3 );//3 means 支付完成
          ord.update();
        }
      }else{
        Order ord = Order.dao.findFirst( "select * from `order` where orderno=?",  Long.parseLong( orderno )  );
        if(ord != null){
          ord.setStatus( 2 );//2 means 支付定金
          ord.update();
        }
      }
      this.removeSessionAttr( "customerid" );
      this.removeSessionAttr( "spay-orderno" );
      this.removeSessionAttr( "due" );
    }
    this.renderNull();
  }

  @Override
  public String getModalName() {
    return "cart";
  }

  @Override
  public String getPageHeader() {
    return "付款页面";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "";
  }

  @Override
  public String getIndexHtml() {
    return "spayment.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_CARTLIST;
  }
}
