package com.xcrm.login;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;

@Before(LoginInterceptor.class)
public class LoginController extends Controller {
	
   @Before(LoginValidator.class)
	public void login(){
	  User user = this.getModel( User.class );
	  User dbUser = User.dao.findFirst( "select * from user where username=?  and password=?", user.getStr( "username" ), user.getStr( "password" ) );
	  if(dbUser!=null){
	    this.getSession().setAttribute( "currentUser", user );
	    this.forwardAction( "/user/index" );
	  }else{
	    this.setAttr( "login_error", "用户名或密码错误,请重新输入!" );
	    render( "/index/index.html" );
	  }
	}
	
	
}


