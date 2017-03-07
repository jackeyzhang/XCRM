package com.xcrm.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeFinder;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.model.Price;
import com.xcrm.common.model.Priceinventoryvalue;
import com.xcrm.common.model.Product;
import com.xcrm.common.util.Constant;


@Before(ProductInterceptor.class)
public class PriceController extends AbstractController {

  public void index() {
    super.index();
  }

  public void preadd() {
    String pid = getPara( "pid" );
    this.setAttribute();
    if ( !StringUtils.isEmpty( pid ) ) {
      this.refreshAttributeforPrice( Integer.parseInt( pid ) );
    }
    fillAttribute( pid );
    setAttr( "id", getPara( "id" ) );
    render( "/price/add.html" );
  }

  public void loadingAddData() {
    String id = getPara( "id" );
    Record record = Db.findById( "price", id );
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
    for ( Attribute attribute : attributes ) {
      Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(), id,
          getCategory() );
      if ( av == null || av.getAttributeid() > 200 )
        continue;
      record.set( "attribute-" + getCategory() + "-" + av.getAttributeid(), av.getValue() );
    }
    Record product = Db.findById( "product", record.getInt( "product" ) );
    record.set( "productname", product.get( "name" ) );
    setAttr( "id", id );
    renderJson( record );
  }

  private void fillAttribute( String productid ) {
    List<Record> records = new ArrayList<Record>();
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
    Map<Attribute, String> valueMap = new HashMap<Attribute, String>();
    for ( Attribute attribute : attributes ) {
      Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(), productid,
          Constant.CATEGORY_PRODUCT );
      if ( av == null || av.getAttributeid() < 200 )
        continue;   
      valueMap.put( attribute, av.getValue() );
    }

    String value1 = "", value2 = "", value3 = "";
    if ( valueMap.values().size() >= 1 ) {
      value1 = getSpecificAttribute( valueMap, 204 );
    }
    if ( valueMap.values().size() >= 2 ) {
      value2 = getSpecificAttribute( valueMap, 206 );
    }
    if ( valueMap.values().size() >= 3 ) {
      value3 = getSpecificAttribute( valueMap, 205 );
    }
    
    if ( !StringUtils.isEmpty( value1 ) && !StringUtils.isEmpty( value2 ) && !StringUtils.isEmpty( value3 ) ) {
      String[] v1 = value1.split( "," );
      String[] v2 = value2.split( "," );
      String[] v3 = value3.split( "," );
      for ( String vv1 : v1 ) {
        for ( String vv2 : v2 ) {
          for ( String vv3 : v3 ) {
            Record r = new Record();
            r.set( "c1", vv1 );
            r.set( "c2", vv2 );
            r.set( "c3", vv3 );
            Priceinventoryvalue value = getPriceAndInventory( vv1 + "-"+ vv2 +  "-"+ vv3, this.getParaToInt("id"));
            if(value!=null){
              r.set( "count", value.getCount());
              r.set( "inventory", value.getInventory());
              r.set( "price", value.getPrice() );
            }else{
              r.set( "count", 0);
              r.set( "price", 0 );
              r.set( "inventory", 0);
            }
            records.add( r );
          }
        }
      }
    }
    setAttr( "avs", records );
  }

  @Override
  protected void preRenderJsonForList( Record record ) {
    int product = record.getInt( "product" );
    Product p = Product.dao.findById( product );
    record.set( "level1category", p.getLevel1category() );
    record.set( "level2category", p.getLevel2category() );

    List<Attributevalue> avs = AttributeFinder.getInstance().getAttributeValueList( getCategory(), record.getInt( "id" ) );
    for ( Attributevalue av : avs ) {
      record.set( "attribute-" + getCategory() + "-" + av.getAttributeid(), av.getValue() );
    }

  }

  public void save() {
    Price price = this.getModel( Price.class, "", true ).set( "store", this.getCurrentStoreId() ).set( "createuser", this.getCurrentUserId() ).set( "createtime", new Date() );
    price.save();
    updatePriceAndInventory( price.getId() );
    forwardIndex( price );
  }

  public void update() {
    Price price = this.getModel( Price.class, "", true ).set( "updateuser", this.getCurrentUserId() ).set( "updatetime", new Date() );
    if(price.getId() == null){
      price.set( "createuser", this.getCurrentUserId() );
      price.save();
    }else{
      price.update();
    }
    updatePriceAndInventory( price.getId() );
    this.forwardIndex();
  }

  public void remove() {
    Price.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }

  @Override
  public String getModalName() {
    return "price";
  }

  @Override
  public String getPageHeader() {
    return "价格设置";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "添加价格";
  }

  @Override
  public String getIndexHtml() {
    return "price.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_PRICE;
  }

  private void updatePriceAndInventory( int priceid ) {
    for ( String key : getParaMap().keySet() ) {
      String dbkey = null;
      if ( key.startsWith( "price-" ) ) {
        dbkey = key.replace( "price-", "" );
      }
      if ( key.startsWith( "count-" ) ) {
        dbkey = key.replace( "count-", "" );
      }
      if ( key.startsWith( "inventory-" ) ) {
        dbkey = key.replace( "inventory-", "" );
      }
      Priceinventoryvalue value = getPriceAndInventory(dbkey, priceid);
      //update
      if ( value != null && key.startsWith( "price-" ) ) {
        value.set( "price", getParaMap().get( key )[0] );
      }
      if ( value != null && key.startsWith( "count-" ) ) {
        value.set( "count", getParaMap().get( key )[0] );
      }
      if ( value != null && key.startsWith( "inventory-" ) ) {
        value.set( "inventory", getParaMap().get( key )[0] );
      }
      if ( value != null ) {
        value.update();
        continue;
      }
      //insert
      Priceinventoryvalue newvalue = new Priceinventoryvalue();
      if ( key.startsWith( "price-" ) && getParaMap().get( key ) != null ) {
        newvalue.setPriceid( priceid );
        newvalue.setPricekey( dbkey );
        newvalue.setPrice( Float.parseFloat( getParaMap().get( key )[0] ) );
        String countKey = key.replace( "price-", "count-" );
        if ( getParaMap().get( countKey ) != null ) {
          newvalue.setCount( Integer.parseInt( getParaMap().get( countKey )[0] ) );
        }
        String inventoryKey = key.replace( "count-", "inventory-" );
        if ( getParaMap().get( inventoryKey ) != null ) {
          newvalue.setInventory( Integer.parseInt( getParaMap().get( inventoryKey )[0] ) );
        }
        newvalue.save();
      }
    }
  }
  
  public Priceinventoryvalue getPriceAndInventory( String key, Integer priceid ){
    Priceinventoryvalue value = Priceinventoryvalue.dao.findFirst( "select * from Priceinventoryvalue where pricekey=? and priceid=?", key, priceid );
    return value;
  }
  
  /**
   * key is "红色-M-白沙"
   * 
   * @param key
   * @param productid
   * @return Priceinventoryvalue price 单价, inventory 库存, count 现存
   */
  public Priceinventoryvalue getPriceInventory( String key, Integer productid ){
    Priceinventoryvalue value = Priceinventoryvalue.dao.findFirst( "select * from Priceinventoryvalue pv, price pc where pv.priceid= pc.id and pv.pricekey=? and pc.product=?", key, productid );
    return value;
  }

  private String getSpecificAttribute(Map<Attribute,String> map,int attributeid){
    for(Attribute a : map.keySet()){
      if(a.getAttributeid() == attributeid){
        return map.get( a );
      }
    }
    return null;
  }
}
