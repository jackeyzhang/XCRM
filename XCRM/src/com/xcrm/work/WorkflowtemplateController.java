package com.xcrm.work;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Workflowtemplate;
import com.xcrm.common.util.Constant;


public class WorkflowtemplateController extends AbstractController {

  public void save() {
    Workflowtemplate worktemplate = this.getModel( Workflowtemplate.class, "" , true);
    worktemplate.save();
    forwardIndex(worktemplate);
  }

  public void update() {
    this.getModel( Workflowtemplate.class, "" , true).update();
    forwardIndex();
  }

  public void remove() {
    Workflowtemplate.dao.deleteById( this.getParaToInt( 0 ) );
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "workflowtemplate";
  }

  @Override
  public String getPageHeader() {
    return "工单模板管理";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建工单模板";
  }

  @Override
  public String getIndexHtml() {
    return "workflowtemplate.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_WORK;
  }

  @Override
  protected String searchWord() {
    return "name";
  }
  
  public void edit( ){
    String templateid = this.getPara( "id" );
    Workflowtemplate template = Workflowtemplate.dao.findById( templateid );
    if( template == null ){
      this.forwardIndex();
    }else{
      this.setAttr( "page_header", template.getName());
      this.render( "addworkflowtemplate.html" );
    }
  }
  
  public void add( ){
    this.setAttr( "page_header", "创建新的工单模板");
    this.render( "addworkflowtemplate.html" );
  }
  
}
