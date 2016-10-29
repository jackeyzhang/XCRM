package com.xcrm.index;

import com.jfinal.core.Controller;


/**
 * IndexController
 */
public class IndexController extends Controller {

  public void index() {
    render( "index.html" );
  }
  
  public void logout(){
    this.forwardAction( "/index" );
  }
}
