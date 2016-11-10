package com.xcrm.schedule;

import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;


public class ScheduleController extends AbstractController {


  @Override
  public String getModalName() {
    return "schedule";
  }

  @Override
  public String getPageHeader() {
    return "管理您的预约信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "";
  }

  @Override
  public String getIndexHtml() {
    return "schedule.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_SCHEDULE;
  }
}
