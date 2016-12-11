/**
 * 
 */
package com.xcrm.common.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.xcrm.common.AttributeFinder;

/**
 * @author jzhang12
 *
 */
public class Book extends Model<Book> {

	private static final long serialVersionUID = 1946424759277934855L;

	public static Book dao = new Book();
	
	public static AttributeFinder afinder = AttributeFinder.getInstance();

	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
}
