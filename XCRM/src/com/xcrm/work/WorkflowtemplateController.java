package com.xcrm.work;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Workflowanditemtemplate;
import com.xcrm.common.model.Workflowtemplate;
import com.xcrm.common.model.Workitemtemplate;
import com.xcrm.common.util.Constant;


public class WorkflowtemplateController extends AbstractController {

  public void listactive( ){
    List<Record> records = Workflowtemplate.dao.listAllActive();
    this.renderJson( records );
  }
  
  
  public void save() {
    Workflowtemplate workflowtemplate = this.getModel( Workflowtemplate.class, "" , true);
    workflowtemplate.save();
    String departments =this.getParaMap().get( "departments" )[0];
    String weights = this.getParaMap().get( "weights" )[0];
    String[] departmentArray = departments.split( "," );
    String[] weightArray = weights.split( "," );
    for( int i=0;i < departmentArray.length; i ++){
      Workitemtemplate workitemtemplate = new Workitemtemplate();
      workitemtemplate.setDep( Integer.parseInt( departmentArray[i] ) );
      if(weightArray.length >= departmentArray.length ){
        workitemtemplate.setWeight( Integer.parseInt( weightArray[i] ) );
      }
      workitemtemplate.setIndex( i );
      workitemtemplate.setUserid( getCurrentUserId() );
      workitemtemplate.save();
      Workflowanditemtemplate relation = new Workflowanditemtemplate();
      relation.setWorkflowtemplate( workflowtemplate.getId() );
      relation.setWorkitemtemplate( workitemtemplate.getId() );
      relation.save();
    }
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
  
  public void view( ){
    this.setAttribute();
    String templateid = this.getPara( "id" );
    Workflowtemplate template = Workflowtemplate.dao.findById( templateid );
    if( template == null ){
      this.forwardIndex();
    }else{
      this.setAttr( "worktemplatename", template.getName());
      this.setAttr( "workitemtemplates",  template.getWorkitemtemplateRecords() );
      this.render( "viewworkflowtemplate.html" );
    }
  }
  
  public void inactive(){
    String templateid = this.getPara( "id" );
    Workflowtemplate template = Workflowtemplate.dao.findById( templateid );
    if( template != null ){
      if(template.isUsing()){
        this.setAttr( "error", "不能注销'"+ template.getName()+"'，该模板正在使用中!" );
      }else{
        template.setEnable( 0 );
        template.update();
      }
    }
    this.forwardIndex();
  }
  
  public void active(){
    String templateid = this.getPara( "id" );
    Workflowtemplate template = Workflowtemplate.dao.findById( templateid );
    if( template != null ){
      template.setEnable( 1 );
      template.update();
    }
    this.forwardIndex();
  }
  
  public void add( ){
    this.setAttr( "page_header", "创建新的工单模板");
    setAttr( "role", getCurrentRoleId() );
    this.render( "addworkflowtemplate.html" );
  }
  
  
  public void wxGetAllWorkTemplates(){
    this.renderJson( Workflowtemplate.dao.find( "select * from Workflowtemplate" ) );
  }
}
