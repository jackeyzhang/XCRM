package com.xcrm.report;

import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;


public class ReportController extends AbstractController {
  
  @Override
  public void index() {
    super.index();
    String reportname = this.getPara();
    if(reportname != null)
    this.setAttr( "currentreport", reportname );
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
}
