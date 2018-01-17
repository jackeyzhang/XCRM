/**
 * 
 */
package com.xcrm.work;

import java.util.Date;

import com.xcrm.common.model.Workflow;
import com.xcrm.common.model.Workitem;
import com.xcrm.common.model.Workitemallocation;


/**
 * @author jzhang12
 *
 */
public class WorkflowUtil {
  
  /**
   * 更新workitem和workflow的状态为Done
   * @param wiaid
   * @return
   */
  public static boolean autoFinishWorkflowByWorkitem( int workitemid ) {
    Workitem workitem = Workitem.dao.findById( workitemid );
    workitem.setStatus( Workitem.WORKITEM_STATUS_DONE );
    workitem.setFinishtime( new Date() );
    workitem.update();
    Workflow workflow = workitem.getWorkflowObj();
    for ( Workitem wi : workflow.getWorkItems() ) {
      if ( wi.getStatus() != Workitem.WORKITEM_STATUS_DONE ) {
          return false;
      }
    }
    workflow.setStatus( Workflow.WORK_STATUS_DONE );
    workflow.setFinishtime( new Date() );
    workflow.update();
    return true;
  }
  
  /**
   * 更新workitem和workflow的状态为start
   * @param wiaid
   * @return
   */
  public static boolean autoRestartWorkflowByWorkitem( int workitemid ) {
    Workitem workitem = Workitem.dao.findById( workitemid );
    workitem.setStatus( Workitem.WORKITEM_STATUS_START );
    workitem.setFinishtime( null );
    workitem.update();
    
    Workflow workflow = workitem.getWorkflowObj();
    if(workflow.getStatus() != Workflow.WORK_STATUS_START){
      workflow.setStatus( Workflow.WORK_STATUS_START );
      workflow.setFinishtime( null );
      workflow.update();
    }
    return true;
  }
  

  
  /**
   * 更新workitem和workflow的状态
   * @param wiaid
   * @return
   */
  public static boolean autoFinishWorkflowByWia( int wiaid ) {
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    Workitem workitem = wia.getWorkItem();
    for ( Workitemallocation wiaa : workitem.getWorkitemallocations() ) {
      if ( wiaa.getId() != wiaid && wiaa.getStatus() != Workitemallocation.WORKITEM_STATUS_DONE ) {
        return false;//有一个wia没done 就返回
      }
    }
    workitem.setStatus( Workitem.WORKITEM_STATUS_DONE );
    workitem.setFinishtime( new Date() );
    workitem.update();
    
    Workflow workflow = workitem.getWorkflowObj();
    for ( Workitem wi : workflow.getWorkItems() ) {
      if ( wi.getStatus() != Workitem.WORKITEM_STATUS_DONE ) { //有一个workitem没done 就返回
          return false;
      }
    }
    workflow.setStatus( Workflow.WORK_STATUS_DONE );
    workflow.setFinishtime( new Date() );
    workflow.update();
    return true;
  }
  
  
  /**
   * 更新workitem和workflow的状态到start
   * @param wiaid
   * @return
   */
  public static boolean autoStartWorkflowByWia( int wiaid ) {
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    Workitem workitem = wia.getWorkItem();
    if( workitem.getStatus() == Workitem.WORKITEM_STATUS_INIT ){
      workitem.setStatus( Workitem.WORKITEM_STATUS_START );
      workitem.setStarttime( new Date() );
      workitem.update();
    }
    
    Workflow workflow = workitem.getWorkflowObj();
    if( workflow.getStatus() == Workflow.WORK_STATUS_INIT ){
      workflow.setStatus( Workflow.WORK_STATUS_START );
      workflow.setStarttime( new Date() );
      workflow.update();
    }
    return true;
  }
}
