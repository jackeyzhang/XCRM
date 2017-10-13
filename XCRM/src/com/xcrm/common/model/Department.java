package com.xcrm.common.model;

import java.util.ArrayList;
import java.util.List;

import com.xcrm.common.model.base.BaseDepartment;


/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Department extends BaseDepartment<Department> {

  private static List<Department> allDepartments = new ArrayList<Department>();
  
  public static final Department dao = new Department();
  
  public static List<Department> getAllDepartments(){
    if( allDepartments.isEmpty() ){
      reloadAll();
    }
    return allDepartments;
  }
  
  public static void reloadAll( ){
    allDepartments = dao.find( "select * from Department" );
  }
}
