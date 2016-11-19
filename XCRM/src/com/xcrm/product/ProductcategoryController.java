package com.xcrm.product;

import java.util.List;

import com.jfinal.aop.Before;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Productcategory;
import com.xcrm.common.util.Constant;


@Before(ProductInterceptor.class)
public class ProductcategoryController extends AbstractController {

  public void index() {
    super.index();
  }

  public void save() {
    this.getModel( Productcategory.class, "" ).save();
    this.forwardIndex();
  }

  public void update() {
    this.getModel( Productcategory.class, "", true ).update();
    this.forwardIndex();
  }

  public void remove() {
    Productcategory.dao.deleteById( this.getParaToInt( 0 ) );
    this.forwardIndex();
  }

  public void listtoplevel() {
    List<Productcategory> pCategorys = Productcategory.dao.find( "select * from productcategory where level=1" );
    renderJson( pCategorys );
  }
  
  public void listsecondlevel() {
    List<Productcategory> pCategorys = Productcategory.dao.find( "select * from productcategory where level=2" );
    renderJson( pCategorys );
  }

  @Override
  public String getModalName() {
    return "productcategory";
  }

  @Override
  public String getPageHeader() {
    return "产品分类";
  }

  @Override
  public String getToolBarAddButtonTitle() {
    return "添加分类";
  }

  @Override
  public String getIndexHtml() {
    return "productcategory.html";
  }

  @Override
  public int getCategory() {
    return Constant.CATEGORY_OTHER;
  }
}
