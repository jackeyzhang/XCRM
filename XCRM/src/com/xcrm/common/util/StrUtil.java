/**
 * 
 */
package com.xcrm.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * @author jzhang12
 *
 */
public class StrUtil {

  public static String formatNum( Number Num ){
    NumberFormat format = new DecimalFormat( "##0.00" );
    return format.format( Num );
  }
  
  public static  String formatPrice( Number price ){
    NumberFormat format = new DecimalFormat( "￥##,###.00" );
    return format.format( price );
  }
  
  public static String formatPercentage( double  Num ){
    BigDecimal  format = new BigDecimal ( Num );
    return "" + format.setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
}
