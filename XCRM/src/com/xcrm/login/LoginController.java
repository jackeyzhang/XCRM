package com.xcrm.login;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;

@Before(LoginInterceptor.class)
public class LoginController extends Controller {
	
	public void login(){
	  User user = this.getModel( User.class );
	  User dbUser = User.dao.findFirst( "select * from user where username=?  and password=?", user.getStr( "username" ), user.getStr( "password" ) );
	  if(dbUser!=null){
	    this.getSession().setAttribute( "currentUser", user );
	    this.forwardAction( "/user/index" );
	  }else{
	    this.forwardAction( "/index" );
	  }
	}
	
	
}


