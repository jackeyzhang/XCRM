package com.xcrm.schedule;

import com.jfinal.core.Controller;


public class ScheduleController extends Controller {

  public void index() {
	setAttr("model", "schedule");
	setAttr("page_header", "管理您的预约信息");
    render( "schedule.html" );
  }

  public void list() {
  }
  
  public void save(){
  }
  
  public void update(){
  }

  public void remove(){
  }
}
