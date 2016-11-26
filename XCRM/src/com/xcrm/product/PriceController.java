package com.xcrm.product;

import java.util.Date;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Price;
import com.xcrm.common.util.Constant;


@Before(ProductInterceptor.class)
public class PriceController extends AbstractController {

  public void index() {
    super.index();
  }

  public void save() {
    Price price = this.getModel( Price.class, "", true ).set( "store", this.getCurrentStoreId() ).set( "createuser", this.getCurrentUserId() ).set( "createtime", new Date() );
    price.save();
    forwardIndex( price );
  }
  
  public void update(){
    this.getModel( Price.class, "" ).update();
    this.forwardIndex();
  }
  
  public void remove(){
    Price.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }

  @Override
  public String getModalName() {
    return "price";
  }

  @Override
  public String getPageHeader() {
    return "价格设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "添加价格";
  }

  @Override
  public String getIndexHtml() {
    return "price.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_PRICE;
  }
}
