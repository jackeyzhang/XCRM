package com.xcrm.customer;

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
    render( "customer.html" );
  }
  
  public void listcustomers() {
    List<Customer> Customers = Customer.dao.find( "select * from customer" );
    this.renderJson( Customers );
  }
  
  public void save(){
    this.getModel( Customer.class, "" ).save();
    this.forwardAction( "/customer/index" );
  }
  
  public void update(){
    this.getModel( Customer.class, "" ).update();
    this.forwardAction( "/customer/index" );
  }

  public void remove(){
    Customer.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardAction( "/customer/index" );
  }

}
