package com.xcrm.order;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Order;
import com.xcrm.common.util.Constant;


public class EditOrderController extends AbstractController {
  
  public void index() {
    super.index();
    String orderno = this.getPara( "orderno" );
    List<Record> list = Db.find(
            "select bi.id, bi.num num,bi.price price,bi.product pid,p.name name,bi.comments comments,GROUP_CONCAT(pic.fielname) filename "
            + "from bookitem bi "
            + "left join product p on bi.product=p.id "
            + "left join productpic pic on pic.productid=p.id "
            + "left join orderitem oi on oi.bookitem=bi.id "
            + "left join xcrm.order ord on ord.id=oi.order "
            + "where ord.orderno=? group by bi.id, bi.num,bi.price,bi.product,p.name",
            orderno);
    setAttr("list", list);
    setAttr("dealprice", 123);
    setAttr("paid", 223);
    setAttr("prdimg_path", getPrdImgBaseUrl());
}
  
  public void cancel(){
    String orderno = this.getPara( "orderno" );
    String cancelreason = this.getPara( "cancelreason" );
    Order order = Order.dao.findFirst( "select * from xcrm.order where orderno=?" , orderno );
    order.setStatus( 4 );
    order.setComments( cancelreason );
    order.update();
    this.renderNull();
  }

  @Override
  public String getModalName() {
    return "order";
  }

  @Override
  public String getPageHeader() {
    return "编辑订单";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "添加产品";
  }

  @Override
  public String getIndexHtml() {
    return "editorder.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_ORDER;
  }
  
  
}
