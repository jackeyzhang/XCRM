package com.xcrm.attribute;

import java.util.List;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Attribute;


public class AttributeController extends AbstractController {
  
  public void list() {
    List<Attribute> Attributes = Attribute.dao.find( "select * from attribute" );
    this.renderJson( Attributes );
  }
  
  public void save(){
    this.getModel( Attribute.class, "" ).save();
    this.forwardIndex();
  }
  
  public void update(){
    this.getModel( Attribute.class, "" ).update();
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
  
}
