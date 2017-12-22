package com.xcrm.common.model;

import java.util.List;

import com.xcrm.common.model.base.BaseWorkitemallocation;
import com.xcrm.common.util.StrUtil;

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
	
	public List<Workitemallocation> getAllWorkitemallocationsNotFinish( int userid ){
	  return Workitemallocation.dao.find( "select wia.*,bi.prdattrs prdattr,bi.comments bicomments, prd.name prdname,ord.orderno orderno,date_format(ord.deliverytime,'%Y-%m-%d') deliverytime,wf.index wfindex,"
	      + "(select ppic.fielname from productpic ppic where ppic.productid = prd.id limit 1) filename,prd.id prdid,bi.num,dep.name depname "
	      + "from Workitemallocation wia "
	      + "left join workitem wi on wi.id=wia.workitem "
	      + "left join workflow wf on wi.workflow=wf.id "
	      + "left join bookitem bi on bi.id=wf.bookitem "
	      + "left join orderitem oi on wf.bookitem=oi.bookitem "
	      + "left join `order` ord on ord.id=oi.order "
	      + "left join product prd on prd.id=bi.product "
	      + "left join department dep on dep.id=wi.dep "
	      + " where wia.worker=? and wia.status != 2 order by ord.deliverytime asc,wia.starttime desc", userid );
	}
	
	public List<Workitemallocation> getAllWorkitemallocationsIsFinish( int userid, String startDate, String endDate ){
	      return Workitemallocation.dao.find( "select wia.*,bi.prdattrs prdattr,bi.comments bicomments, prd.name prdname,ord.orderno orderno,date_format(ord.deliverytime,'%Y-%m-%d') deliverytime,wf.index wfindex,"
	          + "(select ppic.fielname from productpic ppic where ppic.productid = prd.id limit 1) filename,prd.id prdid,bi.num,dep.name depname "
	          + "from Workitemallocation wia "
	          + "left join workitem wi on wi.id=wia.workitem "
	          + "left join workflow wf on wi.workflow=wf.id "
	          + "left join bookitem bi on bi.id=wf.bookitem "
	          + "left join orderitem oi on wf.bookitem=oi.bookitem "
	          + "left join `order` ord on ord.id=oi.order "
	          + "left join product prd on prd.id=bi.product "
	          + "left join department dep on dep.id=wi.dep "
	          + " where wia.worker=? and wia.status = 2 "
	          + ( StrUtil.isEmpty( startDate ) ? "" : " and wia.finishtime >= str_to_date('" + startDate + "','%Y-%m')")
	          + ( StrUtil.isEmpty( endDate ) ? "" : " and wia.finishtime < str_to_date('" + endDate + "','%Y-%m')")
	          + " order by wia.finishtime desc ", userid );
	    }
	   
	 public List<Workitemallocation> getAllWorkitemallocationsNotFinishBySerachWord( int userid, String searchWord ){
       return Workitemallocation.dao.find( "select wia.*,bi.prdattrs prdattr,bi.comments bicomments, prd.name prdname,ord.orderno orderno,date_format(ord.deliverytime,'%Y-%m-%d') deliverytime,wf.index wfindex,"
         + "(select ppic.fielname from productpic ppic where ppic.productid = prd.id limit 1) filename,prd.id prdid,bi.num,dep.name depname "
         + "from Workitemallocation wia "
         + "left join workitem wi on wi.id=wia.workitem "
         + "left join workflow wf on wi.workflow=wf.id "
         + "left join bookitem bi on bi.id=wf.bookitem "
         + "left join orderitem oi on wf.bookitem=oi.bookitem "
         + "left join `order` ord on ord.id=oi.order "
         + "left join product prd on prd.id=bi.product "
         + "left join department dep on dep.id=wi.dep "
         + " where wia.worker = " + userid
         + " and wia.status != 2 "
         + " and ( prd.name like '%" + searchWord + "%' "
         + " or ord.orderno like '%"+ searchWord + "%' ) order by ord.deliverytime asc,wia.starttime desc");
       }
	 
	 public boolean exisitingWorkitemallocation( int workflowid,int userid, int depid ){
	   return !Workitemallocation.dao.find( "select wia.* "
	       + "from Workitemallocation wia "
	       + "join workitem wi on wi.id=wia.workitem "
	       + "where wi.dep=? and wia.worker=? and wi.workflow=?",
	       depid, userid, workflowid).isEmpty();
	 }
}
