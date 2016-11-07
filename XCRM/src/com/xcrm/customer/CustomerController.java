package com.xcrm.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.xcrm.common.model.Customer;
import com.xcrm.common.util.Constant;

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
  
  @SuppressWarnings("rawtypes")
  public void save(){
    Object user = getSessionAttr(Constant.CUR_USER);
    int userId = (int)((HashMap)user).get( "id" );
    this.getModel( Customer.class, "", true ).set( "createDate", new Date() ).set( "createUser", userId ).save();
    this.forwardAction( "/customer/index" );
  }
  
  @SuppressWarnings("rawtypes")
  public void update(){
    Object user = getSessionAttr(Constant.CUR_USER);
    int userId = (int)((HashMap)user).get( "id" );
    this.getModel( Customer.class, "", true ).set( "updateDate", new Date() ).set( "updateUser", userId ).update();
    this.forwardAction( "/customer/index" );
  }

  public void remove(){
    Customer.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardAction( "/customer/index" );
  }

}
