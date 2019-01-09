package com.xcrm.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSalary<M extends BaseSalary<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setProduct(java.lang.Integer product) {
		set("product", product);
	}

	public java.lang.Integer getProduct() {
		return get("product");
	}

	public void setWorkflowtemplateid(java.lang.Integer workflowtemplateid) {
		set("workflowtemplateid", workflowtemplateid);
	}

	public java.lang.Integer getWorkflowtemplateid() {
		return get("workflowtemplateid");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setBaseamount(java.lang.Double baseamount) {
		set("baseamount", baseamount);
	}

	public java.lang.Double getBaseamount() {
		return get("baseamount");
	}

}
