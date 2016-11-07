package com.xcrm.customer;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Customer;

/**
 * 
 * @author jzhang12
 *
 */
@Before(CustomerInterceptor.class)
public class CustomerController extends AbstractController {
  
  public void list() {
    List<Customer> Customers = Customer.dao.find( "select * from customer" );
    this.renderJson( Customers );
  }
  
  public void save(){
    this.getModel( Customer.class, "", true ).set( "createDate", new Date() ).set( "createUser", getCurrentUserId() ).save();
    forwardIndex();
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

}
