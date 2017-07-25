package com.xcrm.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.DateUtil;


public class ReportController extends AbstractController {
  
  public static final String START = "startdate";
  public static final String END = "enddate";

  @Override
  public void index() {
    super.index();
    String reportname = this.getPara();
    if ( reportname != null )
      this.setAttr( "currentreport", reportname );
  }

  /**
   * query product sales
   */
  public void queryingproductsales() {
    Date startdate = this.getParaToDate( START );
    Date enddate = this.getParaToDate( END );
    enddate = DateUtil.get23h59m59sOfOneDay( enddate );
    String orderstatus = getPara( "orderstatus" );
    //query
    String sql = "select prd.name productname,CONCAT(cust.name,'-',cust.company) companyname,ord.date orderdate,sum(bi.num) productcount,ord.orderno,ord.deliverytime,ord.status orderstatus,user.username saler "
        + "from product prd left join bookitem bi on prd.id = bi.product "
        + "left join orderitem oi on oi.bookitem = bi.id "
        + "left join `order` ord on oi.order = ord.id "
        + "left join customer cust on cust.id=bi.customer "
        + "left join user user on user.id=bi.user "
        + "where ord.date>=? and ord.date<=? "
        + getAndWhereWithRoleSql()
        +  ( orderstatus == null || orderstatus.isEmpty() ? " " : " and ord.status in( " + orderstatus + ") ")
        + "group by prd.name, cust.company, ord.id order by prd.name ";
    List<Record> records = Db.find(  sql, startdate, enddate  );
    records = groupRecordsByField( "productname", records,  "productcount");
    this.renderJson( records );
  }
  
  /**
   * query order payment order
   */
  public void queryingorderpaymentreport() {
    Date startdate = this.getParaToDate( START );
    Date enddate = this.getParaToDate( END );
    enddate = DateUtil.get23h59m59sOfOneDay( enddate );
    Boolean topay = this.getParaToBoolean( "topay" );
    Boolean todue = this.getParaToBoolean( "todue" );
    List<Record> records = Db.find(  getOrderAnalyzeSql("o.orderno"), startdate, enddate  );
    records = filterOrderPaymentRecords( records, topay, todue );
    addTotalRecords( "orderno", records, "productcount", "price", "dealprice", "due", "paid" );
    this.renderJson( records );
  }
  
  /**
   * query customer report
   */
  public void queryingcustomerreport(){
    Date startdate = this.getParaToDate( START );
    Date enddate = this.getParaToDate( END );
    enddate = DateUtil.get23h59m59sOfOneDay( enddate );
    List<Record> records = Db.find(  getOrderAnalyzeSql("cust.company"), startdate, enddate  );
    records = groupRecordsByField( "companyname", records,  "productcount", "price", "dealprice", "due", "paid");
    this.renderJson( records );
  }
  
  /**
   * query sales report
   */
  public void queryingsalereport(){
    Date startdate = this.getParaToDate( START );
    Date enddate = this.getParaToDate( END );
    enddate = DateUtil.get23h59m59sOfOneDay( enddate );
    List<Record> records = Db.find(  getOrderAnalyzeSql("user.username"), startdate, enddate  );
    records = groupRecordsByField( "saler", records,  "productcount", "price", "dealprice", "due", "paid");
    this.renderJson( records );
  
  }

  @Override
  public String getModalName() {
    return "report";
  }

  @Override
  public String getPageHeader() {
    return "报表管理";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "";
  }

  @Override
  public String getIndexHtml() {
    return "report.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_SCHEDULE;
  }
  
  private String getOrderAnalyzeSql( String orderbyField ){
    //price是原价  deal price是成交价  
   return  "select CONCAT(cust.name,'-',cust.company) companyname, "
        + "GROUP_CONCAT(p.name) name,"
        + "sum(bi.num) productcount,"
        + "oi.date orderdate,"
        + "contract.name contractname,"
        + "contract.id contractid,"
        + "o.orderno orderno,"
        + "round(o.totalprice,2) price,"//原价
        + "round(o.price,2) dealprice,"//成交价格
        + "round(o.price/o.totalprice*100,2) discountrate,"//折扣
        + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,"//应付
        + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"//已付
        + "o.status orderstatus,"
        + "user.username saler"
        + " from orderitem oi " 
        + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join `order` o on o.id=oi.order " 
        + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id "
        + "left join customer cust on cust.id=bi.customer "
        + "left join user user on user.id=bi.user "
        + "where o.date>=? and o.date<=? "
        + getAndWhereWithRoleSql()
        + "and o.status != " + Order.STATUS_CANCELLED
        + " group by o.orderno order by " +  orderbyField + " desc";
  }
  
  
  private List<Record> groupRecordsByField( String groupbyfield, List<Record> records,  String... calFields ){
    BigDecimal[] countall = new BigDecimal[calFields.length];
    BigDecimal[] count = new BigDecimal[calFields.length];
    for( int i=0;i<calFields.length; i++ ){
      countall[i] = new BigDecimal( 0 );
      count[i] = new BigDecimal( 0 );
    }
    
    Map<String,List<Record>> groupmap = new LinkedHashMap<String,List<Record>>();
    for( Record record : records ){
      String splitkey = record.getStr( groupbyfield ) == null ? "其他" : record.getStr( groupbyfield );
      if(groupmap.containsKey( splitkey )){
        groupmap.get( splitkey ).add( record );
      }else{
        List<Record> tempRecords = new ArrayList<Record>() ;
        tempRecords.add( record );
        groupmap.put( splitkey, tempRecords);
      }
    }
    
    for( String key : groupmap.keySet() ){
      for( int i=0;i<calFields.length; i++ ){
        count[i] = new BigDecimal( 0 );
      }
      for( Record record : groupmap.get( key )){
        for( int index = 0; index < calFields.length; index++ ){
          BigDecimal value = record.getBigDecimal( calFields[index] ) == null ? new BigDecimal( 0 ) : record.getBigDecimal( calFields[index] );
          count[index] = count[index].add( value );
          countall[index] = countall[index].add( value );
        }
      }
      Record grouprecord = new Record();
      grouprecord.set( groupbyfield, key );
      for ( int j = 0; j < count.length; j++ ) {
        grouprecord.set( calFields[j], count[j] );
      }
      grouprecord.set( "isgroup", true );
      groupmap.get( key ).add( 0, grouprecord );
    }
    
    
    List<Record> result = new ArrayList<Record>();
    for( String key : groupmap.keySet() ){
      result.addAll( groupmap.get( key ) );
    }
    
    Record totalrecord = new Record();
    for ( int j = 0; j < countall.length; j++ ) {
      totalrecord.set( calFields[j], countall[j] );
    }
    totalrecord.set( groupbyfield, "总计" );
    totalrecord.set( "istotal", true );
    result.add( 0, totalrecord );
    
    return result;
  }
  
  private List<Record> filterOrderPaymentRecords( List<Record> records, Boolean topay, Boolean todue ) {
    if ( topay && todue )
      return records;
    if ( topay ) {
      Iterator<Record> iter = records.iterator();
      while(iter.hasNext()){
        Record record = iter.next();
        if( record.getBigDecimal( "due" ).floatValue() > 0){
          iter.remove();
        }
      }
      return records;
    }
    if ( todue ) {
      Iterator<Record> iter = records.iterator();
      while(iter.hasNext()){
        Record record = iter.next();
        if( record.getBigDecimal( "due" ).floatValue() < 0){
          iter.remove();
        }
      }
      return records;
    }
    return records;
  }
  
  private void addTotalRecords( String totalfield, List<Record> records, String... calculatefields ) {
    BigDecimal[] countall = new BigDecimal[calculatefields.length];
    for ( int i = 0; i < records.size(); i++ ) {
      for ( int j = 0; j < countall.length; j++ ) {
        if ( countall[j] == null ) {
          countall[j] = new BigDecimal( 0 );
        }
        BigDecimal value = records.get( i ).getBigDecimal( calculatefields[j] );
        countall[j] = countall[j].add( value == null ? new BigDecimal( 0 ) : value );
      }
    }
    Record totalrecord = new Record();
    totalrecord.set( totalfield, "总计" );
    totalrecord.set( "istotal", true );
    for ( int i = 0; i < countall.length; i++ ) {
      totalrecord.set( calculatefields[i], countall[i] );
    }
    records.add( 0,  totalrecord);
  }
  
  
  private String getAndWhereWithRoleSql() {
    User user = User.dao.findById( getCurrentUserId() );
    if ( user.isRoot() ) {
      return "";
    }
    else {
      return " and bi.user= " + getCurrentUserId() + " ";
    }
  }
  
}
