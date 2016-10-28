package com.xcrm.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;
import com.xcrm.common.model.WebRecord;


@Before(UserInterceptor.class)
public class UserController extends Controller {

  public void index() {
    render( "user.html" );
  }

  public void listusers() {
    List<User> users = User.dao.find( "select * from user" );
    WebRecord<User> record = new WebRecord<User>();
    record.setRows( users );
    record.setTotal( users.size() );
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
}
