/**
 * 
 */
package com.xcrm.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author jzhang12
 *
 */
public class StrUtil {

  public static String formatNum( Number Num ){
    if(Num == null ) return "0.00";
    NumberFormat format = new DecimalFormat( "##0.00" );
    return format.format( Num );
  }
  
  public static String formatInt( Number Num ){
    if(Num == null ) return "0";
    NumberFormat format = new DecimalFormat( "##0" );
    return format.format( Num );
  }
  
  
  public static  String formatPrice( Number price ){
    if(price == null ) return "￥0.00";
    NumberFormat format = new DecimalFormat( "￥##,##0.00" );
    return format.format( price );
  }
  
  public static String formatPercentage( String  Num ){
    BigDecimal  format = new BigDecimal ( Num );
    return "" + format.setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
  
  public static String formatDate( Date date, String format ){
      SimpleDateFormat oDateFormat = new SimpleDateFormat( format );
      String lsDate = "";
      if( date != null ) {
        lsDate = oDateFormat.format( date );
      }
      return lsDate;
  }
}
