package com.xcrm.department;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Department;
import com.xcrm.common.util.Constant;


public class DepartmentController extends AbstractController {

  public void save(){
    Department department =  this.getModel( Department.class, "" );
    department.save();
    this.forwardIndex(department);
  }
  
  public void update(){
    this.getModel( Department.class, "" ).update();
    this.forwardIndex();
  }

  public void remove(){
    Department.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }

  @Override
  public String getModalName() {
    return "department";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改部门信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建部门";
  }

  @Override
  public String getIndexHtml() {
    return "department.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_DEPARTMENT;
  }
}
