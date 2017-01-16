package com.xcrm.contract;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

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
	
	public void view( ){
	  String id = getPara("id");
	  setAttr("contract", id);
	  this.render( "view.html" );
	}
}
