package com.xcrm.salesseason;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Salesseason;
import com.xcrm.common.util.Constant;


public class SalesSeasonController extends AbstractController {

  public void save(){
    Salesseason Salesseason =  this.getModel( Salesseason.class, "" );
    Salesseason.save();
    this.forwardIndex(Salesseason);
  }
  
  public void update(){
    this.getModel( Salesseason.class, "" ).update();
    this.forwardIndex();
  }

  public void remove(){
    Salesseason.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }

  @Override
  public String getModalName() {
    return "salesseason";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改上市年份相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建上市年份";
  }

  @Override
  public String getIndexHtml() {
    return "salesseason.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_USER;
  }
}
