package com.xcrm.work;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.User;
import com.xcrm.common.model.Workflow;
import com.xcrm.common.model.Workflowtemplate;
import com.xcrm.common.model.Workitem;
import com.xcrm.common.model.Workitemallocation;
import com.xcrm.common.model.Workitemtemplate;
import com.xcrm.common.qr2.QRCodeUtil;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.DateUtil;
import com.xcrm.common.util.NumUtil;
import com.xcrm.common.util.PropUtil;
import com.xcrm.common.util.RecordUtil;
import com.xcrm.common.util.StrUtil;


public class WorkflowController extends AbstractController {

  public void preSetAttribute() {}

  public void index() {
    //price是原价  deal price是成交价  
    Page<Record> page = getOrderToStartList( getSqlForUserRole() );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    setAttr( "data", pager );
    setAttr( "role", getCurrentRoleId() );
    setAttr( "page_header", "订单工作流管理" );
    setAttr( "prdimg_path", getPrdImgBaseUrl() );
    render( getIndexHtml() );
  }

  public void indexOfProduct() {
    //price是原价  deal price是成交价  
    String sql = "select p.name name, p.id prdid";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order " 
        + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join product p on bi.product=p.id "
        + "where " + getSqlForUserRole() 
        + " and o.status != " + Order.STATUS_CANCELLED 
        + " and o.date >= str_to_date('"+ DateUtil.getBeforeOneMonthDateStr() +"', '%Y-%m-%d')"
        + " " + this.getSearchStatement( true, "" ) 
        + " group by p.name order by p.name desc";
    Page<Record> page = Db.paginate( 1, 30, sql, sqlExcept );
    page.getList().stream().forEach( p -> {
      int prdid = p.getInt( "prdid" );
      Product product = Product.dao.findFirst( "select * from product where id = ?", prdid );
      p.set( "bi", product.getAllBookitems() );
      p.set( "displaystartbtn", product.isStartAllBookitems() ? "no" : "yes" );
    } );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    setAttr( "data", pager );
    setAttr( "role", getCurrentRoleId() );
    setAttr( "page_header", "产品工作流管理" );
    setAttr( "prdimg_path", getPrdImgBaseUrl() );
    render( "prdworkflow.html" );
  }

  public void search() {
    searchWorkflowFromIndexPageAndSetPageAttr( );
    render( getIndexHtml() );
  }
  
  public void indexproductsearch(){
    searchWorkflowFromProductIndexPageAndSetPageAttr( );
    render( "prdworkflow.html" );
  }
  
  private void searchWorkflowFromIndexPageAndSetPageAttr( ){
    String orderno = this.getPara( "orderno" );
    String prdname = this.getPara( "prdname" );
    String customername = this.getPara( "customername" );
    String datepick = this.getPara( "datepick" );
    int month = getMonthsFromWorkflowSearchPage( datepick );
    Date startDate = DateUtil.getFirstDateOfMonth( new Date(), month-1 );
    String startDateStr = StrUtil.formatDate( startDate, "yyyy-MM-dd" );
    //price是原价  deal price是成交价  
    String sql = "select concat(cust.name, '-' , cust.company) company, " + "GROUP_CONCAT(p.name) name," + "o.orderno orderno," + "sum(bi.num) num," + "o.deliverytime date," + "o.status,p.id prdid,"
        + "GROUP_CONCAT(bi.comments) comments";
    String sqlExcept = " from `order` o  " + "left join orderitem oi on o.id=oi.order " + "left join bookitem bi on oi.bookitem=bi.id " + "join product p on bi.product=p.id "
        + ( StrUtil.isEmpty( prdname ) ? "" : "and p.name like '%" + prdname.trim() + "%'" ) + "join customer cust on cust.id=bi.customer "
        + ( StrUtil.isEmpty( customername ) ? "" : "and cust.name like '%" + customername.trim() + "%'" ) + "where 1=1 "
        + ( StrUtil.isEmpty( orderno ) ? "" : "and o.orderno like '%" + orderno.trim() + "%'" ) + " and o.status != " + Order.STATUS_CANCELLED + " "
        + ( month > 0 ? " and o.date >= str_to_date('"+ startDateStr +"', '%Y-%m-%d')" : "")
        + this.getSearchStatement( true, "" ) + " group by o.orderno order by o.orderno,p.name desc";
    Page<Record> page = Db.paginate( 1, 3000, sql, sqlExcept );
    page.getList().stream().forEach( p -> {
      long ordernum = p.getLong( "orderno" );
      Order order = Order.dao.findFirst( "select * from `order` where orderno = ?", ordernum );
      p.set( "bi", order.getAllBookitems() );
      p.set( "displaystartbtn", order.isStartAllBookitems() ? "no" : "yes" );
    } );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    setAttr( "data", pager );
    setAttr( "orderno", orderno );
    setAttr( "prdname", prdname );
    setAttr( "customername", customername );
    setAttr( "datepicker", datepick );
    setAttr( "prdimg_path", getPrdImgBaseUrl() );
    setAttr( "role", getCurrentRoleId() );
  }
  
  private void searchWorkflowFromProductIndexPageAndSetPageAttr( ){
    String orderno = this.getPara( "orderno" );
    String prdname = this.getPara( "prdname" );
    String customername = this.getPara( "customername" );
    String datepick = this.getPara( "datepick" );
    int month = getMonthsFromWorkflowSearchPage( datepick );
    Date startDate = DateUtil.getFirstDateOfMonth( new Date(), month-1 );
    String startDateStr = StrUtil.formatDate( startDate, "yyyy-MM-dd" );
    //price是原价  deal price是成交价  
    String sql = "select p.name name, p.id prdid";
    String sqlExcept = " from `order` o  " 
        + "left join orderitem oi on o.id=oi.order " 
        + "left join bookitem bi on oi.bookitem=bi.id " 
        + "left join product p on bi.product=p.id "
        + ( StrUtil.isEmpty( prdname ) ? "" : "and p.name like '%" + prdname.trim() + "%'" ) + "join customer cust on cust.id=bi.customer "
        + ( StrUtil.isEmpty( customername ) ? "" : "and cust.name like '%" + customername.trim() + "%'" ) 
        + " where " + getSqlForUserRole() 
        + ( StrUtil.isEmpty( orderno ) ? "" : " and o.orderno like '%" + orderno.trim() + "%'" ) + " and o.status != " + Order.STATUS_CANCELLED + " "
        + ( month > 0 ? " and o.date >= str_to_date('"+ startDateStr +"', '%Y-%m-%d')" : "")
        + " " + this.getSearchStatement( true, "" ) 
        + " group by p.name order by p.name desc";
    Page<Record> page = Db.paginate( 1, 3000, sql, sqlExcept );
    page.getList().stream().forEach( p -> {
      int prdid = p.getInt( "prdid" );
      Product product = Product.dao.findFirst( "select * from product where id = ?", prdid );
      p.set( "bi", product.getAllBookitems() );
      p.set( "displaystartbtn", product.isStartAllBookitems() ? "no" : "yes" );
    } );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    setAttr( "data", pager );
    setAttr( "orderno", orderno );
    setAttr( "prdname", prdname );
    setAttr( "customername", customername );
    setAttr( "datepicker", datepick );
    setAttr( "prdimg_path", getPrdImgBaseUrl() );
    setAttr( "role", getCurrentRoleId() );
  }

  public void save() {
    Workflow workflow = this.getModel( Workflow.class, "", true );
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
  
  
  private int getMonthsFromWorkflowSearchPage( String selectindex ){
    if( selectindex.equals( "1" )){
      return 1;
    }else if( selectindex.equals( "2" )){
      return 3;
    }else if( selectindex.equals( "3" )){
      return 6;
    }else if( selectindex.equals( "4" )){
      return 12;
    }
    return -1;
  }

  /**
   * 创建bookitem
   */
  public void startBookItem() {
    wxStartBookItem();
    this.forwardIndex();
  }

  /**
   * 批量创建bookitems
   */
  public void batchStartBookItem() {
    String bookitems = this.getPara( "bookitems" );
    String[] bookitemArray = bookitems.split( ";" );
    for ( String bookitemStr : bookitemArray ) {
      String[] bookitemAndWorktemplate = bookitemStr.split( "," );
      Integer bookitemId = NumUtil.iVal( bookitemAndWorktemplate[0] );
      Integer workflowtemplateId = NumUtil.iVal( bookitemAndWorktemplate[1] );
      Bookitem bookitem = Bookitem.dao.findById( bookitemId );
      //check work flow item and create one if not existing one.
      List<Workflow> found = Workflow.dao.find( "select * from workflow where bookitem = " + bookitemId );
      if ( found.size() > 0 ) {
        return;
      }
      for ( int i = 0; i < bookitem.getNum(); i++ ) {
        Workflow workflow = new Workflow();
        workflow.setBookitem( bookitemId );
        workflow.setIndex( i );
        workflow.setStatus( Workflow.WORK_STATUS_INIT );
        workflow.setProgress( 0 );
        workflow.setWorkflowtemplate( workflowtemplateId );
        workflow.save();
        QRCodeUtil.generator( "" + workflow.getId(), "" + workflow.getId(), getRealPath(), PropUtil.getWorkflowQr2Path() );

        Workflowtemplate wft = Workflowtemplate.dao.findById( workflowtemplateId );
        List<Workitem> wiList = new ArrayList<>();
        for ( Workitemtemplate wit : wft.getWorkitemtemplates() ) {
          Workitem wi = new Workitem();
          wi.setIndex( wit.getIndex() );
          wi.setStatus( wit.getStatus() );
          wi.setWorkflow( workflow.getId() );
          wi.setWeight( wit.getWeight() );
          wi.setDep( wit.getDep() );
          wiList.add( wi );
        }
        Db.batchSave( wiList, wiList.size() );
      }
    }
    this.forwardIndex();
  }

  public void viewWorkflowDetail() {
    int workflowid = this.getParaToInt( "wkid" );
    Workflow workflow = Workflow.dao.findById( workflowid );
    if ( workflow != null ) {
      setAttr( "workflows", workflow.getRelatedWorkflows() );
      List<Workitem> workflowitems = new ArrayList<>();
      for ( Workflow wf : workflow.getRelatedWorkflows() ) {
        workflowitems.addAll( wf.getWorkItems() );
      }
      this.setAttr( "workflowitems", workflowitems );
      setAttr( "prdimages", workflow.getPrdPictures() );
      setAttr( "prdimg_path", getPrdImgBaseUrl() );
    }
    setAttr( "order", workflow.getOrder() );
    setAttr( "orderdeliverytime", StrUtil.formatDate( workflow.getOrder().getDeliverytime(), "yyyy-MM-dd" ) );
    setAttr( "page_header", "订单工作流如下:" );
    setAttr( "workflow_qr2_path", getWorkflowQr2BaseUrl() );
    setAttr( "role", getCurrentRoleId() );
    render( "detailworkflow.html" );
  }

  public void saveorUpdateWorkitemAllocations() {
    Map<String, String[]> form = getParaMap();
    String[] workers = form.get( "selectworker" );
    String[] weights = form.get( "weight" );
    String[] workitemids = form.get( "workitemid" );
    String[] workitemallocationids = form.get( "workitemallocationid" );
    String[] workitemallocationStatus = form.get( "workitemallocationstatus" );

    if ( workitemids == null || workitemids.length == 0 )
      return;

    //delete first
    Integer workitemID = NumUtil.iVal( workitemids[0] );
    Workitem workitem = Workitem.dao.findById( workitemID );
    for ( Workitemallocation wia : workitem.getWorkitemallocations() ) {
      boolean deletefromUI = true;

      for ( String workitemallId : workitemallocationids ) {
        if ( NumUtil.iVal( workitemallId ) == wia.getId() ) {
          deletefromUI = false;
        }
      }
      if ( deletefromUI ) {
        wia.setStatus( Workitemallocation.WORKITEM_STATUS_CANCEL );
        wia.update();
      }
    }

    //update and add then
    for ( int index = 0; index < workitemids.length; index++ ) {
      Integer workitemId = NumUtil.iVal( workitemids[index] );
      Integer worker = NumUtil.iVal( workers[index] );
      Integer weight = NumUtil.iVal( weights[index] );
      Integer status = NumUtil.iVal( workitemallocationStatus == null ? "0" : workitemallocationStatus[index] );
      Integer workitemallocation = 0;
      if ( workitemallocationids != null ) {
        workitemallocation = NumUtil.iVal( workitemallocationids[index] );
      }
      Workitemallocation wAloc = new Workitemallocation();
      if ( workitemallocation > 0 ) {
        wAloc = Workitemallocation.dao.findById( workitemallocation );//if existing, means update
      }
      wAloc.setWorkitem( workitemId );
      wAloc.setWorker( worker );
      wAloc.setWeight( weight );
      wAloc.setStatus( status );
      if ( workitemallocation > 0 ) {
        wAloc.update();
      }
      else {
        wAloc.save();
      }
    }
  }

  public void markcompleteworkitem() {
    int workitemid = this.getParaToInt( "workitemid" );
    WorkflowUtil.autoFinishWorkflowByWorkitem( workitemid );
    this.forwardIndex();
  }

  public void restartworkitem() {
    int workitemid = this.getParaToInt( "workitemid" );
    WorkflowUtil.autoRestartWorkflowByWorkitem( workitemid );
    this.forwardIndex();
  }

  @Deprecated
  public void startWorkflow() {
    int workflowid = this.getParaToInt( "workflowid" );
    Workflow workflow = Workflow.dao.findById( workflowid );
    //upate status
    workflow.setStatus( Workflow.WORK_STATUS_START );
    workflow.update();
  }

  public Page<Record> getOrderToStartList( String sqlForUserRole ) {
    return getOrderToStartList( sqlForUserRole, null, 1 );
  }

  /**
   * 
   * @param sqlForUserRole
   * @param searchOrderNo
   * @return
   */
  private Page<Record> getOrderToStartList( String sqlForUserRole, String searchOrderNo, int isUnstartOrAll ) {
    //price是原价  deal price是成交价  
    String sql = "select concat(cust.name, '-' , cust.company) company, " + "GROUP_CONCAT(p.name) name," + "o.orderno orderno," + "sum(bi.num) num,"
        + "date_format(o.deliverytime,'%Y-%m-%d') date," + "o.status," + "user.username saler," + "GROUP_CONCAT(bi.comments) comments";
    String sqlExcept = " from `order` o  " + "left join orderitem oi on o.id=oi.order " + "left join bookitem bi on oi.bookitem=bi.id " + "left join product p on bi.product=p.id "
        + "left join customer cust on cust.id=bi.customer " + "left join user user on user.id=bi.user " + "where " + sqlForUserRole + " and o.status != " + Order.STATUS_CANCELLED
        + " " + this.getSearchStatement( true, "" ) + ( searchOrderNo == null ? "" : " and o.orderno like '%" + searchOrderNo + "%'" )
        + " and o.date >= str_to_date('"+ DateUtil.getBeforeOneMonthDateStr() +"', '%Y-%m-%d')"
        + " group by o.orderno order by o.orderno,p.name desc";
    Page<Record> page = Db.paginate( 1, 30, sql, sqlExcept );
    page.getList().stream().forEach( o -> {
      long orderno = o.getLong( "orderno" );
      Order order = Order.dao.findFirst( "select * from `order` where orderno = ?", orderno );
      o.set( "bi", order.getAllBookitems() );
      o.set( "displaystartbtn", order.isStartAllBookitems() ? "no" : "yes" );
    } );
    if ( isUnstartOrAll == 0 ) {
      List<Record> list = page.getList().stream().filter( o -> {
        return o.get( "displaystartbtn" ).equals( "yes" );
      } ).collect( Collectors.toList() );
      page = new Page<Record>( list, 1, list.size(), 1, list.size() );
    }
    return page;
  }

  /**
   * following is for wx methods
   */
  public void wxlistallmytasks() {
    int userid = this.getParaToInt( "userid" );
    String searchWord = this.getPara( "searchword" );
    boolean isall = false;
    if( this.getPara( "alltask" ) != null ){
      isall = this.getParaToBoolean( "alltask" );
    }
    List<Workitemallocation> wiaList = new ArrayList<>();
    if ( !StrUtil.isEmpty( searchWord ) ) {
      if( isall ){
        wiaList = Workitemallocation.dao.getAllWorkitemallocationsBySerachWord( userid, searchWord );
      }else{
        wiaList = Workitemallocation.dao.getAllWorkitemallocationsNotFinishBySerachWord( userid, searchWord );
      }
    }
    else {
      if( isall ){
        wiaList = Workitemallocation.dao.getAllWorkitemallocations( userid );
      }else{
        wiaList = Workitemallocation.dao.getAllWorkitemallocationsNotFinish( userid );
      }
    }
    RecordUtil.fillInRowNumber( wiaList );
    this.renderJson( wiaList );
  }

  /**
   * 获取所有完成任务
   */
  public void wxlistallmyfinishtasks() {
    int userid = this.getParaToInt( "userid" );
    String startDate = this.getPara( "date" );
    Calendar nextMonth = Calendar.getInstance();
    boolean isbyyear = false;
    if ( StrUtil.isEmpty( startDate ) ) {//default get current month data
      startDate = Calendar.getInstance().get( Calendar.YEAR ) + "-" + ( Calendar.getInstance().get( Calendar.MONTH ) + 1 );
      nextMonth.add( Calendar.MONTH, 1 );
    }
    else {
      String[] yearandmonth = startDate.split( "-" );
      nextMonth.set( Calendar.YEAR, NumUtil.iVal( yearandmonth[0] ) );
      if ( yearandmonth.length > 1 ) {
        nextMonth.set( Calendar.MONTH, NumUtil.iVal( yearandmonth[1] ) );
      }
      else {
        startDate += "-01";//今年的一月一号
        isbyyear = true;
      }
    }
    String endDate = isbyyear ? nextMonth.get( Calendar.YEAR ) + 1 + "-1" : nextMonth.get( Calendar.YEAR ) + "-" + ( nextMonth.get( Calendar.MONTH ) + 1 );
    List<Workitemallocation> wiaList = Workitemallocation.dao.getAllWorkitemallocationsIsFinish( userid, startDate, endDate );
    RecordUtil.fillInRowNumber( wiaList );
    this.renderJson( wiaList );
  }

  /**
   * 开始一个工作分配项
   */
  public void wxstartwia() {
    int wiaid = this.getParaToInt( "wiaid" );
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    wia.setStatus( Workitemallocation.WORKITEM_STATUS_START );
    wia.setStarttime( new Date() );
    wia.update();
    WorkflowUtil.autoStartWorkflowByWia( wiaid );
    renderJson( wia );
  }

  /**
   * 完成一个工作分配项
   */
  public void wxfinishwia() {
    int wiaid = this.getParaToInt( "wiaid" );
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    wia.setStatus( Workitemallocation.WORKITEM_STATUS_DONE );
    wia.setFinishtime( new Date() );
    wia.update();
    WorkflowUtil.autoFinishWorkflowByWia( wiaid );
    renderJson( wia );
  }

  /**
   * 获取工作流详情
   */
  public void wxgetworkflowdetail() {
    int workflowid = this.getParaToInt( "wfid" );
    int userid = this.getParaToInt( "userid" );
    Workflow workflow = Workflow.dao.findById( workflowid );
    workflow = workflow.getWorkflowDetail( getDepartmentId( userid ) );
    renderJson( workflow );
  }

  /**
   * 员工视图： 开始工作分配项
   */
  public void wxcreateandstartwia() {
    int workflowid = this.getParaToInt( "wfid" );
    int userid = this.getParaToInt( "userid" );
    int depid = this.getParaToInt( "depid" );
    boolean isexsting = Workitemallocation.dao.exisitingWorkitemallocation( workflowid, userid, depid );
    if ( isexsting ) {
      this.renderJson( false );
    }
    else {
      Workflow workflow = Workflow.dao.findById( workflowid );
      Workitem workitem = workflow.getWorkitemByDep( depid );
      Workitemallocation wia = new Workitemallocation();
      wia.setWorker( userid );
      wia.setWorkitem( workitem.getId() );
      wia.setStatus( Workitemallocation.WORKITEM_STATUS_START );
      wia.setStarttime( new Date() );
      wia.setWeight( 100 );//default set to 100
      wia.save();
      WorkflowUtil.autoStartWorkflowByWia( wia.getId() );
      this.renderJson( true );
    }
  }

  /**
   * 获取订单开始数据
   * 
   * 支持按照订单号查询
   */
  public void wxgetOrderToStartList() {
    String searchWord = this.getPara( "wxsearchword" );
    int userselection = 1;// 0 unstart only, 1 all
    if ( !StrUtil.isEmpty( getPara( "userselection" ) ) ) {
      userselection = this.getParaToInt( "userselection" );
    }
    this.renderJson( RecordUtil.fillRowNumber( getOrderToStartList( " bi.user is not null ", searchWord, userselection ).getList() ) );
  }

  /**
   * 开始一个订单项下的所有工作流
   * 
   */
  public void wxStartBookItem() {
    int bookitemid = this.getParaToInt( "bi" );
    int workflowtemplateid = this.getParaToInt( "wftid" );
    //index and num, index 1 num 1 means the second
    int index = this.getParaToInt( "index" );
    int num = this.getParaToInt( "num" );

    //check work flow item and create one if not existing one.
    List<Workflow> found = Workflow.dao.find( "select * from workflow where bookitem = " + bookitemid );
    if ( found.size() > 0 ) {
      this.renderJson( "已启动！" );
      return;
    }
    for ( int i = 0; i < num; i++ ) {
      Workflow workflow = new Workflow();
      workflow.setBookitem( bookitemid );
      workflow.setIndex( index + i );
      workflow.setStatus( Workflow.WORK_STATUS_INIT );
      workflow.setProgress( 0 );
      workflow.setWorkflowtemplate( workflowtemplateid );
      workflow.save();
      QRCodeUtil.generator( "" + workflow.getId(), "" + workflow.getId(), getRealPath(), PropUtil.getWorkflowQr2Path() );

      Workflowtemplate wft = Workflowtemplate.dao.findById( workflowtemplateid );
      List<Workitem> wiList = new ArrayList<>();
      for ( Workitemtemplate wit : wft.getWorkitemtemplates() ) {
        Workitem wi = new Workitem();
        wi.setIndex( wit.getIndex() );
        wi.setStatus( Workitem.WORKITEM_STATUS_INIT );
        wi.setWorkflow( workflow.getId() );
        wi.setWeight( wit.getWeight() );
        wi.setDep( wit.getDep() );
        wiList.add( wi );
      }
      Db.batchSave( wiList, wiList.size() );
    }
    this.renderJson( true );
  }

  /**
   * 微信端启动整个order下所有未启动订单项
   */
  public void wxStartOrder() {
    String bookitemsStr = this.getPara( "bis" );
    String bookitemwfsStr = this.getPara( "wfts" );
    List<String> bookitems = StrUtil.split( bookitemsStr, "," );
    List<String> bookitemwfs = StrUtil.split( bookitemwfsStr, "," );
    for ( int index = 0; index < bookitems.size(); index++ ) {
      String bookitemid = bookitems.get( index );
      Workflow found = Workflow.dao.findFirst( "select * from workflow where bookitem = " + bookitemid );
      if ( found != null ) {
        //已启动
        continue;
      }
      int workflowtemplateid = NumUtil.iVal( bookitemwfs.get( index ) );
      Bookitem bi = Bookitem.dao.findById( bookitemid );
      for ( int i = 0; i < bi.getNum(); i++ ) {
        Workflow workflow = new Workflow();
        workflow.setBookitem( NumUtil.iVal( bookitemid ) );
        workflow.setIndex( index + i );
        workflow.setStatus( Workflow.WORK_STATUS_INIT );
        workflow.setProgress( 0 );
        workflow.setWorkflowtemplate( workflowtemplateid );
        workflow.save();
        QRCodeUtil.generator( "" + workflow.getId(), "" + workflow.getId(), getRealPath(), PropUtil.getWorkflowQr2Path() );
        Workflowtemplate wft = Workflowtemplate.dao.findById( workflowtemplateid );
        List<Workitem> wiList = new ArrayList<>();
        for ( Workitemtemplate wit : wft.getWorkitemtemplates() ) {
          Workitem wi = new Workitem();
          wi.setIndex( wit.getIndex() );
          wi.setStatus( Workitem.WORKITEM_STATUS_INIT );
          wi.setWorkflow( workflow.getId() );
          wi.setWeight( wit.getWeight() );
          wi.setDep( wit.getDep() );
          wiList.add( wi );
        }
        Db.batchSave( wiList, wiList.size() );
      }
    }
    this.renderJson( true );
  }

  /**
   * 获取工作流详情
   * 同时获取同订单下其他工作流
   * 
   */
  public void wxGetWorkflowDetail() {
    int workflowid = this.getParaToInt( "wfid" );
    int userid = this.getParaToInt( "userid" );
    User user = User.dao.findById( userid );
    Workflow workflow = Workflow.dao.findById( workflowid );
    List<Workflow> workflows = workflow.getRelatedWorkflows();
    workflows.stream().forEach( wf -> {
      wf.put( "workitems", user.isDepManager() ? wf.getWorkItems( userid ) : wf.getWorkItems( ) ); //dep or root
    } );
    this.renderJson( workflows );
  }

  public void wxRemoveWia() {
    int wiaid = this.getParaToInt( "wiaid" );
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    if ( wia != null ) {
      wia.setStatus( Workitemallocation.WORKITEM_STATUS_CANCEL );
      this.renderJson( wia.update() );
    }
    else {
      this.renderJson( false );
    }
  }

  /**
   * 员工视图： 开始工作分配项
   */
  public void wxcreatewia() {
    int workflowid = this.getParaToInt( "wfid" );
    int userid = this.getParaToInt( "userid" );
    int depid = this.getParaToInt( "depid" );
    int weight = this.getParaToInt( "weight" );
    boolean isexsting = Workitemallocation.dao.exisitingWorkitemallocation( workflowid, userid, depid );
    if ( isexsting ) {
      this.renderJson( false );
    }
    else {
      Workflow workflow = Workflow.dao.findById( workflowid );
      Workitem workitem = workflow.getWorkitemByDep( depid );
      Workitemallocation wia = new Workitemallocation();
      wia.setWorker( userid );
      wia.setWorkitem( workitem.getId() );
      wia.setStatus( Workitemallocation.WORKITEM_STATUS_INIT );
      wia.setWeight( weight );
      wia.setStarttime( new Date() );
      wia.save();
      this.renderJson( true );
    }
  }

  /**
   * 开始一个工作分配项
   */
  public void wxupdatewia() {
    int wiaid = this.getParaToInt( "wiaid" );
    int weight = this.getParaToInt( "weight" );
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    if ( wia != null ) {
      wia.setWeight( weight );
      this.renderJson( wia.update() );
    }
    this.renderJson( false );
  }
  
  public void wxlistallworkflows( ){
    String searchWord = this.getPara( "searchword" );
    List<Workflow> workflows = Workflow.listAllWorkflowDetails( searchWord );
    workflows.stream().forEach( wf -> {
      wf.put( "workitems", wf.getWorkItems() );
    } );
    this.renderJson( workflows );
  }
 
}
