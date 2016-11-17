/**
 * 
 */
package com.xcrm.common;

import java.util.HashMap;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.PropertiesUtil;


/**
 * @author jzhang12
 *
 */
public abstract class AbstractController extends Controller {

  public AbstractController() {
    super();
  }

  public void index() {
    setAttribute();
    render( getIndexHtml() );
  }
  
  protected void setAttribute(){
    setAttr( "model", getModalName() );
    setAttr( "page_header", getPageHeader() );
    setAttr( "toolbar_create", getToolBarAddButtonTitle() );
    setAttr( "attriutes", AttributeFinder.getInstance().getAllAttributeList( getCategory() ) );
    setAttr("imgMaxCount", PropertiesUtil.getProductImgMaxSize());
  }

  public abstract String getModalName();

  public abstract String getPageHeader();

  public abstract String getToolBarAddButtonTitle();

  public abstract String getIndexHtml();

  public abstract int getCategory();

  public void list() {
    List<Record> records = Db.find( "select * from " + getModalName() );
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
    for(Record record : records){
      for(Attribute attribute : attributes){
        Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),record.getInt( "id" ), getCategory() );
        if(av == null) continue;
        record.set( "attribute-" + av.getAttributeid(), av.getValue() );
      }
    }
    this.renderJson( records );
  
  }

  public void save() {
    this.renderHtml( "not implementation" );
  }

  public void update() {
    this.renderHtml( "not implementation" );
  }

  public void remove() {
    this.renderHtml( "not implementation" );
  }

  public void forwardIndex() {
    this.forwardAction( "/" + getModalName() + "/index" );
  }
  
  public void forwardIndex(Model<?> model) {
    this.setSessionAttr( Constant.CUR_OBJ, model );
    this.forwardAction( "/" + getModalName() + "/index" );
  }

  @SuppressWarnings("rawtypes")
  public int getCurrentUserId() {
    Object user = getSessionAttr( Constant.CUR_USER );
    int userId = (Integer) ( (HashMap)user ).get( "id" );
    return userId;
  }

  @SuppressWarnings("rawtypes")
  public int getCurrentStoreId() {
    Object user = getSessionAttr( Constant.CUR_USER );
    int storeid = (Integer) ( (HashMap)user ).get( "storeid" );
    return storeid;
  }

}
