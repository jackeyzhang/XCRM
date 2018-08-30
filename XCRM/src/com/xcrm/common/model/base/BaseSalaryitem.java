package com.xcrm.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSalaryitem<M extends BaseSalaryitem<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setSalaryid(java.lang.Integer salaryid) {
		set("salaryid", salaryid);
	}

	public java.lang.Integer getSalaryid() {
		return get("salaryid");
	}

	public void setDep(java.lang.Integer dep) {
		set("dep", dep);
	}

	public java.lang.Integer getDep() {
		return get("dep");
	}

	public void setAmount(java.lang.Double amount) {
		set("amount", amount);
	}

	public java.lang.Double getAmount() {
		return get("amount");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

}
