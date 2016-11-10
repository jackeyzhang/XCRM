package com.xcrm.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;


@Before(UserInterceptor.class)
public class UserController extends AbstractController {

  public void list() {
    List<User> users = User.dao.find( "select * from user" );
    for(User user : users){
      user.put( "attriutes", User.afinder.getAllAttributeList( Constant.CATEGORY_USER ) );
    }
    this.renderJson( users );
  }

  public void save() {
    this.getModel( User.class, "" ).save();
    forwardIndex();
  }

  public void update() {
    this.getModel( User.class, "" ).update();
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
}
