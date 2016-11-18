package com.xcrm.attribute;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Attributeid;


public class AttributeidController extends AbstractController {
  
  
  public void save(){
    this.getModel( Attributeid.class, "" ).save();
    this.forwardIndex();
  }
  
  public void update(){
    this.getModel( Attributeid.class, "" ).update();
    this.forwardIndex();
  }

  public void remove(){
    Attributeid.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }
  

  @Override
  public String getModalName() {
    return "attributeid";
  }

  @Override
  public String getPageHeader() {
    return "属性模板设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建属性模板";
  }

  @Override
  public String getIndexHtml() {
    return "attributeid.html";
  }

  @Override
  public int getCategory() {
    // TODO Auto-generated method stub
    return 0;
  }
  
}
