package com.xcrm.cart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.JsonKit;
import com.xcrm.common.AbstractController;
import com.xcrm.common.AttributeID;
import com.xcrm.common.model.Attributevalue;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Orderitem;
import com.xcrm.common.model.Priceinventoryvalue;
import com.xcrm.common.model.Product;
import com.xcrm.common.model.Productpic;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.Tuple;

public class CartController extends AbstractController {

	public void index() {
		super.index();
		Product product = Product.dao.findById(this.getPara("pid"));
		List<Productpic> productpics = Productpic.dao.find("select * from productpic where productid=?",
				this.getPara("pid"));
		this.setAttr("prd", product);
		this.setAttr("prdpics", productpics.size() == 0 ? null : productpics);
		setAttr("prdimg_path", getPrdImgBaseUrl());
		setAttr("attrs", getAttrs());
		setAttr("count", count());
		setAttr("prices", JsonKit.toJson(getPrice()));
	}

	private List<Tuple<AttributeID, List<String>>> getAttrs() {
		List<Attributevalue> list = Attributevalue.dao
				.find("select * from attributevalue where category=1 and objectid=?", this.getPara("pid"));
		List<Tuple<AttributeID, List<String>>> attrs = new ArrayList<Tuple<AttributeID, List<String>>>();
		for (Attributevalue attributevalue : list) {
			String attrValue = attributevalue.getValue();
			if (!StringUtils.isEmpty(attrValue)) {
				attrs.add(new Tuple<AttributeID, List<String>>(AttributeID.getById(attributevalue.getAttributeid()),
						Arrays.asList(attrValue.split(Constant.COMMA))));
			}

		}
		return attrs;
	}

	private Map<String, Priceinventoryvalue> getPrice() {
		List<Priceinventoryvalue> list = Priceinventoryvalue.dao.find(
				"select pi.* from priceinventoryvalue pi left join price pr on pi.priceid=pr.id  where pr.product=?",
				this.getPara("pid"));
		Map<String, Priceinventoryvalue> map = new HashMap<String, Priceinventoryvalue>();
		for (Priceinventoryvalue priceinventoryvalue : list) {
			map.put(priceinventoryvalue.getPricekey(), priceinventoryvalue);
		}
		return map;
	}

	public void save() {
	    String editorderflag = this.getRequest().getHeader("Referer" );
	    if( !editorderflag.isEmpty() && editorderflag.contains( "editorder" )){
	      String orderno = editorderflag.substring( editorderflag.lastIndexOf( "=" ) + 1 );
	      System.out.println( orderno );
	      Order order = Order.dao.findFirst(  "select * from xcrm.order where orderno=?", orderno );
	      if(order != null){
	        Orderitem orderitem = new Orderitem();
	        orderitem.setOrder( order.getId() );
	        orderitem.setDate( new Date() );
	        
	        Bookitem bookitem = this.getModel(Bookitem.class, "", true);
            bookitem.setUser(getCurrentUserId());
            bookitem.setStatus(false);
            bookitem.setDate(new Date());
            bookitem.save();
            
            orderitem.setBookitem( bookitem.getId() );
            orderitem.save();
            this.redirect("/editorder?orderno=" + orderno);
	      }
	    }else{
	        Bookitem bookitem = this.getModel(Bookitem.class, "", true);
	        bookitem.setUser(getCurrentUserId());
	        bookitem.setStatus(false);
	        bookitem.setDate(new Date());
	        bookitem.save();
	        this.redirect("/cartlist/");
	    }
		
		
	}

	@Override
	public String getModalName() {
		return "cart";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "返回产品列表";
	}

	@Override
	public String getIndexHtml() {
		return "cart.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_CART;
	}
}
