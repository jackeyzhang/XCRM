package com.xcrm.work;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.User;
import com.xcrm.common.model.Workflow;
import com.xcrm.common.model.Workflowtemplate;
import com.xcrm.common.model.Workitem;
import com.xcrm.common.model.Workitemtemplate;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.StrUtil;


public class WorkflowController extends AbstractController {
  
  public void preSetAttribute(){
  }
  
  public void index() {
    //price是原价  deal price是成交价  
    String sql = "select concat(cust.name, '-' , cust.company) company, "
        + "GROUP_CONCAT(p.name) name,"
        + "o.orderno orderno,"
        + "sum(bi.num) num,"
        + "date_format(o.deliverytime,'%Y-%m-%d') date,"
        + "o.status,"
        + "user.username saler,"
        + "GROUP_CONCAT(bi.comments) comments";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order "
        + "left join bookitem bi on oi.bookitem=bi.id "  
        + "left join product p on bi.product=p.id "
        + "left join customer cust on cust.id=bi.customer "
        + "left join user user on user.id=bi.user " 
        + "where " + getSqlForUserRole()
        + " and o.status != " + Order.STATUS_CANCELLED + " "
        + this.getSearchStatement( true, "" ) 
        + " group by o.orderno order by o.orderno,p.name desc" ;
    Page<Record> page = Db.paginate( 1, 30, sql, sqlExcept );
    page.getList().stream().forEach(
        p -> {
          long orderno = p.getLong( "orderno" );
          Order order = Order.dao.findFirst( "select * from `order` where orderno = ?", orderno );
          p.set( "bi", order.getAllBookitems() );
        }
        );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    setAttr( "data", pager );
    setAttr( "page_header", "订单工作流管理" );
    setAttr( "prdimg_path", getPrdImgBaseUrl());
    render( getIndexHtml() );
  }
  
  public void indexOfProduct(){
  //price是原价  deal price是成交价  
    String sql = "select p.name name, p.id prdid";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order "
        + "left join bookitem bi on oi.bookitem=bi.id "  
        + "left join product p on bi.product=p.id "
        + "where " + getSqlForUserRole()
        + " and o.status != " + Order.STATUS_CANCELLED + " "
        + this.getSearchStatement( true, "" ) 
        + " group by p.name order by p.name desc" ;
    Page<Record> page = Db.paginate( 1, 30, sql, sqlExcept );
    page.getList().stream().forEach(
        p -> {
          int prdid = p.getInt( "prdid" );
          Product product = Product.dao.findFirst( "select * from product where id = ?", prdid );
          p.set( "bi", product.getAllBookitems() );
        }
        );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.setAttr( "data", pager );
    this.setAttr( "page_header", "产品工作流管理" );
    render("prdworkflow.html");
  }
  
  public void search() {
    String orderno = this.getPara( "orderno" );
    String prdname = this.getPara( "prdname" );
    String customername = this.getPara( "customername" );
    String datepick = this.getPara( "datepick" );
    //price是原价  deal price是成交价  
    String sql = "select concat(cust.name, '-' , cust.company) company, "
        + "GROUP_CONCAT(p.name) name,"
        + "o.orderno orderno,"
        + "sum(bi.num) num,"
        + "oi.date date,"
        + "o.status,"
        + "GROUP_CONCAT(bi.comments) comments";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order "   
        + "left join bookitem bi on oi.bookitem=bi.id "  
        + "join product p on bi.product=p.id " + ( StrUtil.isEmpty( prdname )? "" : "and p.name like '%" + prdname.trim() + "%'")
        + "join customer cust on cust.id=bi.customer "  + ( StrUtil.isEmpty( customername )? "" : "and cust.name like '%" + customername.trim() + "%'")
        + "where 1=1 " 
        + ( StrUtil.isEmpty( orderno )? "" : "and o.orderno like '%" + orderno.trim() + "%'")
        + " and o.status != " + Order.STATUS_CANCELLED + " "
        + this.getSearchStatement( true, "" ) 
        + " group by o.orderno order by o.orderno,p.name desc" ;
    Page<Record> page = Db.paginate( 1, 30, sql, sqlExcept );
    page.getList().stream().forEach(
        p -> {
          long ordernum = p.getLong( "orderno" );
          Order order = Order.dao.findFirst( "select * from `order` where orderno = ?", ordernum );
          p.set( "bi", order.getAllBookitems() );
        }
        );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.setAttr( "data", pager );
    this.setAttr( "orderno", orderno );
    this.setAttr( "prdname", prdname );
    this.setAttr( "customername",customername );
    this.setAttr( "datepick", datepick );
    render( getIndexHtml() );
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
  
  public void startBookItem(){
    int bookitemid = this.getParaToInt( "bi" );
    int workflowtemplateid = this.getParaToInt( "wftid" );
    
    //index and num, index 1 num 1 means the sencond
    int  index = this.getParaToInt( "index" );
    int  num = this.getParaToInt( "num" );
    
    //check work flow item and create one if not existing one.
    List<Workflow> found = Workflow.dao.find( "select * from workflow where bookitem = " + bookitemid);
    if( found.size() > 0){
      return;
    }
    for( int i=0;i< num; i++ ){
      Workflow workflow = new Workflow();
      workflow.setBookitem( bookitemid );
      workflow.setIndex( index + i );
      workflow.setStatus( Workflow.WORK_STATUS_INIT );
      workflow.setProgress( 0 );
      workflow.setWorkflowtemplate( workflowtemplateid );
      workflow.save();
      
      Workflowtemplate wft = Workflowtemplate.dao.findById( workflowtemplateid );
      List<Workitem> wiList = new ArrayList<>();
      for( Workitemtemplate wit : wft.getWorkitemtemplates() ){
        Workitem wi = new Workitem();
        wi.setIndex( wit.getIndex() );
        wi.setStatus( wit.getStatus() );
        wi.setWorkflow( workflow.getId() );
        wi.setWeight( wit.getWeight() );
        wi.setDep( wit.getDep() );
        wiList.add( wi );
      } 
      Db.batchSave( wiList, wiList.size() );
      this.forwardIndex();
    }
  }
  
  public void viewWorkflowDetail(){
    int workflowid = this.getParaToInt( "wkid" );
    Workflow workflow = Workflow.dao.findById( workflowid );
    if( workflow != null ){
      this.setAttr( "workflows", workflow.getRelatedWorkflows() );
    }
    this.setAttr("order", workflow.getOrder());
    this.setAttr("orderdeliverytime", StrUtil.formatDate( workflow.getOrder().getDeliverytime(), "yyyy-MM-dd" ));
    render("detailworkflow.html");
  }
  
}
