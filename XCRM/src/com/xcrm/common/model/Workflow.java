package com.xcrm.common.model;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.model.base.BaseWorkflow;
import com.xcrm.common.util.StrUtil;


/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Workflow extends BaseWorkflow<Workflow> {

  public static final Workflow dao = new Workflow();

  public static final int WORK_STATUS_INIT = 0; //待处理 
  public static final int WORK_STATUS_START = 1; //进行中
  public static final int WORK_STATUS_DONE = 2; //已完成
  public static final int WORK_STATUS_CANCEL = 3; //取消

  /**
   * 获取同一批(同一个bookitem)Workflow
   * 
   * @return
   */
  public List<Workflow> getRelatedWorkflows() {
    int bookitemid = this.getBookitem();
    return dao.find( "select wf.*,wft.name name,prd.name prdname,prd.id prdid,bi.num binum,bi.comments,bi.prdattrs,ord.orderno " + "from workflow wf "
        + "join workflowtemplate wft on wft.id=wf.workflowtemplate " + "join bookitem bi on bi.id=wf.bookitem " + "join orderitem oi on oi.bookitem=bi.id "
        + "join `order` ord on ord.id=oi.order " + "join product prd on prd.id=bi.product " + "where wf.bookitem=?", bookitemid );
  }

  public Bookitem getBookItem() {
    return Bookitem.dao.findById( getBookitem() );
  }

  public List<Productpic> getPrdPictures() {
    Bookitem bookitem = getBookItem();
    Product product = Product.dao.findById( bookitem.getProduct() );
    if ( product != null ) {
      return product.getPictures();
    }
    return new ArrayList<>();
  }

  public Order getOrder() {
    Bookitem bookitem = Bookitem.dao.findById( getBookitem() );
    return bookitem.getOrder();
  }

  public List<Workitem> getWorkItems() {
    List<Workitem> items = Workitem.dao.find( "select wi.*,dp.name dp, (case when wi.status=0 then 'lightblue' when wi.status=1 then 'blue' when wi.status=2 then 'green' else 'red' end) statuscolor from workitem wi join department dp on dp.id=wi.dep  where wi.workflow=?", this.getId() );
    items.stream().forEach( i -> i.put( "workitemallocations", i.getWorkitemallocations() ) );
    return items;
  }
  
  public List<Workitem> getWorkItems( int userid ) {
    List<Workitem> items = Workitem.dao.find( "select wi.*,dp.name dp, (case when wi.status=0 then 'lightblue' when wi.status=1 then 'blue' when wi.status=2 then 'green' else 'red' end) statuscolor from workitem wi join department dp on dp.id=wi.dep join user usr on dp.id=usr.department where wi.workflow=? and usr.id=?", this.getId(), userid );
    items.stream().forEach( i -> i.put( "workitemallocations", i.getWorkitemallocations() ) );
    return items;
  }

  public Workitem getWorkitemByDep( int depid ) {
    Workitem item = Workitem.dao.findFirst( "select wi.*,dp.name dp from workitem wi join department dp on dp.id=wi.dep  where wi.workflow=? and wi.dep=?", this.getId(), depid );
    return item;
  }

  public List<Record> getDeps() {
    List<Record> items = Db.find( "select dp.name,dp.id from workitem wi join department dp on dp.id=wi.dep  where wi.workflow=" + this.getId() + " order by dp.id " );
    return items;
  }

  public Record getPrdInfo() {
    return Db.findFirst( "select prd.name,bi.comments,bi.prdattrs from bookitem bi join product prd on prd.id=bi.product where bi.id=" + getBookitem() + " limit 1 " );
  }

  /**
   * contains order, prdpictures, prdname, departments
   * @return
   */
  public Workflow getWorkflowDetail( int defaultdep ) {
    this.put( "order", getOrder() );
    this.put( "prdpictures", getPrdPictures() );
    this.put( "prd", getPrdInfo() );
    this.put( "deps", getDeps() );
    int i = 0;
    for ( Record record : getDeps() ) {
      if ( record.getInt( "id" ) == defaultdep ) {
        this.put( "selectdepidx", i );
        break;
      }
      i++;
    }
    return this;
  }
  
  public Workflow getWorkflowWithPrdAndOrder( ){
    this.put( "order", getOrder() );
    this.put( "prdpictures", getPrdPictures() );
    this.put( "prd", getPrdInfo() );
    this.put( "deps", getDeps() );
    return this;
  }

  /**
   * 获取all workflow及其详情
   * 
   * @param status
   * @return
   */
  public static List<Workflow> listAllWorkflowDetails( String searchword ) {
    return Workflow.dao.find( "select distinct wf.id,wf.status,wftmp.name,bi.prdattrs,bi.comments,wf.index,prd.name prdname,ord.orderno orderno"
        + " from workflow wf "
        + " join bookitem bi on bi.id = wf.bookitem " 
        + " join orderitem oi on oi.bookitem = bi.id "
        + " join `order` ord on oi.order= ord.id "
        + " join workflowtemplate wftmp on wftmp.id = wf.workflowtemplate " 
        + " join product prd on prd.id= bi.product "
        + " where 1=1 " 
        + ( StrUtil.isEmpty( searchword )? "" : " and ( prd.name like '%" + searchword + "%' " )
        + ( StrUtil.isEmpty( searchword )? "" : " or ord.orderno like '%" + searchword + "%') " )
        + " order by bi.id,wf.index "
        + " limit 30" );
  }
}
