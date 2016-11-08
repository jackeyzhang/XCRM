package com.xcrm.schedule;

import com.xcrm.common.AbstractController;


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
}
