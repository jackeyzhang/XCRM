package com.xcrm.salary;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;


@Before(SalaryInterceptor.class)
public class SalaryController extends AbstractController {
  
  public void list() {
    
  }

  public void save() {
    
  }

  public void update() {
    
  }

  public void remove() {
    
  }



  @Override
  public String getModalName() {
    return "salary";
  }

  @Override
  public String getPageHeader() {
    return "工价设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "工价设置";
  }

  @Override
  public String getIndexHtml() {
    return "salary.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_SALARY;
  }

  @Override
  protected String searchWord() {
    return "name";
  }
}
