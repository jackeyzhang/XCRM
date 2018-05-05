/**
 * 
 */
package com.xcrm.common.util;

import java.util.Calendar;
import java.util.Date;


/**
 * @author jzhang12
 *
 */
public class DateUtil {

  public static Date get23h59m59sOfOneDay( Date date){
    Calendar endcalendar = Calendar.getInstance();
    endcalendar.setTime( date );
    endcalendar.set( Calendar.HOUR_OF_DAY, 23 );
    endcalendar.set( Calendar.MINUTE, 59 );
    endcalendar.set( Calendar.SECOND, 59 );
    return endcalendar.getTime();
  }
  
  public static Date getFirstDateOfMonth( Date date, Integer beforeCurrentMonth ){
    Calendar calender = Calendar.getInstance();
    calender.setTime( date );
    if( beforeCurrentMonth != null ){
      int month = calender.get( Calendar.MONTH );
      calender.set( Calendar.MONTH, month - beforeCurrentMonth );
    }
    calender.set( Calendar.DAY_OF_MONTH, 1 );
    calender.set( Calendar.HOUR_OF_DAY , 0 );
    calender.set( Calendar.MINUTE , 0 );
    calender.set( Calendar.SECOND , 0 );
    return calender.getTime();
  }
  
  public static String getFurtureDay( int day ){
    Calendar currentdate = Calendar.getInstance();
    currentdate.add( Calendar.DAY_OF_MONTH, day );
    String startDateStr = StrUtil.formatDate( currentdate.getTime(), "yyyy-MM-dd" );
    return startDateStr;
  }
  
  public static String getFurtureDayByMonth( int month ){
    Calendar currentdate = Calendar.getInstance();
    currentdate.add( Calendar.MONTH, month );
    String startDateStr = StrUtil.formatDate( currentdate.getTime(), "yyyy-MM-dd" );
    return startDateStr;
  }
  
  public static void main(String[] args){
    System.out.println( getFirstDateOfMonth(new Date(), 2));
  }
}
