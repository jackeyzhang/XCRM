package com.xcrm.bonus;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.util.Constant;


@Before(BonusInterceptor.class)
public class BonusController extends AbstractController {
  
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
    return "工价奖金设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "工价奖金设置";
  }

  @Override
  public String getIndexHtml() {
    return "bonus.html";
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
