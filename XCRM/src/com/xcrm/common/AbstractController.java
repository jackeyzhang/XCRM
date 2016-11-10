/**
 * 
 */
package com.xcrm.common;

import java.util.HashMap;

import com.jfinal.core.Controller;
import com.xcrm.common.util.Constant;



/**
 * @author jzhang12
 *
 */
public abstract class AbstractController extends Controller {

  public AbstractController() {
    super();
  }

  public void index() {
    setAttr( "model", getModalName() );
    setAttr( "page_header", getPageHeader() );
    setAttr( "toolbar_create", getToolBarAddButtonTitle() );
    setAttr( "attriutes", AttributeFinder.getInstance().getAllAttributeIDList( getCategory()) );
    render( getIndexHtml() );
  }

  public abstract String getModalName();

  public abstract String getPageHeader();

  public abstract String getToolBarAddButtonTitle();

  public abstract String getIndexHtml();
  
  public abstract int getCategory();

  
  public void list() {
    this.renderHtml( "not implementation" );
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
  
  @SuppressWarnings("rawtypes")
  public int getCurrentUserId(){
    Object user = getSessionAttr(Constant.CUR_USER);
    int userId = (int)((HashMap)user).get( "id" );
    return userId;
  }
  
  @SuppressWarnings("rawtypes")
  public int getCurrentStoreId(){
    Object user = getSessionAttr(Constant.CUR_USER);
    int storeid = (int)((HashMap)user).get( "storeid" );
    return storeid;
  }

}
