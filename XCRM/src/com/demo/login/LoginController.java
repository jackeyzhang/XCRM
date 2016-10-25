package com.demo.login;

import java.util.List;

import com.demo.common.model.User;
import com.demo.common.model.WebRecord;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(LoginInterceptor.class)
public class LoginController extends Controller {
  
    public void index(){
      render("user.html");
    }
	
	public void login(){
	  this.forwardAction( "/login/index" );
	}
	
	public void listusers(){
	  List<User> users = User.dao.find( "select * from user" );
	  WebRecord<User> record = new WebRecord<User>();
	  record.setRows( users );
	  record.setTotal( users.size() );
	  this.renderJson( record );
	}
	
	public void save(){
	  this.getModel( User.class ).save();
	  this.forwardAction( "/login/index" );
	}
}


