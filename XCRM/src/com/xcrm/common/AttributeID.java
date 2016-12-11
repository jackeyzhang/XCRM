/**
 * 
 */
package com.xcrm.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xcrm.common.model.Attributevalue;

/**
 * @author jzhang12
 *
 */
public class AttributeID {

	public static final int TYPE_NUMBER = 1, TYPE_LIST_SINGLE = 2, TYPE_LIST_MULTIPLE = 3, TYPE_TEXT = 4;
	public static Map<Integer, AttributeID> map = new HashMap<Integer, AttributeID>();
	/**
	 * 0 - 100 预留for与业务无关的attribute
	 */
	public static final AttributeID PROVINCE = new AttributeID(010, "省/市", TYPE_LIST_SINGLE);
	public static final AttributeID CITY = new AttributeID(011, "城市", TYPE_LIST_SINGLE);
	public static final AttributeID AREA = new AttributeID(012, "区县", TYPE_LIST_SINGLE);

	/**
	 * from 101, extend for business attribute
	 * 
	 * '101', '颜色', '2', '红色,蓝色,绿色,白色,紫色,卡其色' '102', '型号', '2', 'XXL,XL,L,M,S'
	 * '103', '数量单位', '2', '件,个' '104', '时间单位', '2', '天,小时' '105', '数量', '1',
	 * NULL '106', '交货期', '1', NULL
	 */
	public static final AttributeID COLOR = new AttributeID(101, "颜色", TYPE_LIST_MULTIPLE);
	public static final AttributeID INVENTORY = new AttributeID(102, "型号", TYPE_LIST_MULTIPLE);
	public static final AttributeID NUM_UNIT = new AttributeID(103, "数量单位", TYPE_LIST_SINGLE);
	public static final AttributeID TIME_UNIT = new AttributeID(104, "时间单位", TYPE_LIST_SINGLE);
	public static final AttributeID NUM = new AttributeID(105, "数量", TYPE_NUMBER);
	public static final AttributeID PERIOD = new AttributeID(106, "交货期", TYPE_NUMBER);

	/**
	 * from 201, extend for script-in attribute
	 * 
	 * '201', '部门', '2', '礼服部,摄像部,化妆部,选片部,设计部,取件部,客服部' '202', '工作部门', '3',
	 * '礼服部,摄像部,化妆部,选片部,设计部,取件部,客服部' '203', '产品类型', '2', '婚纱,礼服' '204', '产品面料',
	 * '2', '白沙,蕾丝' '205', '产品颜色', '3', '红色,黄色,蓝色,白色,粉色,紫色' '206', '产品尺码', '2',
	 * 'S,M,L,XL,XXL'
	 */
	public static final AttributeID DEPARTMENT = new AttributeID(201, "部门", TYPE_LIST_SINGLE);
	public static final AttributeID PRD_DEPARTMENT = new AttributeID(202, "工作部门", TYPE_LIST_MULTIPLE);
	public static final AttributeID PRD_CATEGORY = new AttributeID(203, "产品类型", TYPE_LIST_MULTIPLE);
	public static final AttributeID PRD_MATERIAL = new AttributeID(204, "产品面料", TYPE_LIST_MULTIPLE);
	public static final AttributeID PRD_COLOR = new AttributeID(205, "产品颜色", TYPE_LIST_MULTIPLE);
	public static final AttributeID PRD_SIZE = new AttributeID(206, "产品尺码", TYPE_LIST_MULTIPLE);

	private int id;
	private String name;
	private int type;

	public AttributeID(int id, String name, int type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static String getValue(List<Attributevalue> attributevalues, AttributeID attributeid) {
		for (Attributevalue av : attributevalues) {
			if (av.getAttributeid() == attributeid.getId()) {
				return av.getValue();
			}
		}
		return "";
	}

	static {
		try {
			String clazzName = AttributeID.class.getName();
			Class clazz = Class.forName(clazzName);
			Field[] fields = clazz.getFields();

			for (Field field : fields) {
				if (clazzName.equals(field.getType().getName())) {
					AttributeID attr = (AttributeID) field.get(clazz);
					map.put(attr.getId(), attr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static AttributeID getById(int id) {
		return map.get(id);
	}
}
