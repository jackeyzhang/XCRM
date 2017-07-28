package com.xcrm.order;

import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Order;
import com.xcrm.common.util.Constant;


public class CancelOrderController extends AbstractController {
  
  public void cancel(){
    String orderno = this.getPara( "orderno" );
    String cancelreason = this.getPara( "cancelreason" );
    Order order = Order.dao.findFirst( "select * from `order` where orderno=?" , orderno );
    order.setStatus( Order.STATUS_CANCELLED );
    order.setComments( cancelreason );
    order.update();
    this.renderNull();
  }
  
  public void wxcancelorder(){
    cancel();
  }

  @Override
  public String getModalName() {
    return "order";
  }

  @Override
  public String getPageHeader() {
    return "取消订单";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "我的订单";
  }

  @Override
  public String getIndexHtml() {
    return "cancelorder.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_ORDER;
  }
  
  
}
