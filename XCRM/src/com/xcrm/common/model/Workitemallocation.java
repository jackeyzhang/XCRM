package com.xcrm.common.model;

import com.xcrm.common.model.base.BaseWorkitemallocation;

@SuppressWarnings("serial")
public class Workitemallocation extends BaseWorkitemallocation<Workitemallocation> {
	public static final Workitemallocation dao = new Workitemallocation();
	
    public static final int WORKITEM_STATUS_INIT = 0; //待处理
    public static final int WORKITEM_STATUS_START = 1; //进行中
    public static final int WORKITEM_STATUS_DONE = 2; //已完成
    public static final int WORKITEM_STATUS_CANCEL = 3; //已取消
    public static final int WORKITEM_STATUS_HOLD = 4; //已挂起
	
	public Workitem getWorkItem( ){
	  return Workitem.dao.findById( this.getWorkitem() );
	}
}
