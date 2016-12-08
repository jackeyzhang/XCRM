package com.xcrm.product;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeFinder;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.model.Price;
import com.xcrm.common.model.Product;
import com.xcrm.common.util.Constant;


@Before(ProductInterceptor.class)
public class PriceController extends AbstractController {

  public void index() {
    super.index();
  }
  
  public void preadd(){
    String pid = getPara("pid");
    this.setAttribute();
    if (!StringUtils.isEmpty(pid)) {
      this.refreshAttributeforPrice( NumberUtils.stringToInt( pid ) );
    }
    render("/price/add.html");
  }
  
  public void loadingAddData(){
    String id= getPara("id");
    Record record = Db.findById( "price", id );
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList(getCategory());
      for (Attribute attribute : attributes) {
        Attributevalue av = Attributevalue.dao.findFirst(
            "select * from attributevalue where attributeid=? and objectid=? and category=?",
            attribute.getAttributeid(), id, getCategory());
        if (av == null)
          continue;
        record.set("attribute-" + getCategory() + "-"+av.getAttributeid(), av.getValue());
      }
    this.setAttr( "id", id );
    renderJson(record);
  }
  

  @Override
  protected void preRenderJsonForList( Record record ) {
    int product = record.getInt( "product" );
    Product p = Product.dao.findById( product );
    record.set( "level1category", p.getLevel1category() );
    record.set( "level2category", p.getLevel2category() );
    
    List<Attributevalue> avs = AttributeFinder.getInstance().getAttributeValueList( getCategory(), record.getInt( "id" ));
    for (Attributevalue av : avs) {
      record.set("attribute-"+ getCategory() + "-" + av.getAttributeid(), av.getValue());
    }
    
  }

  public void save() {
    Price price = this.getModel( Price.class, "", true ).set( "store", this.getCurrentStoreId() ).set( "createuser", this.getCurrentUserId() ).set( "createtime", new Date() );
    price.save();
    forwardIndex( price );
  }
  
  public void update(){
    this.getModel( Price.class, "", true ).set( "updateuser",  this.getCurrentUserId() ).set( "updatetime", new Date() ).update();
    this.forwardIndex();
  }
  
  public void remove(){
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
}
