/**
 * 
 */
package com.xcrm.work;

import com.xcrm.common.model.Workflow;
import com.xcrm.common.model.Workitem;
import com.xcrm.common.model.Workitemallocation;


/**
 * @author jzhang12
 *
 */
public class WorkflowUtil {

  public static boolean autoCloseWorkflow( int wiaid ) {
    Workitemallocation wia = Workitemallocation.dao.findById( wiaid );
    Workitem workitem = wia.getWorkItem();
    for ( Workitemallocation wiaa : workitem.getWorkitemallocations() ) {
      if ( wiaa.getId() != wiaid && wiaa.getStatus() != Workitemallocation.WORKITEM_STATUS_DONE ) {
        return false;
      }
    }
    workitem.setStatus( workitem.WORKITEM_STATUS_DONE );
    workitem.save();
    Workflow workflow = workitem.getWorkflowObj();
    for ( Workitem wi : workflow.getWorkItems() ) {
      if( wi.getWorkitemallocations().size() == 0 ){
        return false;
      }
      if ( wi.getStatus() != Workitem.WORKITEM_STATUS_DONE ) {
          return false;
      }
    }
    workflow.setStatus( Workflow.WORK_STATUS_DONE );
    workflow.save();
    return true;
  }
}
