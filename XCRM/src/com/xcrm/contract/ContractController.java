package com.xcrm.contract;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Contract;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.PropUtil;
import com.xcrm.common.util.StrUtil;


public class ContractController extends AbstractController {

  public void save() {
    String name = getPara( "name" );
    String editorValue = getPara( "editorValue" );
    Contract contract = new Contract();
    contract.set( "name", name ).save();
    String filename = getContractTemplatePath() + contract.getId() + ".html";
    try {
      FileUtils.write( new File( filename ), editorValue );
    }
    catch ( IOException e ) {
      e.printStackTrace();
    }
    redirect( "/" + getModalName() + "/" );
  }

  public void update() {
    Integer id = getParaToInt( "id" );
    String name = getPara( "name" );
    String editorValue = getPara( "editorValue" );
    Contract contract = new Contract();
    contract.set( "id", id ).set( "name", name ).update();
    String filename = getContractTemplatePath() + contract.getId() + ".html";
    try {
      FileUtils.write( new File( filename ), editorValue );
    }
    catch ( IOException e ) {
      e.printStackTrace();
    }
    redirect( "/" + getModalName() + "/" );
  }

  public void preadd() {
    String id = getPara( "id" );
    if ( !StringUtils.isEmpty( id ) ) {
      Contract contract = Contract.dao.findById( id );
      String filename = getContractTemplatePath() + contract.getId() + ".html";
      setAttr( "id", contract.getId() );
      setAttr( "name", contract.getName() );
      try {
        setAttr( "editorValue", FileUtils.readFileToString( new File( filename ) ) );
      }
      catch ( IOException e ) {
        e.printStackTrace();
      }
    }
    render( "/contract/add.html" );
  }

  public void remove() {
    Contract.dao.deleteById( this.getParaToInt( 0 ) );
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "contract";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改合同相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建合同";
  }

  @Override
  public String getIndexHtml() {
    return "contract.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_USER;
  }

  public void view() {
    Integer contractid = this.getParaToInt( "id" );
    Long orderno = this.getParaToLong( "orderno" );
    
    this.setAttr( "contract", contractid );
    this.setAttr( "number", orderno );
    
    ContractPrintInfo info = new ContractPrintInfo();
    info.loaddata( orderno );

    this.setAttr( "Acontract", info.getContract() );
    this.setAttr( "Acontact", info.getContact() );
    this.setAttr( "Atelephone", info.getTelephone() );
    this.setAttr( "Aaddress", info.getAddress() );

    this.setAttr( "Bcontract", PropUtil.getContractConfig( "compnayname" ) );
    this.setAttr( "Bcontact", PropUtil.getContractConfig( "compnayman" ) );
    this.setAttr( "Btelephone", PropUtil.getContractConfig( "compnaytel" ) );
    this.setAttr( "Baddress", PropUtil.getContractConfig( "compnayaddress" ) );

    this.setAttr( "orderiteminfo", info.getOrderinfo() );
    this.setAttr( "paymentinfo", PropUtil.getContractConfig( "paymentinfo" ) );
    this.setAttr( "accountinfo", PropUtil.getContractConfig( "paymentaccount" ) );

    this.setAttr( "numcount" , info.getNumcount( ) );
    this.setAttr( "amount", info.getAmount() );
    this.setAttr( "afee", info.getAfee() );//afee
    this.setAttr( "tax", info.getTax() );
    this.setAttr( "paid", info.getDue() );
    
    this.setAttr( "haspaid", info.getHaspaid() );
    this.setAttr( "notpaid", info.getNotpaid() );

    this.render( "view.html" );
  }

  private String formatContractNumber( Integer contractid ) {
    NumberFormat format = new DecimalFormat( "0000" );
    return format.format( contractid );
  }

  public void wxlistallcontracts() {
    List<Record> records = Db.find( "select name, id  from contract" );
    this.renderJson( records );
  }

}

class ContractPrintInfo {

  private String contract;
  private String contact;
  private String telephone;
  private String address;
  private String orderinfo;

  private String numcount;
  private String amount;
  private String discount;
  private String afee;
  private String due;
  private String tax;
  
  private String haspaid;
  private String notpaid;

  public void loaddata( Long orderno ) {
    String sql = "select cust.name cname, cust.shiptoAddr, cust.company, cust.phone, cust.contact, prd.name pname,"
        + "o.price due,o.taxrate tax, bi.price*bi.num*(bi.discount/100)*(o.totaldiscount/100) price, o.totalprice, o.price/o.totalprice*100 discount,"
        + "bi.additionfee afee, bi.num, bi.comments, bi.prdattrs, bi.price oprice,"
        + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"
        + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) notpaid"
        + " from `order` o" 
        + " left join orderitem oi on o.id=oi.order" 
        + " left join bookitem bi on oi.bookitem=bi.id" 
        + " left join customer cust on cust.id= bi.customer"
        + " left join product prd on prd.id=bi.product" 
        + " where o.orderno=" + orderno 
        + " and bi.status !=" + Bookitem.STATUS_CANCELLED;
    
    List<Record> records = Db.find( sql );

    StringBuilder sb = new StringBuilder();
    BigDecimal afeetotal = new BigDecimal( 0 );
    BigDecimal amounttotal = new BigDecimal( 0 );
    BigDecimal tax = new BigDecimal( 0 );
    int numcountTotal = 0;
    for ( int i = 0; i < records.size(); i++ ) {
      Record record = records.get( i );
      if ( i == 0 ) {
        setContract( record.getStr( "company" ) );
        setContact( record.getStr( "cname" ) );
        setTelephone( record.getStr( "phone" ) );
        setAddress( record.getStr( "shiptoAddr" ) );
        setDue( "" + StrUtil.formatPrice( record.getBigDecimal( "due" ) ) );//应该支付
        setHaspaid( "" + StrUtil.formatPrice( record.getBigDecimal( "paid" ) )  );
        setNotpaid( "" + StrUtil.formatPrice( record.getBigDecimal( "notpaid" ) ) );
      }
      afeetotal = afeetotal.add( record.getBigDecimal( "afee" ) );
      amounttotal = amounttotal.add( record.getBigDecimal( "price" ) );
      tax = tax.add( record.getBigDecimal( "tax" ) );
      numcountTotal +=  record.getInt( "num" );
      if ( i == records.size() - 1 ) {
        setAmount( "" + StrUtil.formatPrice( amounttotal ) );//商品小计
        setAfee( "" + StrUtil.formatPrice( afeetotal ) );//定制费
        setNumcount( "" + StrUtil.formatInt( numcountTotal ) );//数量小计
        setTax( "" + StrUtil.formatPercentage( tax.toString() ) );//税率
      }
      sb
          .append( "<tr>" )
          .append( getTD( 120, record.getStr( "pname" ), false ) )
          .append( getTD( 180, formatAttr( record.getStr( "prdattrs" ) ) , false) )
          .append( getTD( 80, StrUtil.formatInt( record.getNumber( "num" ) ) , true) )
          .append( getTD( 120, StrUtil.formatPrice( record.getNumber( "oprice" ) ) , false) )//产品原价
          .append( getTD( 120, StrUtil.formatPrice( record.getNumber( "price" ) ) , false) )//订单项金额小计
          .append( getTD( 120, StrUtil.formatPrice( record.getNumber( "afee" ) ), false ) )//订单项定制费
          .append( getTD( 300, record.getStr( "comments" ) == null ? "" : record.getStr( "comments" ) , false) )//订单项备注
          .append( "</tr>" );
    }
    this.setOrderinfo( sb.toString() );

  }

  /**
   * format attribute value
   * 
   * e.g. {"204":"白沙","205":"黄色","206":"S"}
   * 
   * @param attrValue
   * @return 白沙 黄色 S
   */
  private String formatAttr( String attrValue ) {
    if ( attrValue.contains( "-" ) ) {
      return attrValue.replace( "-", " " );
    }
    String[] attrs = attrValue.split( "," );
    String result = "";
    for ( String attr : attrs ) {
      result += attr.split( ":" )[1] + " ";
    }
    result = result.replace( "}", "" ).replace( "\"", "" );
    return result;
  }

  public String getContract() {
    return contract;
  }

  public void setContract( String contract ) {
    this.contract = contract;
  }

  public String getContact() {
    return contact;
  }

  public void setContact( String contact ) {
    this.contact = contact;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone( String telephone ) {
    this.telephone = telephone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress( String address ) {
    this.address = address;
  }

  public String getOrderinfo() {
    return orderinfo;
  }

  public void setOrderinfo( String orderinfo ) {
    this.orderinfo = orderinfo;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount( String amount ) {
    this.amount = amount;
  }

  public String getDiscount() {
    return discount;
  }

  public void setDiscount( String discount ) {
    this.discount = discount;
  }
  
  public String getTax(){
    return tax;
  }
  
  public void setTax( String tax ){
    this.tax = tax;
  }

  public String getAfee() {
    return afee;
  }

  public void setAfee( String afee ) {
    this.afee = afee;
  }
  
  public String getDue() {
    return due;
  }

  
  public void setDue( String due ) {
    this.due = due;
  }

  
  public String getHaspaid() {
    return haspaid;
  }

  
  public void setHaspaid( String haspaid ) {
    this.haspaid = haspaid;
  }

  
  public String getNotpaid() {
    return notpaid;
  }

  
  public void setNotpaid( String notpaid ) {
    this.notpaid = notpaid;
  }
  
  

  //  public String getTR( ){
  //    StringBuilder tr = new StringBuilder();
  //    tr.append( "<tr>" )
  //    .append( getTD( 182, "商品信息") )
  //    .append( getTD( 164, "型号颜色大小") )
  //    .append( getTD( 112, "数量") )
  //    .append( getTD( 134, "单价") )
  //    .append( getTD( 452, "商品备注") );
  //    tr.append( "</tr>" );
  //    return tr.toString();
  //  }
  //  
  public String getTD( int width, String text, boolean center ) {
    if( center ){
      return "<td width=\"" + width + "\" valign=\"top\" style=\"word-break: break-all;text-align:center;\">" + text + "</td>";
    }
    return "<td width=\"" + width + "\" valign=\"top\" style=\"word-break: break-all;\">" + text + "</td>";
  }

  
  public String getNumcount() {
    return numcount;
  }

  
  public void setNumcount( String numcount ) {
    this.numcount = numcount;
  }
  
  

}
