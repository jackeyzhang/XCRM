package com.xcrm.bonus;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;


@Before(BonusInterceptor.class)
public class BonusController extends AbstractController {
  
  public void list() {
    //price是原价  deal price是成交价  
    String sql = "select wi.index,wia.weight,usr.username,dep.name depname,o.orderno,wia.starttime,wia.finishtime,prd.name prdname";
    String sqlExcept = " from workitem wi "
                      + " join workitemallocation wia on wia.workitem=wi.id "
                      + " join user usr on usr.id=wia.worker "
                      + " join department dep on dep.id=wi.dep "
                      + " join workflow wf on wf.id=wi.workflow "
                      + " join bookitem bi on bi.id=wf.bookitem "
                      + " join product prd on prd.id=bi.product "
                      + " join orderitem oi on oi.bookitem=bi.id "
                      + " join `order`  o on o.id=oi.order " 
                      + " where wi.status=2 " 
                      + " order by o.id,wi.index ";
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );

  }

  public void save() {
    
  }

  public void update() {
    
  }

  public void remove() {
    
  }
  
  private String getSqlForUserRole() {
    User user = User.dao.findById( this.getCurrentUserId() );
    if ( user.isRoot() ) {
      return " bi.user is not null ";
    }
    else {
      return " bi.user=" + this.getCurrentUserId() + " ";
    }
  }

  @Override
  public String getModalName() {
    return "bonus";
  }

  @Override
  public String getPageHeader() {
    return "奖金设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "奖金设置";
  }

  @Override
  public String getIndexHtml() {
    return "bonus.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_BONUS;
  }

  @Override
  protected String searchWord() {
    return "name";
  }
}
