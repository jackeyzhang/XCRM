package com.xcrm.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

  public void querying() {
    Date startdate = this.getParaToDate( "startdate" );
    Date enddate = this.getParaToDate( "enddate" );
    //Integer orderstatus = getParaToInt( "orderstatus" );
    //query
    String sql = "select prd.name productname,cust.company companyname,ord.date orderdate,sum(bi.num) productncount "
        + "from xcrm.product prd left join xcrm.bookitem bi on prd.id = bi.product "
        + "left join xcrm.orderitem oi on oi.bookitem = bi.id "
        + "left join xcrm.order ord on oi.order = ord.id "
        + "left join xcrm.customer cust on cust.id=bi.customer "
        + "where ord.date>=? and ord.date<=? "
        + "group by prd.name, cust.company, ord.id order by prd.name ";
    List<Record> records = Db.find(  sql, startdate, enddate  );
    //groupRecordsByField(records, "productncount", "productname");
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
  
  private void groupRecordsByField( List<Record> records, String calField, String groupfield ){
    int groupIndex = 0;
    String currentSplitkey = "";
    BigDecimal count = new BigDecimal( 0 );
    for( int i = 0; i < records.size(); i++){
      Record record = records.get( i );
      String splitkey = record.getStr( groupfield );
      if(currentSplitkey.isEmpty()){
        currentSplitkey = splitkey;
      }
      count.add( record.getBigDecimal( calField ) );
      if(currentSplitkey.equals( splitkey ) && i != records.size()-1){
        continue;
      }else{
        currentSplitkey = splitkey;
        groupIndex = i;
        Record grouprecord = new Record();
        grouprecord.set( calField, count );
        grouprecord.set( groupfield, splitkey );
        records.add( groupIndex, grouprecord );
      }
      
    }
  }
  
}
