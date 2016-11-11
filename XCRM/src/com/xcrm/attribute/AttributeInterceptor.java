package com.xcrm.attribute;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.util.Constant;


/**
 * check user session is valid, if yes then going on, else go to root page.
 */
public class AttributeInterceptor implements Interceptor {

  public void intercept( Invocation inv ) {
    inv.invoke();
    if(inv.getController().getRequest().getMethod().equalsIgnoreCase( "post" )){
      Map<String, String[]> paraMap =  inv.getController().getRequest().getParameterMap();
      updateAttribute(filterAttributeNames(paraMap),getObjectId(paraMap,inv.getController()), getCategoryId(inv.getController()));
    }
  }

  private Map<String, String[]> filterAttributeNames( Map<String, String[]> params ) {
    if ( params.size() == 0 )
      return null;
    Map<String, String[]> map = new HashMap<String, String[]>();
    for ( String key : params.keySet() ) {
      if ( key.startsWith( "attribute-" ) ) {
        map.put( key.substring( key.indexOf( "-" )+1 ), params.get( key ) );
      }
    }
    return map;
  }

  private int getObjectId( Map<String, String[]> params,  Controller controller  ) {
    if ( params.size() == 0 )
      return 0;
    for ( String key : params.keySet() ) {
      if ( key.equalsIgnoreCase( "id" ) ) {
        if(params.get( key )[0].length() > 0){
          return Integer.parseInt( params.get( key )[0] );
        }else if(controller.getSessionAttr( Constant.CUR_OBJ ) != null){
           return ((Model<?>)controller.getSessionAttr( Constant.CUR_OBJ )).getInt( "id" );
        }
      }
    }
    return 0;
  }

  private int getCategoryId( Controller controller ) {
    if ( controller.getRequest().getRequestURI().contains( "/user/" ) ) {
      return Constant.CATEGORY_USER;
    }
    else if ( controller.getRequest().getRequestURI().contains( "/customer/" ) ) {
      return Constant.CATEGORY_CUSTOMER;
    }
    else if ( controller.getRequest().getRequestURI().contains( "/product/" ) ) {
      return Constant.CATEGORY_PRODUCT;
    }
    else if ( controller.getRequest().getRequestURI().contains( "/schedule/" ) ) {
      return Constant.CATEGORY_SCHEDULE;
    }
    else if ( controller.getRequest().getRequestURI().contains( "/store/" ) ) {
      return Constant.CATEGORY_STORE;
    }
    return 0;
  }

  private void updateAttribute( Map<String, String[]> attributeMap, int objectid, int category ) {
    if ( attributeMap == null ||  attributeMap.isEmpty())
      return;
    for ( String attributeid : attributeMap.keySet() ) {
      Attributevalue value = Attributevalue.dao.findFirst( "select * from Attributevalue where attributeid = ?  and objectid= ? and category=?", attributeid, objectid, category );
      if ( value == null ) {
        new Attributevalue().set( "attributeid", attributeid ).set( "objectid", objectid ).set( "category", category ).set( "value", attributeMap.get( attributeid )[0] ).save();
      }
      else {
        value.set( "value", attributeMap.get( attributeid )[0] ).update();
      }
    }
  }
}
