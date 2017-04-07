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

  public static final int ROLE_ROOT = 0;
  public static final int ROLE_NORMAL = 1;

  public static User dao = new User();

  public static AttributeFinder afinder = AttributeFinder.getInstance();

  public Page<User> paginate( int pageNumber, int pageSize ) {
    return paginate( pageNumber, pageSize, "select *", "from user order by id asc" );
  }

  public Map<String, Object> getAttrs() {
    return super.getAttrs();
  }

  public boolean isRoot() {
    Integer role = Integer.parseInt( getAttrs().get( "role" ).toString() );
    return role == ROLE_ROOT;
  }
}
