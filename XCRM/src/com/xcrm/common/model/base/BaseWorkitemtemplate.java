package com.xcrm.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWorkitemtemplate<M extends BaseWorkitemtemplate<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setIndex(java.lang.Integer index) {
		set("index", index);
	}

	public java.lang.Integer getIndex() {
		return get("index");
	}

	public void setUserid(java.lang.Integer userid) {
		set("userid", userid);
	}

	public java.lang.Integer getUserid() {
		return get("userid");
	}

	public void setWeight(java.lang.Integer weight) {
		set("weight", weight);
	}

	public java.lang.Integer getWeight() {
		return get("weight");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setDep(java.lang.Integer dep) {
		set("dep", dep);
	}

	public java.lang.Integer getDep() {
		return get("dep");
	}

}
