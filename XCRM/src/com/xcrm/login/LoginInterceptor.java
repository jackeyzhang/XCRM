package com.xcrm.login;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.xcrm.common.util.Constant;

/**
 * LoginInterceptor 此拦截器仅做为示例展示，在本 demo 中并不需要
 */
public class LoginInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		System.out.println("Before invoking " + inv.getActionKey());
		String uri = inv.getController().getRequest().getRequestURI();
		Object user = inv.getController().getSessionAttr(Constant.CUR_USER);
		if (uri.contains(".")) {
			inv.invoke();
		} else {
			if (user != null) {
				inv.getController().setAttr("user", user);
				inv.invoke();
			} else {
				if (uri.equals("/login/login") || uri.equals("/")) {
					inv.invoke();
				} else {
					inv.getController().redirect("/");
				}
			}
		}

		System.out.println("After invoking " + inv.getActionKey());
	}
}
