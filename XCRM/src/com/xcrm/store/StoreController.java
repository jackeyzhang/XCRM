package com.xcrm.store;

import java.util.List;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Store;


public class StoreController extends AbstractController {

  public void list() {
    List<Store> Stores = Store.dao.find( "select * from Store" );
    this.renderJson( Stores );
  }
  
  public void save(){
    this.getModel( Store.class, "" ).save();
    this.forwardIndex();
  }
  
  public void update(){
    this.getModel( Store.class, "" ).update();
    this.forwardIndex();
  }

  public void remove(){
    Store.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }

  @Override
  public String getModalName() {
    return "store";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改门店相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建门店";
  }

  @Override
  public String getIndexHtml() {
    return "store.html";
  }
}
