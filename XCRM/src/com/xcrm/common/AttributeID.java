/**
 * 
 */
package com.xcrm.common;


/**
 * @author jzhang12
 *
 */
public class AttributeID {
  
  public static final int TYPE_NUMBER = 1, TYPE_LIST_SINGLE = 2, TYPE_LIST_MULTIPLE = 3, TYPE_TEXT = 4;

  /**
   * 0 - 100 预留for与业务无关的attribute
   */
  public static final AttributeID PROVINCE = new AttributeID(010, "省/市", TYPE_LIST_SINGLE);
  public static final AttributeID CITY = new AttributeID(011, "城市", TYPE_LIST_SINGLE);
  public static final AttributeID AREA = new AttributeID(012, "区县", TYPE_LIST_SINGLE);
  
  
  /**
   * from 101, extend for business attribute
   */
  
  public static final AttributeID COLOR = new AttributeID(101, "颜色", TYPE_LIST_MULTIPLE);
  public static final AttributeID INVENTORY = new AttributeID(102, "库存", TYPE_NUMBER);
  
  /**
   * from 201, extend for script-in attribute
   */
  public static final AttributeID DEPARTMENT = new AttributeID(201, "部门", TYPE_LIST_SINGLE);
  
  private int id;
  private String name;
  private int type;
  
  public AttributeID( int id, String name, int type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }
  

  
  public int getId() {
    return id;
  }

  
  public void setId( int id ) {
    this.id = id;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName( String name ) {
    this.name = name;
  }

  
  public int getType() {
    return type;
  }

  
  public void setType( int type ) {
    this.type = type;
  }
  

}
