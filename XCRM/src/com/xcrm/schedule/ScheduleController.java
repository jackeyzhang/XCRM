package com.xcrm.schedule;

import com.jfinal.core.Controller;
import com.xcrm.common.model.User;


public class ScheduleController extends Controller {

  public void index() {
	setAttr("model", "schedule");
	setAttr("page_header", "管理您的预约信息");
	User user = (User)getSession().getAttribute( "currentUser" );
	setAttr("login_user", "欢迎你, " + user.get( "username" ));
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
