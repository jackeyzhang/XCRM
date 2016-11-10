/**
 * 
 */
package com.xcrm.common;

import java.util.List;

import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributeid;


/**
 * @author jzhang12
 *
 */
public class AttributeFinder {

  private static AttributeFinder instance = null;
  
  public static AttributeFinder getInstance( ){
    if(instance == null){
      instance = new AttributeFinder();
    }
    return instance;
  }
  
  private AttributeFinder() {}
  
  public List<Attribute> getAllAttributeList( int category ){
    return Attribute.dao.find( "select * from Attribute where category = " + category );
  }
  
  public List<Attribute> getAttributeList( int category, int storeid ){
    return Attribute.dao.find( "select * from Attribute where category = " + category + " and scopetype=2 and scopevalue = " + storeid );
  }
  
  public Attribute getAttribute(int category, int attributeid ){
    return Attribute.dao.findFirst( "select * from Attribute where category = " + category + " and attributeid = " + attributeid );
  }
  
  public List<Attributeid> getAllAttributeIDList( int category ){
    return Attributeid.dao.find( "select * from Attributeid d inner join attribute a on d.id = a.attributeid where a.category = " + category );
  }

  
}
