package com.xcrm.common.model;

import java.util.List;

import com.xcrm.common.model.base.BaseWorkitem;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Workitem extends BaseWorkitem<Workitem> {
	public static final Workitem dao = new Workitem();
	  public static final int WORKITEM_STATUS_INIT = 0; //待处理
	  public static final int WORKITEM_STATUS_START = 1; //进行中
	  public static final int WORKITEM_STATUS_DONE = 2; //已完成
	  
	public List<Workitemallocation> getWorkitemallocations( ){
	  return Workitemallocation.dao.find( "select wia.*,usr.username workername, (case when wia.status=0 then 'lightblue' when wia.status=1 then 'blue' when wia.status=2 then 'green' else 'gray' end) statuscolor from Workitemallocation wia join user usr on wia.worker= usr.id where wia.status <> 3 and wia.workitem=?", this.getId() );
	}
	
	public Workflow getWorkflowObj( ){
	  return Workflow.dao.findById( this.getWorkflow() );
	}
	
}
