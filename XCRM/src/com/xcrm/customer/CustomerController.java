package com.xcrm.customer;

import java.util.Date;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Customer;
import com.xcrm.common.util.Constant;

/**
 * 
 * @author jzhang12
 *
 */
@Before(CustomerInterceptor.class)
public class CustomerController extends AbstractController {
  
  public void save(){
    Customer customer = this.getModel( Customer.class, "", true ).set( "createDate", new Date() ).set( "createUser", getCurrentUserId() );
    customer.save();
    forwardIndex(customer);
  }
  
  public void update(){
    this.getModel( Customer.class, "", true ).set( "updateDate", new Date() ).set( "updateUser", getCurrentUserId() ).update();
    forwardIndex();
  }

  public void remove(){
    Customer.dao.deleteById( this.getParaToInt( 0 ) );
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "customer";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改客户相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建客户";
  }

  @Override
  public String getIndexHtml() {
    return "customer.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_CUSTOMER;
  }
}
