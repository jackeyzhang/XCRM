/**
 * 
 */
package com.xcrm.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.PropUtil;


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
  }

  public abstract String getModalName();
  
  public Model getModel(){
    return null;
  }

  public abstract String getPageHeader();

  public abstract String getToolBarAddButtonTitle();

  public abstract String getIndexHtml();

  public abstract int getCategory();

  public void list() {
    Pager pager = new Pager();
    if(this.getPara("pageNumber") != null){
      int pagenumber = Integer.parseInt(this.getPara("pageNumber"));
      int pagesize = Integer.parseInt(this.getPara("pageSize"));
      Page<Record> page = Db.paginate(pagenumber, pagesize, "select * ", "from " + getModalName() +"");
      pager = new Pager(page.getTotalRow(), page.getList());
      List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
      String searchword = this.getPara( "searchword" );
      Iterator<Record> iter = pager.getRows().iterator();
      for(;iter.hasNext();){
        Record record = iter.next();
        if(record.getStr( "name" ) != null && searchword != null && !record.getStr( "name" ).contains( searchword )){
          iter.remove();
          continue;
        }
        for(Attribute attribute : attributes){
          Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),record.getInt( "id" ), getCategory() );
          if(av == null) continue;
          record.set( "attribute-" + av.getAttributeid(), av.getValue() );
        }
      }
      this.renderJson( pager );
    }else{
      List<Record> records = Db.find( "select * from " + getModalName() );
      pager = new Pager(records.size(), records);
      List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
      for(Record record : pager.getRows()){
        for(Attribute attribute : attributes){
          Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),record.getInt( "id" ), getCategory() );
          if(av == null) continue;
          record.set( "attribute-" + av.getAttributeid(), av.getValue() );
        }
      }
      this.renderJson( records );
    }
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
  
  public String getRealPath() {
    return this.getRequest().getServletContext().getRealPath("/") + Constant.SLASH ;
  }
  
  public String getTempImgPath() {
    return this.getRealPath() + Constant.TEMP_IMG + Constant.SLASH
            + getCurrentUserId() + Constant.SLASH;
  }
  
  public String getPrdImgPath(String prdid){
    String path =  PropUtil.getPrdImgPath() + Constant.SLASH + prdid + Constant.SLASH;
    if(PropUtil.isDevMode()){
      path =  this.getRealPath() + path;
    }
    return path;
  }
  
  public String getPrdImgBaseUrl(){
    String url = PropUtil.getPrdImgPath() + Constant.SLASH;
    if(PropUtil.isDevMode()){
      url =  Constant.SLASH + url;
    }else{
      //TODO:if product env is stand alone image server, 
      //need provide the image domain, now devMode and prdMode use the same url. 
      url =  Constant.SLASH + url;
    }
    return url;
  }

}
