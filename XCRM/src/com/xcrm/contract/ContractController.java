package com.xcrm.contract;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;


public class ContractController extends AbstractController {

  public void save() {
    forwardIndex();
  }

  public void update() {
    forwardIndex();
  }
  
  public void preadd() {
	  render("/contract/add.html");
   }

  public void remove() {
    forwardIndex();
  }


  @Override
  public String getModalName() {
    return "contract";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改合同相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建合同";
  }

  @Override
  public String getIndexHtml() {
    return "contract.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_USER;
  }
}
