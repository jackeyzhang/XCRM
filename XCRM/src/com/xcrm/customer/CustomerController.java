package com.xcrm.customer;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

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
  

}
