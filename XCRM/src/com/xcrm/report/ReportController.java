package com.xcrm.report;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;


public class ReportController extends AbstractController {

  @Override
  public void index() {
    super.index();
    String reportname = this.getPara();
    if ( reportname != null )
      this.setAttr( "currentreport", reportname );
  }

  public void queryingproductsales() {
    Date startdate = this.getParaToDate( "startdate" );
    Date enddate = this.getParaToDate( "enddate" );
    Calendar endcalendar = Calendar.getInstance();
    endcalendar.setTime( enddate );
    endcalendar.set( Calendar.HOUR_OF_DAY, 23 );
    endcalendar.set( Calendar.MINUTE, 59 );
    endcalendar.set( Calendar.SECOND, 59 );
    enddate = endcalendar.getTime();
    Integer orderstatus = getParaToInt( "orderstatus" );
    //query
    String sql = "select prd.name productname,cust.company companyname,ord.date orderdate,sum(bi.num) productcount,ord.deliverytime,ord.status orderstatus "
        + "from xcrm.product prd left join xcrm.bookitem bi on prd.id = bi.product "
        + "left join xcrm.orderitem oi on oi.bookitem = bi.id "
        + "left join xcrm.order ord on oi.order = ord.id "
        + "left join xcrm.customer cust on cust.id=bi.customer "
        + "where ord.date>=? and ord.date<=? "
        + " and bi.user= " + getCurrentUserId() + " "
        +  (orderstatus == 0 ? "" : " and ord.status = " + orderstatus + " ")
        + "group by prd.name, cust.company, ord.id order by prd.name ";
    List<Record> records = Db.find(  sql, startdate, enddate  );
    groupRecordsByField(records, "productcount", "productname", true);
    this.renderJson( records );
  }
  
  public void queryingorderpaymentreport() {
    Date startdate = this.getParaToDate( "startdate" );
    Date enddate = this.getParaToDate( "enddate" );
    Calendar endcalendar = Calendar.getInstance();
    endcalendar.setTime( enddate );
    endcalendar.set( Calendar.HOUR_OF_DAY, 23 );
    endcalendar.set( Calendar.MINUTE, 59 );
    endcalendar.set( Calendar.SECOND, 59 );
    enddate = endcalendar.getTime();
    Boolean topay = this.getParaToBoolean( "topay" );
    Boolean todue = this.getParaToBoolean( "todue" );
    //price是原价  deal price是成交价  
    String sql = "select cust.company companyname, "
        + "GROUP_CONCAT(p.name) name,"
        + "sum(bi.num) productcount,"
        + "oi.date orderdate,"
        + "contract.name contractname,"
        + "contract.id contractid,"
        + "o.orderno orderno,"
        + "round(o.totalprice,2) price,"//原价
        + "round(o.price,2) dealprice,"//成交价格
        + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,"//应付
        + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"//已付
        + "o.status orderstatus"
        + " from orderitem oi " 
        + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join `order` o on o.id=oi.order " 
        + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id "
        + "left join customer cust on cust.id=bi.customer "
        + "where o.date>=? and o.date<=? "
        + "and bi.user= " + getCurrentUserId() + " "
        +"group by o.orderno order by o.orderno desc";
    List<Record> records = Db.find(  sql, startdate, enddate  );
    records = filterOrderPaymentRecords( records, topay, todue );
    groupAllRecords( "orderno", records, "productcount", "price", "dealprice", "due", "paid" );
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
  
  private void groupRecordsByField( List<Record> records, String calField, String groupfield, boolean includeAll ){
    int groupIndex = 0;
    String currentSplitkey = "";
    BigDecimal countall = new BigDecimal( 0 );
    BigDecimal count = new BigDecimal( 0 );
    Map<Integer,Record> map = new LinkedHashMap<Integer,Record>();
    for( int i = 0; i < records.size(); i++){
      Record record = records.get( i );
      String splitkey = record.getStr( groupfield );
      if(currentSplitkey.isEmpty()){
        currentSplitkey = splitkey;
      }
      if(currentSplitkey.equals( splitkey ) && i != records.size()-1){
        count = count.add( record.getBigDecimal( calField ) );
        countall = countall.add( record.getBigDecimal( calField ) );
        continue;
      }else{
        if(i == records.size()-1){
          count = count.add( record.getBigDecimal( calField ) );
          countall = countall.add( record.getBigDecimal( calField ) );
        }
        Record grouprecord = new Record();
        grouprecord.set( calField, count );
        grouprecord.set( groupfield, currentSplitkey );
        grouprecord.set( "isgroup", true );
        map.put( groupIndex, grouprecord );
        groupIndex = i+1;
        count = new BigDecimal( 0 );
        currentSplitkey = splitkey;
      }
    }
    for( Integer index : map.keySet()){
      records.add( index, map.get( index ) );
    }
    if(includeAll){
      Record totalrecord = new Record();
      totalrecord.set( calField, countall );
      totalrecord.set( groupfield, "总计" );
      totalrecord.set( "istotal", true );
      records.add( 0, totalrecord );
    }
  }
  
  private List<Record> filterOrderPaymentRecords( List<Record> records, Boolean topay, Boolean todue ) {
    if ( topay && todue )
      return records;
    if ( topay ) {
      return records.stream().filter( r -> r.getBigDecimal( "due" ).floatValue() < 0 ).collect( Collectors.toList() );
    }
    if ( todue ) {
      return records.stream().filter( r -> r.getBigDecimal( "due" ).floatValue() > 0 ).collect( Collectors.toList() );
    }
    return records;
  }
  
  private void groupAllRecords( String groupfield, List<Record> records, String... calculatefields ) {
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
    totalrecord.set( groupfield, "总计" );
    totalrecord.set( "istotal", true );
    for ( int i = 0; i < countall.length; i++ ) {
      totalrecord.set( calculatefields[i], countall[i] );
    }
    records.add( 0,  totalrecord);
  }
  
}
