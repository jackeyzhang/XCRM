package com.xcrm.common.util;


public class NumUtil {


  public static Long lVal( String val ){
    if( val == null || val.isEmpty() ) return 0l;
    Long lval = Long.parseLong( val );
    return lval;
  }
  
  public static Integer iVal( String val ){
    if( val == null || val.isEmpty() ) return 0;
    Integer ival = Integer.parseInt( val );
    return ival;
  }
  
  
}
