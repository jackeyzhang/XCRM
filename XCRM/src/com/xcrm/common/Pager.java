/**
 * 
 */
package com.xcrm.common;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


/**
 * @author jzhang12
 *
 */
public class Pager implements Serializable{
  private static final long serialVersionUID = -7748205953539684381L;

  private int total;
  
  private List<Record> rows;
  
  public Pager(){}
  
  public Pager( int total, List<Record> rows ) {
    super();
    this.total = total;
    this.rows = rows;
  }


  public int getTotal() {
    return total;
  }

  
  public void setTotal( int total ) {
    this.total = total;
  }

  
  public List<Record> getRows() {
    return rows;
  }

  
  public void setRows( List<Record> rows ) {
    this.rows = rows;
  }


}
