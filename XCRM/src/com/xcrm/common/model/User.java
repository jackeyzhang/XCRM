/**
 * 
 */
package com.xcrm.common.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;


/**
 * @author jzhang12
 *
 */
public class User extends Model<User>{

  private static final long serialVersionUID = 1946424759277934855L;
  
  public static User dao = new User();
  
  public Page<User> paginate(int pageNumber, int pageSize) {
    return paginate(pageNumber, pageSize, "select *", "from user order by id asc");
}
  
  
  
}