/**
 * 
 */
package com.xcrm.common.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.xcrm.common.AttributeFinder;


/**
 * @author jzhang12
 *
 */
public class User extends Model<User> {

  private static final long serialVersionUID = 1946424759277934855L;

  public static final int ROLE_ROOT= -1;//管理层：看自己分配任务
  public static final int ROLE_DEP_MANAGER = 0;//部门主管：看所有订单 分配工作任务
  
  public static final int ROLE_SALER = 1;//销售员：看自己订单 看分配任务
  public static final int ROLE_WORKER = 2;//工厂工人：看自己分配任务
  public static final int ROLE_SALER_MANAGER = 3;//销售主管：

  public static User dao = new User();

  public static AttributeFinder afinder = AttributeFinder.getInstance();

  public Page<User> paginate( int pageNumber, int pageSize ) {
    return paginate( pageNumber, pageSize, "select *", "from user order by id asc" );
  }

  public Map<String, Object> getAttrs() {
    return super.getAttrs();
  }

  /**
   * 部门主管
   * @return
   */
  public boolean isDepManager() {
    Integer role = Integer.parseInt( getAttrs().get( "role" ).toString() );
    return role == ROLE_DEP_MANAGER;
  }
  
  /**
   * 销售
   * @return
   */
  public boolean isSaler() {
    Integer role = Integer.parseInt( getAttrs().get( "role" ).toString() );
    return role == ROLE_SALER;
  }
  
  /**
   * 工厂员工
   * @return
   */
  public boolean isWorker() {
    Integer role = Integer.parseInt( getAttrs().get( "role" ).toString() );
    return role == ROLE_WORKER;
  }
  
  /**
   * 管理层
   * @return
   */
  public boolean isRoot() {
    Integer role = Integer.parseInt( getAttrs().get( "role" ).toString() );
    return role == ROLE_ROOT;
  }
  
  /**
   * 销售主管
   * @return
   */
  public boolean isSalerManager() {
    Integer role = Integer.parseInt( getAttrs().get( "role" ).toString() );
    return role == ROLE_SALER_MANAGER;
  }
  
  /**
   * 销售和管理层允许登录到web
   * @return
   */
  public boolean canLoginToWeb(){
    return isRoot() || isSaler() || isSalerManager();
  }
}
