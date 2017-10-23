package com.xcrm.work;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.User;
import com.xcrm.common.model.Workflow;
import com.xcrm.common.util.Constant;


public class WorkflowController extends AbstractController {
  
  public void list() {
    //price是原价  deal price是成交价  
    String sql = "select ur.username username, concat(cust.name, '-' , cust.company) company, "
        + "GROUP_CONCAT(p.name) name,"
        + "o.orderno orderno,"
        + "round(sum(bi.price*bi.num),2) price,"
        + "round(o.price,2) dealprice,"
        + "sum(bi.num) num,"
        + "oi.date date,"
        + "contract.name contractname,"
        + "contract.id contractid,"
        + "(select round(sum(paid),2) from payment where orderno= o.orderno) paid,"
        + "(select round(o.price-ifnull(sum(paid),0), 2) from payment where orderno= o.orderno) due,"
        + "o.status";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order "
        + "left join bookitem bi on oi.bookitem=bi.id "  
        + "left join product p on bi.product=p.id "
        + "left join contract contract on bi.contract=contract.id " 
        + "left join customer cust on cust.id=bi.customer left join user ur on bi.user=ur.id " 
        + "where " + getSqlForUserRole()
        + " and o.status != " + Order.STATUS_CANCELLED + " "
        + this.getSearchStatement( true, "" ) + " group by o.orderno order by o.orderno desc";
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );
  }

  public void save() {
    Workflow workflow = this.getModel( Workflow.class, "" , true);
    workflow.save();
    forwardIndex();
  }


  public void remove() {
    Workflow.dao.deleteById( this.getParaToInt( 0 ) );
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "workflow";
  }

  @Override
  public String getPageHeader() {
    return "工作流管理";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "工作流管理";
  }

  @Override
  public String getIndexHtml() {
    return "workflow.html";
  }
  
  @Override
  public int getCategory() {
    return Constant.CATEGORY_WORKFLOW;
  }

  @Override
  protected String searchWord() {
    return "name";
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
  
}
