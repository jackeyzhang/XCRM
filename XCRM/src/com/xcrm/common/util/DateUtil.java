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
}
