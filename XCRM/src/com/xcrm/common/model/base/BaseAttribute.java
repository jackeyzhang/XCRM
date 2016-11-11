package com.xcrm.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAttribute<M extends BaseAttribute<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setAttributeid(java.lang.Integer attributeid) {
		set("attributeid", attributeid);
	}

	public java.lang.Integer getAttributeid() {
		return get("attributeid");
	}

	public void setDisplayname(java.lang.String displayname) {
		set("displayname", displayname);
	}

	public java.lang.String getDisplayname() {
		return get("displayname");
	}

	public void setCategory(java.lang.Integer category) {
		set("category", category);
	}

	public java.lang.Integer getCategory() {
		return get("category");
	}

	public void setValue(java.lang.String value) {
		set("value", value);
	}

	public java.lang.String getValue() {
		return get("value");
	}

	public void setScopetype(java.lang.Integer scopetype) {
		set("scopetype", scopetype);
	}

	public java.lang.Integer getScopetype() {
		return get("scopetype");
	}

	public void setScopevalue(java.lang.String scopevalue) {
		set("scopevalue", scopevalue);
	}

	public java.lang.String getScopevalue() {
		return get("scopevalue");
	}

}