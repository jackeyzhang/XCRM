package com.xcrm.bonus;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.User;
import com.xcrm.common.model.Workitemallocation;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.NumUtil;
import com.xcrm.common.util.StrUtil;


@Before(BonusInterceptor.class)
public class BonusController extends AbstractController {
  
  public void list() {
    //price是原价  deal price是成交价  
    String sql = "select wf.id,o.orderno,prd.name prdname,(wf.index+1) wfindex,wf.status,"
        + "group_concat('' + wia.id, '-', dep.name,'-',usr.username,'-', ''+wia.bonus order by dep.name) worker";
    String sqlExcept = " from workflow wf "
         +" JOIN bookitem bi ON bi.id = wf.bookitem "
         +" JOIN product prd ON prd.id = bi.product "
         +" JOIN orderitem oi ON oi.bookitem = bi.id "
         +" JOIN `order` o ON o.id = oi.order "
         +" left JOIN workitem wi ON wf.id = wi.workflow "
         +" left JOIN workitemallocation wia ON wia.workitem = wi.id "
         +" left JOIN user usr ON usr.id = wia.worker "
         +" left JOIN department dep ON dep.id = wi.dep "
         +" group by wf.id "
         +" order by o.orderno,wf.index";
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );

  }

  public void save() {
    String id = this.getAttr( "id" );
    System.out.println(id);
  }

  public void update() {
    String postData=HttpKit.readData(this.getRequest());
    List<String> values = StrUtil.split( postData, "&" );
    if(values.isEmpty() || values.size() < 2) return;
    List<String> idList = StrUtil.split( values.get( 0 ), "=" );
    int wiaid = NumUtil.iVal( idList.get( 1 ) );
    List<String> bonusList = StrUtil.split( values.get( 1 ), "=" );
    double bonus = NumUtil.dVal(  bonusList.get( 1 )  );
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    if(wia == null) return;
    wia.setBonus( new BigDecimal( bonus ) );
    wia.update();
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
