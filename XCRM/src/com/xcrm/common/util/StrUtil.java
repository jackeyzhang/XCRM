/**
 * 
 */
package com.xcrm.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author jzhang12
 *
 */
public class StrUtil {

  public static String formatNum( Number Num ) {
    if ( Num == null )
      return "0.00";
    NumberFormat format = new DecimalFormat( "##0.00" );
    return format.format( Num );
  }

  public static String formatInt( Number Num ) {
    if ( Num == null )
      return "0";
    NumberFormat format = new DecimalFormat( "##0" );
    return format.format( Num );
  }

  public static String formatPrice( Number price ) {
    if ( price == null )
      return "￥0.00";
    NumberFormat format = new DecimalFormat( "￥##,##0.00" );
    return format.format( price );
  }

  public static String formatPercentage( String Num ) {
    BigDecimal format = new BigDecimal( Num );
    return "" + format.setScale( 2, RoundingMode.HALF_UP ).doubleValue();
  }

  public static String formatDate( Date date, String format ) {
    SimpleDateFormat oDateFormat = new SimpleDateFormat( format );
    String lsDate = "";
    if ( date != null ) {
      lsDate = oDateFormat.format( date );
    }
    return lsDate;
  }

  public static boolean isEmpty( String str ) {
    if ( str == null )
      return true;
    return str.isEmpty();
  }

  public static List<String> split( String inStr, String splitCode ) {
    if ( isEmpty( inStr ) )
      return new ArrayList<String>();
    String[] strArray = inStr.split( splitCode );
    return Arrays.asList( strArray );
  }
}
