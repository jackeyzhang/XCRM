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

  public static final int[] USED_FOR_PRICE_FROM_PRODUCT = { AttributeID.PRD_MATERIAL.getId(), AttributeID.PRD_COLOR.getId(), AttributeID.PRD_SIZE.getId() };

  public AbstractController() {
    super();
  }

  public void index() {
    preSetAttribute();
    setAttribute();
    render( getIndexHtml() );
  }

  protected void preSetAttribute() {}

  protected void setAttribute() {
    setAttr( "model", getModalName() );
    setAttr( "page_header", getPageHeader() );
    setAttr( "toolbar_create", getToolBarAddButtonTitle() );
    if ( getModalName().equalsIgnoreCase( "price" ) ) {
      refreshAttributeforPrice( null );
    }
    else {
      setAttr( "attriutes", AttributeFinder.getInstance().getAllAttributeList( getCategory() ) );
    }
    setAttr( "imgMaxCount", PropUtil.getPrdImgMaxSize() );
  }

  public abstract String getModalName();

  public Model getModel() {
    return null;
  }

  protected void refreshAttributeforPrice( Integer productid ) {
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( Constant.CATEGORY_PRICE );
    List<Attributevalue> attributesForProduct = AttributeFinder.getInstance().getAttributeValueList( Constant.CATEGORY_PRODUCT, productid == null ? 0 : productid,
        USED_FOR_PRICE_FROM_PRODUCT );
    Iterator<Attribute> iter = attributes.iterator();
    for ( ; iter.hasNext(); ) {
      Attribute attribute = iter.next();
      for ( Attributevalue productAttr : attributesForProduct ) {
        if ( productAttr.getAttributeid().equals( attribute.getAttributeid() ) ) {
          attribute.setValue( productAttr.getValue() );
        }
      }
    }
    setAttr( "attriutes", attributes );
  }

  public abstract String getPageHeader();

  public abstract String getToolBarAddButtonTitle();

  public abstract String getIndexHtml();

  public abstract int getCategory();

  public void list() {
    if ( this.getPara( "pageNumber" ) != null ) {
      int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
      int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
      String searchword = this.getPara( "searchword" );
      String sqlExcept = searchword == null ? "from " + getModalName() : "from " + getModalName() + " where " + searchWord() + " like '%" + searchword + "%'";
      Page<Record> page = Db.paginate( pagenumber, pagesize, "select * ", sqlExcept);
      Pager pager = new Pager( page.getTotalRow(), page.getList() );
      List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
      Iterator<Record> iter = pager.getRows().iterator();
      for ( ; iter.hasNext(); ) {
        Record record = iter.next();
        for ( Attribute attribute : attributes ) {
          Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),
              record.getInt( "id" ), getCategory() );
          if ( av == null )
            continue;
          record.set( "attribute-" + getCategory() + "-" + av.getAttributeid(), av.getValue() );
        }
        preRenderJsonForList( record );
      }
      this.renderJson( pager );
    }
    else {
      List<Record> records = Db.find( "select * from " + getModalName() );
      Pager pager = new Pager( records.size(), records );
      List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
      for ( Record record : pager.getRows() ) {
        for ( Attribute attribute : attributes ) {
          Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),
              record.getInt( "id" ), getCategory() );
          if ( av == null )
            continue;
          record.set( "attribute-" + getCategory() + av.getAttributeid(), av.getValue() );
        }
        preRenderJsonForList( record );
      }
      this.renderJson( records );
    }
  }

  protected void preRenderJsonForList( Record record ) {}

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

  public void forwardIndex( Model<?> model ) {
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
    return this.getRequest().getServletContext().getRealPath( "/" ) + Constant.SLASH;
  }

  public String getTempImgPath() {
    return this.getRealPath() + Constant.TEMP_IMG + Constant.SLASH + getCurrentUserId() + Constant.SLASH;
  }

  public String getContractTemplatePath() {
    return this.getRealPath() + PropUtil.getContractTemplatePath() + Constant.SLASH + Constant.SLASH;
  }

  public String getPrdImgPath( String prdid ) {
    return this.getRealPath() + PropUtil.getPrdImgPath() + Constant.SLASH + prdid + Constant.SLASH;
  }

  public String getPrdQr2Path() {
    return this.getRealPath() + PropUtil.getPrdQr2Path() + Constant.SLASH;
  }

  public String getPrdImgBaseUrl() {
    return getImgUrl( PropUtil.getPrdImgPath() );
  }

  public String getPrdQr2BaseUrl() {
    return getImgUrl( PropUtil.getPrdQr2Path() );
  }

  public String getImgUrl( String url ) {
    url = url + Constant.SLASH;
    if ( PropUtil.isDevMode() ) {
      url = Constant.SLASH + url;
    }
    else {
      url = PropUtil.getImgDomain() + Constant.SLASH + url;
    }
    return url;
  }

  protected long count() {
    return Db.queryLong( "select count(0) from bookitem where status=0 and user=? ", getCurrentUserId() );
  }

  protected String searchWord() {
    return "name";
  }
  
  protected String getSearchStatement( boolean containsAnd, String prefix ){
    String searchword = this.getPara( "searchword" );
    if( searchword == null || searchword.isEmpty() ){
      return "";
    }
    StringBuilder result = new StringBuilder();
    if(containsAnd){
      result.append( " and " );
    }
    result.append( prefix + searchWord() + " like '%" +  searchword + "%' " );
    return result.toString();
  }
  
  protected void fillProductAttributesInOrderCart( List<Record> records, String columnName ){
    for( Record record : records ){
      String columnVal = record.get( columnName );
      if(columnVal == null || columnVal.isEmpty()) continue;
      if ( columnVal.contains( "-" ) ) {
        String[] attributes = columnVal.split( "-" );
        record.set( "m", attributes[0] );
        record.set( "s", attributes[1] );
        record.set( "c", attributes[2] );
      }else{
        columnVal = columnVal.replace( "{", "" );
        columnVal = columnVal.replace( "}", "" );
        columnVal = columnVal.replace( "[", "" );
        columnVal = columnVal.replace( "]", "" );
        columnVal = columnVal.replace( "\"", "" );
        columnVal = columnVal.replace( "204", "m" );
        columnVal = columnVal.replace( "205", "c" );
        columnVal = columnVal.replace( "206", "s" );
        String[] attributes = columnVal.split( "," );
        for( String attribute : attributes ){
          String[] av = attribute.split( ":" );
          record.set( av[0], av[1] );
        }
      }
    }
    
  }
  
 public class ProductAttribute{
    
    private String color;
    private String material;
    private String size;
    
    public ProductAttribute() {
      super();
    }

    public String getColor() {
      return color;
    }
    
    public void setColor( String color ) {
      this.color = color;
    }
    
    public String getMaterial() {
      return material;
    }
    
    public void setMaterial( String material ) {
      this.material = material;
    }
    
    public String getSize() {
      return size;
    }
    
    public void setSize( String size ) {
      this.size = size;
    }
  }
  
}
