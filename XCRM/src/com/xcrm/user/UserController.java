package com.xcrm.user;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;


@Before(UserInterceptor.class)
public class UserController extends AbstractController {


  public void save() {
    User user = this.getModel( User.class, "" , true);
    user.save();
    forwardIndex(user);
  }

  public void update() {
    this.getModel( User.class, "" , true).update();
    forwardIndex();
  }

  public void remove() {
    User.dao.deleteById( this.getParaToInt( 0 ) );
    forwardIndex();
  }

  public void logoff() {
    User user = User.dao.findById( this.getPara( 0 ) );
    if ( user == null )
      return;
    user.set( "isenable", false ).update();
    forwardIndex();
  }

  public void active() {
    User user = User.dao.findById( this.getPara( 0 ) );
    if ( user == null )
      return;
    user.set( "isenable", true ).update();
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "user";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改用户相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建用户";
  }

  @Override
  public String getIndexHtml() {
    return "user.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_USER;
  }

  @Override
  protected String searchWord() {
    return "username";
  }
  
  
}
