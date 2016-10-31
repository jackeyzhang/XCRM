package com.xcrm.customer;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.Customer;
import com.xcrm.common.model.User;

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
    User user = (User)getSession().getAttribute( "currentUser" );
    setAttr("login_user", "欢迎你, " + user.get( "username" ));
    render( "customer.html" );
  }
  
  public void list() {
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
