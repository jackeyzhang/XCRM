package com.xcrm.cart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Order;
import com.xcrm.common.model.Orderitem;
import com.xcrm.common.util.Constant;

public class CartlistController extends AbstractController {

	public void index() {
		super.index();
		List<Record> list = Db.find(
				"select bi.id, bi.num num,bi.price price,bi.product pid,p.name name,bi.comments comments,GROUP_CONCAT(pic.fielname) filename from bookitem bi left join product p on bi.product=p.id left join productpic pic on pic.productid=p.id where  bi.user=? and bi.status=0 group by bi.id, bi.num,bi.price,bi.product,p.name",
				getCurrentUserId());
		setAttr("list", list);
		setAttr("prdimg_path", getPrdImgBaseUrl());
	}

	public void save() {
		String ids = this.getPara("ids");
		Db.update("update bookitem set status=1 where id in (" + ids + ") ");
		// add comment per book items
		String comments = this.getPara("comments");
		if(comments != null && !comments.isEmpty()){
		  String[] commentArray = comments.split( "," );
		  for( String comment : commentArray){
		    String[] bookitmeIDAndComment = comment.split( "=" );
		    if(bookitmeIDAndComment.length < 2) continue;
		    String bookitemid = bookitmeIDAndComment[0].replace( "comments-", "" );
		    Db.update("update bookitem set comments='"+ bookitmeIDAndComment[1] +"' where id = " + bookitemid);
		  }
		}
		saveOrder();
		this.redirect("/order/");
	}

	public void saveOrder() {
		String ids = this.getPara("ids");
		String[] idarray = ids.split(",");
		Order order = new Order();
		Date date = new Date();
		order.setDate(date);
		order.setOrderno(date.getTime());
	    // persist price
        String price = this.getPara("price");
        if( price != null && !price.isEmpty()){
          order.setPrice( Float.parseFloat( price ) );
        }
        // persist amount
        String amount = this.getPara("amount");
        if( amount != null && !amount.isEmpty()){
          order.setTotalprice(  Float.parseFloat( amount )  );
        }
       // persist comments
        String ordercomments = this.getPara("ordercomments");
        if( ordercomments != null && !ordercomments.isEmpty()){
          order.setComments( ordercomments );
        }
		order.save();
		List<Orderitem> orderitems = new ArrayList<Orderitem>();
		for (String id : idarray) {
			Orderitem orderitem = new Orderitem();
			orderitem.setBookitem(Integer.valueOf(id));
			orderitem.setDate(date);
			orderitem.setOrder(order.getId());
			orderitems.add(orderitem);
		}
		Db.batchSave(orderitems, orderitems.size());
	}

	public void remove() {
		Bookitem.dao.deleteById(this.getPara("id"));
		renderNull();
	}

	@Override
	public String getModalName() {
		return "cartlist";
	}

	@Override
	public String getPageHeader() {
		return "订单管理";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "继续购买";
	}

	@Override
	public String getIndexHtml() {
		return "cartlist.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_CARTLIST;
	}
}
