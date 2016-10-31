package com.xcrm.login;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class LoginValidator extends Validator {
	
	protected void validate(Controller controller) {
	  validateRequiredString("user.username", "nameMsg", "请输入用户名");
	  validateRequiredString("user.password", "passMsg", "请输入密码");
	}
	
	protected void handleError(Controller c) {
	  c.keepPara("user.username");
	  c.render( "/index/index.html" );
	}
}
