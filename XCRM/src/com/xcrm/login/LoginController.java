package com.xcrm.login;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;

@Before(LoginInterceptor.class)
public class LoginController extends Controller {

	@Before(LoginValidator.class)
	public void login() {
		User user = this.getModel(User.class);
		User dbUser = User.dao.findFirst("select * from user where username=? or email=? and password=?",
				user.getStr("username").trim(), user.getStr("username").trim(), user.getStr("password"));
		if (dbUser != null) {
			this.setSessionAttr(Constant.CUR_USER, dbUser.getAttrs());
			this.redirect("/user/index");
		} else {
			this.setAttr("login_error", "用户名或密码错误,请重新输入!");
			this.setAttr("model", "login");
			render("/index/index.html");
		}
	}

}
