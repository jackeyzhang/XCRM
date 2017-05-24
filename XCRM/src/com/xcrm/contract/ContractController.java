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
    
    this.setAttr( "Bcontract", "上海曼可服饰有限公司" );
    this.setAttr( "Bcontact", "王有锋" );
    this.setAttr( "Btelephone", "021 67868791" );
    this.setAttr( "Baddress", "上海市松江区九亭镇九谊路399号四楼" );
    
    this.setAttr( "orderiteminfo", info.getOrderinfo() );
    this.setAttr( "paymentinfo", "请于本合同签订之日支付款项50%，本合同签订之日起7日内付清剩余款项。" );
    this.setAttr( "accountinfo", "王锋<br> 6222081001018679971 <br> 中国工商银行<br>" );
    
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
      sb.append( getFixlengthStrWithSpace(record.getStr( "pname" ), 35 ) )
      .append(  getFixlengthStrWithSpace( formatAttr( record.getStr( "prdattrs" ) ), 25 ) )
      .append(  getFixlengthStrWithSpace( StrUtil.formatNum( record.getNumber( "num" ) ) ,25) )
      .append(  getFixlengthStrWithSpace( StrUtil.formatPrice( record.getNumber( "price" )), 25 )  )
      .append(  getFixlengthStrWithSpace( record.getStr( "comments" ) == null ? "" : record.getStr( "comments" ), 20)  )
      .append( "<br>" );
    }
    this.setOrderinfo( sb.toString() );

  }
  
  private String getFixlengthStrWithSpace( String result, int num ) {
    int spacelength = num - result.length();
    for ( int i = 0; i < spacelength; i++ ) {
      result += "&nbsp";
    }
    return result;
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
  
}
