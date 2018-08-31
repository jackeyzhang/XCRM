package com.xcrm.salary;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.model.Salary;
import com.xcrm.common.model.Salaryitem;
import com.xcrm.common.model.Workflowtemplate;
import com.xcrm.common.model.Workitemtemplate;
import com.xcrm.common.util.Constant;


@Before(SalaryInterceptor.class)
public class SalaryController extends AbstractController {
  
  public void list() {
    String searchword = this.getPara( "searchword" );
    String sql = "select prd.name prdname,wft.name wftname,sa.status, GROUP_CONCAT(dep.name order by dep.id) depname,prd.id prdid,wft.id wftid";
    String sqlExcept = "from product prd "
                      + "join workflowtemplate wft on wft.id = prd.workflow "
                      + "join workflowanditemtemplate wfit on wft.id=wfit.workflowtemplate  "
                      + "join workitemtemplate wit on wit.id = wfit.workitemtemplate "
                      + "join department dep on dep.id = wit.dep "
                      + "left join salary sa on sa.product=prd.id and sa.workflowtemplateid=wft.id "
                      + ( searchword == null || searchword.isEmpty()  ? "" :" where prd.name like '%" + searchword + "%'")
                      + "group by prd.id,wft.id "
                      + "order by prd.id " ;
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );
  }

  public void start() {
    int prdid = this.getParaToInt( "prdid" );
    int workflowtemplateid = this.getParaToInt( "wftid" );
    initSalary(prdid,workflowtemplateid);
    this.renderJson( true);
  }
  
  private Salary initSalary(int prdid, int workflowtemplateid){
    Salary salary = new Salary();
    salary.setProduct( prdid );
    salary.setWorkflowtemplateid( workflowtemplateid );
    salary.setStatus( Salary.STATUS_INIT );
    salary.save();
    
    Workflowtemplate wft = Workflowtemplate.dao.findById( workflowtemplateid );
    for( Workitemtemplate wfi : wft.getWorkitemtemplates( ) ){
      Salaryitem salaryitem = new Salaryitem();
      salaryitem.setStatus( Salaryitem.STATUS_INIT );
      salaryitem.setDep( wfi.getDep() );
      salaryitem.setSalaryid( salary.getId() );
      salaryitem.save();
    }
    return salary;
  }

  public void gotoedit() {
    int prdid = this.getParaToInt( "prdid" );
    int workflowtemplateid = this.getParaToInt( "wftid" );
    String start = this.getPara( "start" );
    Salary salary = Salary.dao.getSalary( prdid, workflowtemplateid );
    if( start != null && start.equals( "yes" ) && salary == null){
      salary = initSalary( prdid, workflowtemplateid );
    }
    List<Productpic> pics =
        Productpic.dao.find("select * from productpic where productid=?", prdid);
    setAttr( "role", getCurrentRoleId() );
    setAttr( "title", salary.toString() );
    setAttr( "prdid", prdid);
    setAttr( "wftid", workflowtemplateid);
    setAttr( "salaryitems", salary.getSalaryItems());
    setAttr( "prdimages", pics);
    setAttr( "prdimg_path", getPrdImgBaseUrl() );
    setAttr( "page_header", getPageHeader() );
    render( "editsalary.html" );
  }
  
  public void update(){
    int prdid = this.getParaToInt( "prdid" );
    int workflowtemplateid = this.getParaToInt( "wftid" );
    String departments =this.getParaMap().get( "depids" )[0];
    String amounts = this.getParaMap().get( "amounts" )[0];
    String siids = this.getParaMap().get( "siids" )[0];
    String[] departmentArray = departments.split( "," );
    String[] amount = amounts.split( "," );
    String[] salaryitemIds = siids.split( "," );
    for(int i = 0; i < salaryitemIds.length; i++ ){
      Salaryitem si = Salaryitem.dao.findById( salaryitemIds[i] );
      Double b = new Double( amount[i] );
      si.setAmount(  b );
      si.update();
    }
    this.forwardIndex();
  }


  @Override
  public String getModalName() {
    return "salary";
  }

  @Override
  public String getPageHeader() {
    return "工价设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "工价设置";
  }

  @Override
  public String getIndexHtml() {
    return "salary.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_SALARY;
  }

  @Override
  protected String searchWord() {
    return "name";
  }
}
