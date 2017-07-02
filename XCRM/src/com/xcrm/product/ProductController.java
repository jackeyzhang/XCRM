package com.xcrm.product;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeFinder;
import com.xcrm.common.AttributeID;
import com.xcrm.common.Pager;
import com.xcrm.common.model.Attribute;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.model.Price;
import com.xcrm.common.model.Priceinventoryvalue;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productcategory;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.qr2.QRCodeUtil;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.MD5Util;
import com.xcrm.common.util.PropUtil;

@Before(ProductInterceptor.class)
public class ProductController extends AbstractController {

  public void list() {
    Pager pager = new Pager();
    if ( this.getPara( "pageNumber" ) != null ) {
      int pagenumber = Integer.parseInt( this.getPara( "pageNumber" ) );
      int pagesize = Integer.parseInt( this.getPara( "pageSize" ) );
      int level1 = this.getParaToInt( "level1", 0 );
      int level2 = this.getParaToInt( "level2", 0 );
      int salesseason = this.getParaToInt( "salesseason", 0 );
      String searchword = this.getPara( "searchword" );
      if ( searchword == null ) {
        searchword = "";
      }
      Page<Record> page = Db.paginate( pagenumber, pagesize, "select prd.*,(select count(*) from productpic pic where pic.productid=prd.id) piccount,(select ss.id from salesseason ss where ss.id=prd.salesseason) salesseason ", " from product prd where 1=1 " + ( level1 > 0 ? " and level1category= " + level1 : "" )
          + ( level2 > 0 ? " and level2category= " + level2 : "" ) 
          + ( salesseason > 0 ? " and salesseason= " + salesseason : "" ) 
          + ( searchword.isEmpty() ? "" : " and name like '%" + searchword + "%'" ) );
      pager = new Pager( page.getTotalRow(), page.getList() );
    }
    else {
      List<Record> records = Db.find( "select id,name,(select ss.id from salesseason ss where ss.id=prd.salesseason) salesseason from " + getModalName() + " prd");
      pager = new Pager( records.size(), records );
    }
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList( getCategory() );
    for ( Record record : pager.getRows() ) {
      for ( Attribute attribute : attributes ) {
        Attributevalue av = Attributevalue.dao.findFirst( "select * from attributevalue where attributeid=? and objectid=? and category=?", attribute.getAttributeid(),
            record.getInt( "id" ), getCategory() );
        if ( av == null )
          continue;
        record.set( "attribute-" + getCategory() + "-" + av.getAttributeid(), av.getValue() );
      }
    }
    this.renderJson( pager );

  }

  public void detail() {
    setAttribute();
    Product product =
        Product.dao.findFirst("select prd.*,(select price from price where product=prd.id) price from product prd where barcode =?", this.getPara("barcode"));
    List<Productpic> pics =
        Productpic.dao.find("select * from productpic where productid=?", product.getId());
    List<Attributevalue> attributevalues =
        Attributevalue.dao.find("select * from attributevalue where objectid=?", product.getId());

    setAttr("page_header", "产品详细信息");
    setAttr("product_qr2_path", getPrdQr2BaseUrl());
    setAttr("product_color", AttributeID.getValue(attributevalues, AttributeID.PRD_COLOR));
    setAttr("product_size", AttributeID.getValue(attributevalues, AttributeID.PRD_SIZE));
    setAttr("product_depatment", AttributeID.getValue(attributevalues, AttributeID.PRD_DEPARTMENT));
    setAttr("product_material", AttributeID.getValue(attributevalues, AttributeID.PRD_MATERIAL));
    setAttr("prdimg_path", getPrdImgBaseUrl());
    setAttr("prdimages", pics);
    setAttr("product", product);
    render("productdetail.html");
  }
  
  /**
   * print all products
   * 
   */
  public void printproducts(){
    String salesseason = this.getPara( "ss" );
    String level1category = this.getPara( "lev1" );
    String level2category = this.getPara( "lev2" );
    StringBuilder appendWhere = new StringBuilder();
    appendWhere.append( salesseason.isEmpty() ? "" : " and salesseason=" + salesseason );
    appendWhere.append( level1category.isEmpty() ? "" : " and level1category=" + level1category );
    appendWhere.append( level2category.isEmpty() ? "" : " and level2category=" + level2category );
    List<Product> products = Product.dao.find("select id,name,(select price from price where product=prd.id) price from product prd where 1 = 1 " +  appendWhere);
    setAttr("products", products);
    setAttr("prdimg_path", getPrdImgBaseUrl());
    setAttr("product_qr2_path", getPrdQr2BaseUrl());
    render("printproducts.html");
  }

  public void save() {
    Product product = this.getModel(Product.class, "", true).set("barcode", MD5Util.getSystemKey())
        .set("createuser", this.getCurrentUserId()).set("createdate", new Date());
    Product duplicatProduct = Product.dao.findFirst( "select * from product where name=?", product.getName() );
    if ( duplicatProduct != null ){
      this.renderError( 522 );//name is duplicated
    }
    Productcategory level2 = Productcategory.dao.findById( product.getLevel2category() );
    product.setLevel1category( level2.getPid() );
    product.save();
    saveImgs(product.getId());
    QRCodeUtil.generator(product.getId(), getRealPath(), PropUtil.getPrdQr2Path());
    
    Price price = new Price();
    price.setProduct( product.getId() );
    price.setName( product.getName() + "价格" );
    price.save();
    forwardIndex(product);
  }

  public void loadimgs() {
    String destDirPath = getTempImgPath();
    File destDir = getFileByStr(destDirPath);
    if (destDir.exists()) {
      try {
        FileUtils.deleteDirectory(destDir);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    destDir.mkdirs();
    String prdid = this.getPara("prdid");
    File srcDir = getFileByStr(getPrdImgPath(prdid));
    String imgs = "";
    if (srcDir.exists()) {
      for (File record : srcDir.listFiles()) {
        try {
          FileUtils.copyFile(record, getFileByStr(destDirPath + record.getName()));
        } catch (IOException e) {
          e.printStackTrace();
        }
        imgs += record.getName() + ",";

      }
    }
    this.renderJson("{\"imgs\":\"" + imgs + "\"}");
  }

  public void saveImgs(int prdid) {
    String imgs = this.getPara("imgs");
    if (!StringUtils.isEmpty(imgs)) {
      String[] imgArray = imgs.split(Constant.COMMA);
      Db.update("delete from productpic where productid=" + prdid);
      String srcDirPath = getTempImgPath();
      String destDirPath = getPrdImgPath(String.valueOf(prdid));
      File destDir = getFileByStr(destDirPath);
      if (destDir.exists()) {
        try {
          FileUtils.deleteDirectory(destDir);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      destDir.mkdir();
      for (String img : imgArray) {
        if (StringUtils.isEmpty(img))
          continue;
        Productpic pic = new Productpic();
        pic.setProductid(prdid);
        pic.setFielname(img);
        pic.save();
        try {
          FileUtils.moveFile(getFileByStr(srcDirPath + img), getFileByStr(destDirPath + img));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        FileUtils.deleteDirectory(getFileByStr(srcDirPath));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private File getFileByStr(String str) {
    return new File(str);
  }

  public void upload() {
    UploadFile uploadFile = this.getFile();
    if (uploadFile != null) {
      String destDir = getTempImgPath();
      synchronized (Integer.valueOf(getCurrentUserId())) {
        getFileByStr(destDir).mkdirs();
      }
      File destFile = getFileByStr(destDir + uploadFile.getFileName());
      if (destFile.exists()) {
        destFile.delete();
      }
      try {
        FileUtils.moveFile(uploadFile.getFile(), destFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    this.renderJson();
  }

  @Override
  public void update() {
    Product product = this.getModel(Product.class, "", true)
        .set("edituser", this.getCurrentUserId()).set("editdate", new Date());
    if (product.getStr("barcode").isEmpty()) {
      product.set("barcode", MD5Util.getSystemKey());
    }
    Productcategory level2 = Productcategory.dao.findById( product.getLevel2category() );
    product.setLevel1category( level2.getPid() );
    product.update();
    saveImgs(product.getId());
    forwardIndex();
  }

  @Override
  public void remove() {
    Integer prdid =this.getParaToInt(0);
    Product.dao.deleteById(prdid);
    Price price = Price.dao.findFirst( "select * from price where product=" + prdid );
    if( price != null) {
      price.delete();
    }
    forwardIndex();
  }

  @Override
  public String getModalName() {
    return "product";
  }

  @Override
  public String getPageHeader() {
    return "创建或修改产品相关信息";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "创建产品";
  }

  @Override
  public String getIndexHtml() {
    return "product.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_PRODUCT;
  }
  
  public void wxlist(){
    List<Record> records = Db.find("select id,name,(select pic.fielname from productpic pic where pic.productid=prd.id limit 1) filename,(select name from productcategory where level=1 and id=prd.level1category limit 1 ) category,(select price from price where product=prd.id) price  from product prd limit 20 ");
    Pager pager = new Pager(records.size(), records);
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList(getCategory());
    for (Record record : pager.getRows()) {
      for (Attribute attribute : attributes) {
        Attributevalue av = Attributevalue.dao.findFirst(
            "select * from attributevalue where attributeid=? and objectid=? and category=? ",
            attribute.getAttributeid(), record.getInt("id"), getCategory());
        if (av == null)
          continue;
        record.set("attribute-" + getCategory() + "-"+av.getAttributeid(), av.getValue());
      }
    }
    this.renderJson(pager);
  }
  
  public void wxsearchlist(){
    if( getPara( "searchtext" ).isEmpty( ) ){
      wxlist();
      return;
    }
    List<Record> records = Db.find("select id,name,(select pic.fielname from productpic pic where pic.productid=prd.id limit 1) filename,(select name from productcategory where level=1 and id=prd.level1category limit 1 ) category  from product prd where prd.name like ? ", "%"+getPara( "searchtext" )+"%");
    Pager pager = new Pager(records.size(), records);
    List<Attribute> attributes = AttributeFinder.getInstance().getAllAttributeList(getCategory());
    for (Record record : pager.getRows()) {
      for (Attribute attribute : attributes) {
        Attributevalue av = Attributevalue.dao.findFirst(
            "select * from attributevalue where attributeid=? and objectid=? and category=? ",
            attribute.getAttributeid(), record.getInt("id"), getCategory());
        if (av == null)
          continue;
        record.set("attribute-" + getCategory() + "-"+av.getAttributeid(), av.getValue());
      }
    }
    this.renderJson(pager);
  }
  
  public void wxgetproduct() {
    Record record = Db.findFirst( "select id,name,(select pic.fielname from productpic pic where pic.productid=prd.id limit 1) filename from product prd where prd.id=?",
        getPara( "prdid" ) );
    List<Priceinventoryvalue> list = Priceinventoryvalue.dao.find( "select pi.* from priceinventoryvalue pi left join price pr on pi.priceid=pr.id  where pi.price>0 and pr.product=?",
        this.getPara( "prdid" ) );
    record.set( "prices",list);
    renderJson( record );
  }
}
