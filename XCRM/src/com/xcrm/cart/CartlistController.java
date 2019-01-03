package com.xcrm.cart;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.util.Constant;

public class CartlistController extends AbstractController {

	public void index() {
		super.index();
		List<Record> list = Db.find(
				"select bi.id,bi.discount, bi.num num,bi.price price,bi.additionfee afee,bi.product pid,p.name name,bi.prdattrs attrs,bi.comments comments,GROUP_CONCAT(pic.fielname) filename from bookitem bi join product p on bi.product=p.id left join productpic pic on pic.productid=p.id where  bi.user=? and bi.status=0 group by bi.id, bi.num,bi.price,bi.product,p.name",
				getCurrentUserId());
		fillProductAttributesInOrderCart(list, "attrs");
		setAttr("list", list);
		setAttr("prdimg_path", getPrdImgBaseUrl());
		if(this.getSessionAttr( "totaldiscount" ) != null){
		  setAttr("discount", this.getSessionAttr( "totaldiscount" ));
		}else{
		  setAttr("discount", 100);
		}
	}
	
	public void gotoPayment(){
	  this.setSessionAttr( "orderno", this.getPara("orderno") );
	  this.setSessionAttr( "ordercomments", this.getPara( "ordercomments" ) );
	  this.setSessionAttr( "bookitems", this.getPara( "ids" ) );
	  this.setSessionAttr( "totaldiscount", this.getPara( "totaldiscount" ) );//总折扣
	  this.setSessionAttr( "amount", this.getPara( "amount" ) );//原价
	  this.setSessionAttr( "price", this.getPara( "price" ) ); //成交价
	  this.setSessionAttr( "taxrate", this.getPara( "taxrate" ) ); //税率
	  this.renderNull();
	}
	
	public void discount(){
	  Bookitem bi = Bookitem.dao.findById( this.getPara("id") );
	  bi.setDiscount( this.getParaToInt( "discount" ) );
	  bi.update();
      renderNull();
	}
	
	public void totaldiscount(){
	  this.setSessionAttr( "totaldiscount", this.getPara( "discount" ) );//总折扣
      renderNull();
    }
	
  public void changebookitemprice() {
    Bookitem bi = Bookitem.dao.findById( this.getPara( "id" ) );
    try{
      Long aFee = Long.parseLong( this.getPara( "addprice" ) );
      BigDecimal additionalFee = BigDecimal.valueOf(aFee);
      bi.setAdditionfee( additionalFee );
      bi.update();
    }catch( Exception e){
      
    }
    renderNull();
  }

	public void save() {
		String ids = this.getPara("ids");
		//update status to active
		Db.update("update bookitem set status=1 where id in (" + ids + ") ");
		// add comment per book items
		String comments = this.getPara("comments");
		if (comments != null && !comments.isEmpty()) {
			String[] commentArray = comments.split(",");
			for (String comment : commentArray) {
				String[] bookitmeIDAndComment = comment.split("=");
				if (bookitmeIDAndComment.length < 2)
					continue;
				String bookitemid = bookitmeIDAndComment[0].replace("comments-", "");
				Db.update("update bookitem set comments='" + bookitmeIDAndComment[1] + "' where id = " + bookitemid);
			}
		}
		renderNull();
	}

	public void updatecomments() {
		String id = this.getPara("id");
		String comments = this.getPara("comments");
		if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(id)) {
			Db.update("update bookitem set comments=? where id =?", comments, id);
		}
		renderNull();
	}


	public void remove() {
		Bookitem.dao.deleteById(this.getPara("id"));
		renderNull();
	}
	
	public void cancel() {
	   Bookitem bookitem =  Bookitem.dao.findById( this.getPara("id") );
	   bookitem.setStatus( Bookitem.STATUS_CANCELLED );
	   bookitem.update();
	   renderNull();
	}
	
	public void resume(){
  	   Bookitem bookitem =  Bookitem.dao.findById( this.getPara("id") );
       bookitem.setStatus( Bookitem.STATUS_ACTIVE );
       bookitem.update();
       renderNull();
	}

	@Override
	public String getModalName() {
		return "cartlist";
	}

	@Override
	public String getPageHeader() {
		return "我的购物车";
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
