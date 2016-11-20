package com.xcrm.common.util;

import com.jfinal.kit.PropKit;

public class PropUtil {

  public static int getPrdImgMaxSize() {
    return PropKit.getInt("product.img.maxsize");
  }

  public static String getPrdImgPath() {
    return PropKit.get("product.img.path");
  }

  public static String getPrdQr2Path() {
    return PropKit.get("product.qr2.path");
  }

  public static boolean isDevMode() {
    return PropKit.getBoolean("devMode");
  }

  public static String getImgDomain() {
    return PropKit.get("img.domain");
  }

}
