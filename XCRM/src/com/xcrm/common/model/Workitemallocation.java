package com.xcrm.common.model;

import com.xcrm.common.model.base.BaseWorkitemallocation;

@SuppressWarnings("serial")
public class Workitemallocation extends BaseWorkitemallocation<Workitemallocation> {
	public static final Workitemallocation dao = new Workitemallocation();
	
	public Workitem getWorkItem( ){
	  return Workitem.dao.findById( this.getWorkitem() );
	}
}
