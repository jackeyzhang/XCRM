package com.xcrm.attribute;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Attribute;


public class AttributeController extends AbstractController {
  
  
  public void save(){
    this.getModel( Attribute.class, "" ).save();
    this.forwardIndex();
  }
  
  public void update(){
    this.getModel( Attribute.class, "", true ).update();
    this.forwardIndex();
  }

  public void remove(){
    Attribute.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }
  

  @Override
  public String getModalName() {
    return "attribute";
  }

  @Override
  public String getPageHeader() {
    return "属性设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建属性";
  }

  @Override
  public String getIndexHtml() {
    return "attribute.html";
  }

  @Override
  public int getCategory() {
    // TODO Auto-generated method stub
    return 0;
  }
  
}
