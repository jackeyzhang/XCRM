package com.demo.common.model;

import java.util.List;


public class WebRecord<M> {
  
  private int total;
  
  private List<M> rows;

  
  public int getTotal() {
    return total;
  }

  
  public void setTotal( int total ) {
    this.total = total;
  }

  
  public List<M> getRows() {
    return rows;
  }

  
  public void setRows( List<M> rows ) {
    this.rows = rows;
  }
  
  

}
