/**
 * 
 */
package com.xcrm.common;

import java.util.List;

import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributeid;
import com.xcrm.common.model.Attributevalue;


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
  
  public List<Attribute> getAllAttributeList( int... category ){
    return Attribute.dao.find( "select * from Attribute where category in ( " + joinStr(",", category)  +") and visiable=1");
  }
  
  public List<Attribute> getAttributeList( int category, int storeid ){
    return Attribute.dao.find( "select * from Attribute where visiable=1 and category = " + category + " and scopetype=2 and scopevalue = " + storeid );
  }
  
  public Attribute getAttribute(int category, int attributeid ){
    return Attribute.dao.findFirst( "select * from Attribute where category = " + category + " and attributeid = " + attributeid );
  }
  
  public List<Attributeid> getAllAttributeIDList( int category ){
    return Attributeid.dao.find( "select * from Attributeid d inner join attribute a on d.id = a.attributeid where a.category = " + category );
  }  
  
  public List<Attributevalue> getAttributeValueList( int category , int object , int... attributeid ){
    List<Attributevalue> avs = Attributevalue.dao.find(
        "select * from attributevalue where objectid=? and category=?",object,category); 
    return avs;
  }

  public String joinStr( String split, int... categorys ) {
    String s = "";
    for ( int category : categorys ) {
      s += category;
      s += split;
    }
    if(s.length() > 1){
      return s.substring( 0, s.length()-1 );
    }
    return s;
  }
  
}
