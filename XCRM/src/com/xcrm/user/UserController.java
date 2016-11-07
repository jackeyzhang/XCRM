package com.xcrm.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;


@Before(UserInterceptor.class)
public class UserController extends Controller {

  public void index() {
	setAttr("model", "user");
	setAttr("page_header", "创建或修改用户相关信息");
	setAttr("toolbar_create", "创建用户");
    render( "user.html" );
  }

  public void list() {
    List<User> users = User.dao.find( "select * from user" );
//    users.stream().forEach(  c->c.set( "isenable", true ).update() );
    this.renderJson( users );
  }
  
  public void save(){
    this.getModel( User.class, "" ).save();
    this.forwardAction( "/user/index" );
  }
  
  public void update(){
    this.getModel( User.class, "" ).update();
    this.forwardAction( "/user/index" );
  }

  public void remove(){
    User.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardAction( "/user/index" );
  }
  
  public void logoff(){
    User user = User.dao.findById( this.getPara(0) );
    if(user == null ) return;
    user.set( "isenable", false ).update();
    this.forwardAction( "/user/index" );
  }
}
