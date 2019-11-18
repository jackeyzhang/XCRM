package com.xcrm.customer;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
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
    int customerId =  this.getParaToInt( 0 );
    List<Record> bookitems = Db.find( "select * from Bookitem where customer=" + customerId );
    if( bookitems == null || bookitems.isEmpty() ){
      Customer.dao.deleteById( customerId );    
      forwardIndex();
    }else{
      failed( "该客户信息不允许被删除!" );
    }
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

  @Override
  protected String searchWord() {
    return "company";
  }
  
  public void wxlistallcustomers( ){
    List<Record> records = Db.find( "select name,id,company  from customer order by id desc limit 20" );
    this.renderJson( records );
  }
  
  public void wxsearchcustomer(){
    String name = this.getPara( "name" );
    List<Record> records = Db.find( "select name,id,company  from customer where name like '%" + name + "%' or company like '%" + name + "%' order by id desc"  );
    this.renderJson( records );
  }
  
  public void wxaddcustomer(){
    String customername = this.getPara( "customername" );
    String companyname = this.getPara( "companyname");
    String mailaddress = this.getPara( "mailaddress");
    String phone = this.getPara( "phone");
    Customer customer = new Customer();
    customer.set( "name", customername );
    customer.set( "company", companyname );
    customer.set( "shiptoAddr", mailaddress );
    customer.set( "custAddr", mailaddress );
    customer.set( "phone", phone );
    customer.save();
    this.renderJson( customer );
  }
  
  public void wxgetcustomer(){
    String id = this.getPara( "id" );
    Record record = Db.findFirst( "select name,id,company,shiptoaddr,phone  from customer where id=" + id);
    this.renderJson( record );
  }
  
  public void wxdeletecustomer(){
    String id = this.getPara( "id" );
    List<Record> bookitems = Db.find( "select * from Bookitem where customer=" + id );
    if( bookitems == null || bookitems.isEmpty()){
      boolean deleted = Db.deleteById( "customer", id );
      this.renderJson( deleted );
    }else{
      this.renderJson( false );//customer cannot be delete if puchased some bookitem
    }
  }
  
  public void wxupdatecustomer( ){
    String id = this.getPara( "id" );
    String customername = this.getPara( "customername" );
    String companyname = this.getPara( "companyname");
    String mailaddress = this.getPara( "mailaddress");
    String phone = this.getPara( "phone");
    Customer customer = Customer.dao.findById( id );
    customer.set( "name", customername );
    customer.set( "company", companyname );
    customer.set( "shiptoAddr", mailaddress );
    customer.set( "custAddr", mailaddress );
    customer.set( "phone", phone );
    customer.update();
    this.renderJson( true );
  }
}
