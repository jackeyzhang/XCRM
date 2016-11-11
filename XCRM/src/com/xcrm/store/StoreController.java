package com.xcrm.store;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Store;
import com.xcrm.common.util.Constant;


public class StoreController extends AbstractController {

  public void save(){
    Store store =  this.getModel( Store.class, "" );
    store.save();
    this.forwardIndex(store);
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
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_STORE;
  }
}
