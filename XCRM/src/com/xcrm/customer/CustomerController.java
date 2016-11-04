package com.xcrm.customer;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.Customer;

/**
 * 
 * @author jzhang12
 *
 */
@Before(CustomerInterceptor.class)
public class CustomerController extends Controller {

  public void index() {
    setAttr("model", "customer");
    setAttr("page_header", "创建或修改客户相关信息");
    setAttr("toolbar_create", "创建客户");
    render( "customer.html" );
  }
  
  public void list() {
    List<Customer> Customers = Customer.dao.find( "select * from customer" );
    this.renderJson( Customers );
  }
  
  public void save(){
    this.getModel( Customer.class, "", true ).set( "createDate", new Date() ).save();
    this.forwardAction( "/customer/index" );
  }
  
  public void update(){
    this.getModel( Customer.class, "", true ).update();
    this.forwardAction( "/customer/index" );
  }

  public void remove(){
    Customer.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardAction( "/customer/index" );
  }

}
