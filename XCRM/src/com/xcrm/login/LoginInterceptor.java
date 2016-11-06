package com.xcrm.login;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.xcrm.common.util.Constant;

/**
 * check user session is valid, if yes then going on, else go to root page.
 */
public class LoginInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		String uri = inv.getController().getRequest().getRequestURI();
		Object user = inv.getController().getSessionAttr(Constant.CUR_USER);
		if (uri.contains(Constant.DOT)) {
			inv.invoke();
		} else {
			if (user != null) {
				inv.getController().setAttr("user", user);
				inv.invoke();
			} else {
				if (uri.equals(Constant.LOGIN_ACTION) || uri.equals(Constant.SLASH)) {
					inv.invoke();
				} else {
					inv.getController().redirect(Constant.SLASH);
				}
			}
		}
	}
}
