package com.xcrm.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Department;
import com.xcrm.common.model.User;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.StrUtil;


@Before(UserInterceptor.class)
public class UserController extends AbstractController {
  
  public void list() {
    //price是原价  deal price是成交价  
    String sql = "select usr.* ";
    String sqlExcept = " from user usr  " 
        + " left join department dep on dep.id=usr.department "
        + " where 1=1 " +  getSearchStatement( true, "" );
    int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
    int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
    Page<Record> page = Db.paginate( pagenumber, pagesize, sql, sqlExcept );
    Pager pager = new Pager( page.getTotalRow(), page.getList() );
    this.renderJson( pager );

  }

  public void save() {
    User user = this.getModel( User.class, "", true );
    user.save();
    forwardIndex( user );
  }

  public void update() {
    this.getModel( User.class, "", true ).update();
    forwardIndex();
  }

  public void remove() {
    User.dao.deleteById( this.getParaToInt( 0 ) );
    forwardIndex();
  }

  public void logoff() {
    User user = User.dao.findById( this.getPara( 0 ) );
    if ( user == null )
      return;
    user.set( "isenable", false ).update();
    forwardIndex();
  }

  public void active() {
    User user = User.dao.findById( this.getPara( 0 ) );
    if ( user == null )
      return;
    user.set( "isenable", true ).update();
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "user";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改用户相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建用户";
  }

  @Override
  public String getIndexHtml() {
    return "user.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_USER;
  }

  @Override
  protected String searchWord() {
    return "usr.username,dep.name";
  }

  public void listWorkers() {
    wxlistWorkers();
  }

  public void wxgetuserinfo() {
    int userid = this.getParaToInt( "userid" );
    User user = User.dao.findFirst( "select usr.*,dep.name depname from user usr join department dep on dep.id= usr.department where usr.id=" + userid );
    this.renderJson( user );
  }

  public void wxlistWorkers() {
    String searchword = this.getPara( "searchword" );
    String orderbydepid = this.getPara( "orderbydepid" );
    String sortString = "";
    if( !StrUtil.isEmpty( orderbydepid )){
      int orderbyDep = Integer.parseInt( orderbydepid );
      for( Integer depid : Department.getAllDepartmentIDs()){
        if(depid != orderbyDep)
        sortString += ( depid + "," );
      }
      sortString = orderbyDep + "," +sortString;
      sortString = sortString.substring( 0, sortString.length()-1 );
    }
    List<User> allworkers = User.dao.find( "select usr.*,dep.name depname from user usr join department dep on dep.id=usr.department" 
    + (StrUtil.isEmpty( searchword ) ? "" : " where dep.name like '%" + searchword + "%' or usr.username like '%" + searchword + "%' ")
    + (StrUtil.isEmpty( orderbydepid ) ? "" : " order by field(dep.id," + sortString + " ) "));
    this.renderJson( allworkers );
  }

}
