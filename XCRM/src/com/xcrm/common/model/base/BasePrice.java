package com.xcrm.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePrice<M extends BasePrice<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setProduct(java.lang.Integer product) {
		set("product", product);
	}

	public java.lang.Integer getProduct() {
		return get("product");
	}

	public void setStore(java.lang.Integer store) {
		set("store", store);
	}

	public java.lang.Integer getStore() {
		return get("store");
	}

	public void setEnable(java.lang.Integer enable) {
		set("enable", enable);
	}

	public java.lang.Integer getEnable() {
		return get("enable");
	}

	public void setDefault(java.lang.Integer _default) {
		set("default", _default);
	}

	public java.lang.Integer getDefault() {
		return get("default");
	}

	public void setPrice(java.lang.Float price) {
		set("price", price);
	}

	public java.lang.Float getPrice() {
		return get("price");
	}

	public void setCreatetime(java.util.Date createtime) {
		set("createtime", createtime);
	}

	public java.util.Date getCreatetime() {
		return get("createtime");
	}

	public void setCreateuser(java.lang.Integer createuser) {
		set("createuser", createuser);
	}

	public java.lang.Integer getCreateuser() {
		return get("createuser");
	}

	public void setUpdatetime(java.util.Date updatetime) {
		set("updatetime", updatetime);
	}

	public java.util.Date getUpdatetime() {
		return get("updatetime");
	}

	public void setUpdateuser(java.lang.Integer updateuser) {
		set("updateuser", updateuser);
	}

	public java.lang.Integer getUpdateuser() {
		return get("updateuser");
	}

}
