package com.xcrm.login;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;

@Before(LoginInterceptor.class)
public class LoginController extends Controller {
	
	public void login(){
	  this.getModel( User.class );
	  this.forwardAction( "/user/index" );
	}
	
	
}


