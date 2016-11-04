package com.xcrm.index;

import com.jfinal.core.Controller;


/**
 * IndexController
 */
public class IndexController extends Controller {

  public void index() {
    setAttr("model", "index");
    this.setAttr( "login_error", "" );
    this.setAttr( "nameMsg", "" );
    this.setAttr( "passMsg", "" );
    render( "index.html" );
  }
  
  public void logout(){
    this.getSession().invalidate();
    this.redirect( "/index" );
  }
}
