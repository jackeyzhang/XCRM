package com.xcrm.common.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.jfinal.kit.PathKit;

public class PropertiesUtil {
	private static PropertiesConfiguration config = null;
	public static final String PRODUCT_IMG_MAXSIZE = "product.img.maxsize";
	public static final String PRODUCT_IMG_PATH = "product.img.path";
	static {
		try {
			config = new PropertiesConfiguration(PathKit.getWebRootPath() + "/WEB-INF/xcrm.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStr(String key) {
		return config.getString(key);
	}

	public static int getInt(String key) {
		return config.getInt(key);
	}

	public static int getProductImgMaxSize() {
		return getInt(PRODUCT_IMG_MAXSIZE);
	}

	public static String getProductImgPath() {
		return getStr(PRODUCT_IMG_PATH);
	}

}
