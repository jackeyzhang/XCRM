package com.xcrm.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseOrder<M extends BaseOrder<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setOrderno(java.lang.Long orderno) {
		set("orderno", orderno);
	}

	public java.lang.Long getOrderno() {
		return get("orderno");
	}

	public void setDate(java.util.Date date) {
		set("date", date);
	}

	public java.util.Date getDate() {
		return get("date");
	}

	public void setTotalprice(java.lang.Float totalprice) {
		set("totalprice", totalprice);
	}

	public java.lang.Float getTotalprice() {
		return get("totalprice");
	}

	public void setPrice(java.lang.Float price) {
		set("price", price);
	}

	public java.lang.Float getPrice() {
		return get("price");
	}

	public void setComments(java.lang.String comments) {
		set("comments", comments);
	}

	public java.lang.String getComments() {
		return get("comments");
	}

	public void setPaymenttype(java.lang.Integer paymenttype) {
		set("paymenttype", paymenttype);
	}

	public java.lang.Integer getPaymenttype() {
		return get("paymenttype");
	}

	public void setPaid(java.lang.Float paid) {
		set("paid", paid);
	}

	public java.lang.Float getPaid() {
		return get("paid");
	}

	public void setDeliverytime(java.util.Date deliverytime) {
		set("deliverytime", deliverytime);
	}

	public java.util.Date getDeliverytime() {
		return get("deliverytime");
	}

}
