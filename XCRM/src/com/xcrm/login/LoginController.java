package com.xcrm.login;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.StrUtil;


@Before(LoginInterceptor.class)
public class LoginController extends Controller {

  @Before(LoginValidator.class)
  public void login() {
    User user = this.getModel( User.class );
    User dbUser = User.dao.findFirst( "select * from user where (username=? or email=?) and password=? and isenable = true", user.getStr( "username" ).trim(),
        user.getStr( "username" ).trim(), user.getStr( "password" ) );
    if ( dbUser != null && dbUser.canLoginToWeb() ) {
      this.setSessionAttr( Constant.CUR_USER, dbUser.getAttrs() );
      this.redirect( "/order/index" );
    }
    else {
      if ( dbUser == null ) {
        this.setAttr( "login_error", "用户名或密码错误,请重新输入!" );
      }
      else {
        this.setAttr( "login_error", "您的权限尚不能登录该系统，请通过手机端进行操作或者联系管理员改变权限!" );
      }
      this.setAttr( "model", "login" );
      render( "/index/index.html" );
    }
  }

  public void wxlogin() {
    String username = this.getPara( "user" );
    String password = this.getPara( "password" );
    Integer role = StrUtil.isEmpty( getPara( "userrole" ) ) ? null : getParaToInt( "userrole" );
    if ( username == null || username.isEmpty() || password == null || password.isEmpty() ) {
      this.renderJson( false );
    }
    User dbUser = User.dao.findFirst( "select * from user where (username=? or email=?) and password=? and isenable = true", username.trim(), username.trim(), password );
    if ( dbUser != null ) {
      if ( role == null ) {
        this.renderJson( dbUser );
      }
      else if ( role == User.ROLE_DEP_MANAGER ) { //登录主管系统，不允许工人和销售
        if ( dbUser.isWorker() || dbUser.isSaler() ) {
          this.renderJson( false );
        }else{
          this.renderJson( dbUser );
        }
      }
      else if ( role == User.ROLE_WORKER && dbUser.isSaler() ) { //登录工人系统，不允许销售
        this.renderJson( false );
      }
      else {
        this.renderJson( dbUser );
      }
    }
    else {
      this.renderJson( false );
    }

  }

}
