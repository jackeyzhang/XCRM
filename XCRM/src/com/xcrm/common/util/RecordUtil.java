/**
 * 
 */
package com.xcrm.common.util;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * @author jzhang12
 *
 */
public class RecordUtil {

  public static List<Record> fillRowNumber( List<Record> originalRecords ){
    int i = 0;
    for( Record record : originalRecords ){
      record.set( "index", ++i );
    }
    return originalRecords;
  }
}
