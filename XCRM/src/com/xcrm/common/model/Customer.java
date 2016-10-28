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
public class Customer extends Model<Customer>{

  private static final long serialVersionUID = 1946424759277934855L;
  
  public static Customer dao = new Customer();
  
  public Page<Customer> paginate(int pageNumber, int pageSize) {
    return paginate(pageNumber, pageSize, "select *", "from customer order by id asc");
}
  
  
  
}
