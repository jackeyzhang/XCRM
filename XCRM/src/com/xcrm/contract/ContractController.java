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
import com.xcrm.common.model.Contract;
import com.xcrm.common.util.Constant;

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
    info.Dataloading( orderno );
    
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

    this.render( "view.html" );
  }
  
  private String formatContractNumber( Integer contractid ){
    NumberFormat format = new DecimalFormat( "0000" );
    return format.format( contractid );
  }

}

class ContractPrintInfo{
  
  private String contract;
  private String contact;
  private String telephone;
  private String address;
  private String orderinfo;
  
  public void Dataloading( Long orderno ) {
    String sql = "select bi.comments,cust.name cname,cust.shiptoAddr,cust.company,cust.phone,cust.contact,bi.price,bi.num,prd.name pname,bi.prdattrs from xcrm.order o"
        + " left join xcrm.orderitem oi on o.id=oi.order"
        + " left join xcrm.bookitem bi on oi.bookitem=bi.id"
        + " left join xcrm.customer cust on cust.id= bi.customer"
        + " left join xcrm.product prd on prd.id=bi.product"
        + " where o.orderno=" + orderno;
    List<Record> records = Db.find( sql );
    
    int i =0;
    StringBuilder sb = new StringBuilder();
    for( Record record : records ){
      if ( i == 0 ) {
        setContract( record.getStr( "company" ) );
        setContact( record.getStr( "cname" ) );
        setTelephone( record.getStr( "phone" ) );
        setAddress( record.getStr( "shiptoAddr" ) );
        i++;
      }
      sb.append( record.getStr( "pname" ) ).append(getSpace(25))
      .append(  formatAttr( record.getStr( "prdattrs" ) ) ).append(getSpace(20))
      .append(  formatNum( record.getNumber( "num" ) )  ).append(getSpace(20))
      .append(  formatPrice( record.getNumber( "price" ))  ).append(getSpace(20))
      .append(  record.getStr( "comments" )  ).append(getSpace(20))
      .append( "<br>" );
    }
    this.setOrderinfo( sb.toString() );
  }
  
  private String getSpace( int num ){
    String result = "";
    for( int i=0; i<num; i++){
      result += "&nbsp";
    }
    return result;
  }
  
  private String formatAttr( String attrValue ){
    String[] attrs =  attrValue.split( "," );
    String result = "";
    for( String attr : attrs ){
      result += attr.split( ":" )[1] +" ";
    }
    result = result.replace( "}", "" ).replace( "\"", "" );
    return result;
  }
  
  private String formatPrice( Number price ){
    NumberFormat format = new DecimalFormat( "￥##,###.00##" );
    return format.format( price );
  }
  
  private String formatNum( Number Num ){
    NumberFormat format = new DecimalFormat( "##.00" );
    return format.format( Num );
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
  
  
}
