package com.xcrm.contract;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xcrm.common.AbstractController;
import com.xcrm.common.model.Bookitem;
import com.xcrm.common.model.Contract;
import com.xcrm.common.util.Constant;
import com.xcrm.common.util.PropUtil;
import com.xcrm.common.util.StrUtil;

public class ContractController extends AbstractController {

	public void save() {
		String name = getPara("name");
		String editorValue = getPara("editorValue");
		Contract contract = new Contract();
		contract.set("name", name).save();
		String filename = getContractTemplatePath() + contract.getId() + ".html";
		try {
			FileUtils.write(new File(filename), editorValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
		redirect("/" + getModalName() + "/");
	}

	public void update() {
		Integer id = getParaToInt("id");
		String name = getPara("name");
		String editorValue = getPara("editorValue");
		Contract contract = new Contract();
		contract.set("id", id).set("name", name).update();
		String filename = getContractTemplatePath() + contract.getId() + ".html";
		try {
			FileUtils.write(new File(filename), editorValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
		redirect("/" + getModalName() + "/");
	}

	public void preadd() {
		String id = getPara("id");
		if (!StringUtils.isEmpty(id)) {
			Contract contract = Contract.dao.findById(id);
			String filename = getContractTemplatePath() + contract.getId() + ".html";
			setAttr("id", contract.getId());
			setAttr("name", contract.getName());
			try {
				setAttr("editorValue", FileUtils.readFileToString(new File(filename)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		render("/contract/add.html");
	}

	public void remove() {
		Contract.dao.deleteById(this.getParaToInt(0));
		forwardIndex();
	}

	@Override
	public String getModalName() {
		return "contract";
	}

	@Override
	public String getPageHeader() {
		return "创建或修改合同相关信息";
	}

	@Override
	public String getToolBarAddButtonTitle() {
		return "创建合同";
	}

	@Override
	public String getIndexHtml() {
		return "contract.html";
	}

	@Override
	public int getCategory() {
		return Constant.CATEGORY_USER;
	}
	
  public void view() {
    Integer contractid = this.getParaToInt( "id" );
    this.setAttr( "contract", contractid );
    this.setAttr( "number", formatContractNumber( contractid ) );
    
    Long orderno = this.getParaToLong( "orderno" );
    ContractPrintInfo info = new ContractPrintInfo();
    info.loaddata( orderno );
    
    this.setAttr( "Acontract", info.getContract() );
    this.setAttr( "Acontact", info.getContact() );
    this.setAttr( "Atelephone", info.getTelephone() );
    this.setAttr( "Aaddress", info.getAddress() );
    
    this.setAttr( "Bcontract", PropUtil.getContractConfig( "compnayname" ) );
    this.setAttr( "Bcontact", PropUtil.getContractConfig( "compnayman" ) );
    this.setAttr( "Btelephone", PropUtil.getContractConfig( "compnaytel" ) );
    this.setAttr( "Baddress", PropUtil.getContractConfig( "compnayaddress" ) );
    
    this.setAttr( "orderiteminfo", info.getOrderinfo() );
    this.setAttr( "paymentinfo", PropUtil.getContractConfig( "paymentinfo" ) );
    this.setAttr( "accountinfo", PropUtil.getContractConfig( "paymentaccount" ) );
    
    this.setAttr( "amount", info.getAmount());
    this.setAttr( "discount", info.getDiscount() );
    this.setAttr( "paid", info.getPaid() );
    
    this.render( "view.html" );
  }
  
  private String formatContractNumber( Integer contractid ){
    NumberFormat format = new DecimalFormat( "0000" );
    return format.format( contractid );
  }
  
  public void wxlistallcontracts( ){
    List<Record> records = Db.find( "select name, id  from contract" );
    this.renderJson( records );
  }

}

class ContractPrintInfo{
  
  private String contract;
  private String contact;
  private String telephone;
  private String address;
  private String orderinfo;
  
  private String amount;
  private String discount;
  private String paid;
  
  public void loaddata( Long orderno ) {
    String sql = "select bi.comments,cust.name cname,cust.shiptoAddr,cust.company,cust.phone,cust.contact,o.price shouldpay,bi.price price,o.totalprice,o.price/o.totalprice*100 discount,bi.num,prd.name pname,bi.prdattrs "
        + " from `order` o"
        + " left join orderitem oi on o.id=oi.order"
        + " left join bookitem bi on oi.bookitem=bi.id"
        + " left join customer cust on cust.id= bi.customer"
        + " left join product prd on prd.id=bi.product"
        + " where o.orderno=" + orderno 
        + " and bi.status !=" + Bookitem.STATUS_CANCELLED;
    List<Record> records = Db.find( sql );
    
    int i =0;
    StringBuilder sb = new StringBuilder();
   
    for( Record record : records ){
      if ( i == 0 ) {
        setContract( record.getStr( "company" ) );
        setContact( record.getStr( "cname" ) );
        setTelephone( record.getStr( "phone" ) );
        setAddress( record.getStr( "shiptoAddr" ) );
        setAmount( "" + StrUtil.formatPrice( record.getBigDecimal( "totalprice" )) );
        setPaid( "" + StrUtil.formatPrice( record.getBigDecimal( "shouldpay" ) ) );
        setDiscount( "" + StrUtil.formatPercentage( "" + record.getBigDecimal( "discount" ) ) );
        i++;
      }
      sb.append( "<tr>" )
      .append( getTD( 182, record.getStr( "pname" ) ))
      .append( getTD( 164, formatAttr( record.getStr( "prdattrs" ) ) ))
      .append( getTD( 112, StrUtil.formatNum( record.getNumber( "num" ) ) ))
      .append( getTD( 134, StrUtil.formatPrice( record.getNumber( "price" )) ))
      .append( getTD( 452, record.getStr( "comments" ) == null ? "" : record.getStr( "comments" ) ))
      .append( "</tr>" );
    }
    this.setOrderinfo( sb.toString() );

  }
  
  /**
   * format attribute value
   * 
   * e.g. {"204":"白沙","205":"黄色","206":"S"}
   * 
   * @param attrValue
   * @return 白沙 黄色 S
   */
  private String formatAttr( String attrValue ){
    if(attrValue.contains( "-" )){
      return attrValue.replace( "-", " " );
    }
    String[] attrs =  attrValue.split( "," );
    String result = "";
    for( String attr : attrs ){
      result += attr.split( ":" )[1] +" ";
    }
    result = result.replace( "}", "" ).replace( "\"", "" );
    return result;
  }
  
  
  public String getContract() {
    return contract;
  }
  
  public void setContract( String contract ) {
    this.contract = contract;
  }
  
  public String getContact() {
    return contact;
  }
  
  public void setContact( String contact ) {
    this.contact = contact;
  }
  
  public String getTelephone() {
    return telephone;
  }
  
  public void setTelephone( String telephone ) {
    this.telephone = telephone;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress( String address ) {
    this.address = address;
  }
  
  public String getOrderinfo() {
    return orderinfo;
  }
  
  public void setOrderinfo( String orderinfo ) {
    this.orderinfo = orderinfo;
  }

  
  public String getAmount() {
    return amount;
  }

  
  public void setAmount( String amount ) {
    this.amount = amount;
  }

  
  public String getDiscount() {
    return discount;
  }

  
  public void setDiscount( String discount ) {
    this.discount = discount;
  }

  
  public String getPaid() {
    return paid;
  }

  
  public void setPaid( String paid ) {
    this.paid = paid;
  }
  
//  public String getTR( ){
//    StringBuilder tr = new StringBuilder();
//    tr.append( "<tr>" )
//    .append( getTD( 182, "商品信息") )
//    .append( getTD( 164, "型号颜色大小") )
//    .append( getTD( 112, "数量") )
//    .append( getTD( 134, "单价") )
//    .append( getTD( 452, "商品备注") );
//    tr.append( "</tr>" );
//    return tr.toString();
//  }
//  
  public String getTD( int width, String text ){
    return "<td width=\""+ width +"\" valign=\"top\" style=\"word-break: break-all;\">"+ text +"</td>";
  }

  
}
