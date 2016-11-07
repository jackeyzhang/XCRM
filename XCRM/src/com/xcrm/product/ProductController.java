package com.xcrm.product;

import java.io.File;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.upload.UploadFile;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Product;
import com.xcrm.common.util.Constant;


@Before(ProductInterceptor.class)
public class ProductController extends AbstractController {

  public void list() {
    List<Product> Products = Product.dao.find( "select * from product" );
    this.renderJson( Products );
  }

  public void save() {
    this.getModel( Product.class, "" ).save();
    forwardIndex();
  }

  public void delupload() {
    System.out.println( 333 );
  }

  public void upload() {
    UploadFile uploadFile = this.getFile();
    String filename = null;
    if ( uploadFile != null ) {
      File file = uploadFile.getFile();
      File dest = new File( file.getAbsolutePath() + Constant.UNDER_LINE + getCurrentUserId() );
      if ( dest.exists() ) {
        dest.delete();
      }
      file.renameTo( dest );
      file.delete();
      filename = file.getName();
    }
    if ( filename == null ) {
      this.renderJson();
    }
    else {
      this.renderJson( "[\"" + filename + "\"]" );
    }
  }

  public void update() {
    this.getModel( Product.class, "" ).update();
    forwardIndex();
  }

  public void remove() {
    Product.dao.deleteById( this.getParaToInt( 0 ) );
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
}
