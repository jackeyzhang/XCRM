package com.xcrm.common.config;

import java.util.Properties;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.FreeMarkerRender;
import com.xcrm.attribute.AttributeController;
import com.xcrm.attribute.AttributeInterceptor;
import com.xcrm.attribute.AttributeidController;
import com.xcrm.bonus.BonusController;
import com.xcrm.cart.CartController;
import com.xcrm.cart.CartlistController;
import com.xcrm.cart.PaymentController;
import com.xcrm.cart.SingePaymentController;
import com.xcrm.common.model._MappingKit;
import com.xcrm.common.util.Constant;
import com.xcrm.contract.ContractController;
import com.xcrm.customer.CustomerController;
import com.xcrm.department.DepartmentController;
import com.xcrm.index.IndexController;
import com.xcrm.login.LoginController;
import com.xcrm.login.LoginInterceptor;
import com.xcrm.order.CancelOrderController;
import com.xcrm.order.EditOrderController;
import com.xcrm.order.OrderController;
import com.xcrm.order.OrderViewController;
import com.xcrm.product.PriceController;
import com.xcrm.product.ProductController;
import com.xcrm.product.ProductcategoryController;
import com.xcrm.report.ReportController;
import com.xcrm.salary.SalaryController;
import com.xcrm.salesseason.SalesSeasonController;
import com.xcrm.schedule.ScheduleController;
import com.xcrm.search.SearchController;
import com.xcrm.store.StoreController;
import com.xcrm.user.UserController;
import com.xcrm.work.WorkflowController;
import com.xcrm.work.WorkflowtemplateController;


/**
 * API引导式配置
 */
public class XCRMConfig extends JFinalConfig {

  /**
   * 配置常量
   */
  public void configConstant(Constants me) {
    // 加载少量必要配置，随后可用PropKit.get(...)获取值
    PropKit.use("xcrm.properties");
    me.setDevMode(PropKit.getBoolean("devMode", false));
    me.setBaseUploadPath(Constant.TEMP_IMG);
    me.setEncoding("UTF-8");
  }

  /**
   * 配置路由
   */
  public void configRoute(Routes me) {
    me.add("/", IndexController.class, "/index"); // 第三个参数为该Controller的视图存放路径
    me.add("/login", LoginController.class);
    me.add("/user", UserController.class);
    me.add("/customer", CustomerController.class);
    me.add("/product", ProductController.class);
    me.add("/price", PriceController.class);
    me.add("/schedule", ScheduleController.class);
    me.add("/store", StoreController.class);
    me.add("/attributeid", AttributeidController.class);
    me.add("/attribute", AttributeController.class);
    me.add("/productcategory", ProductcategoryController.class);
    me.add("/contract", ContractController.class);
    me.add("/search", SearchController.class);
    me.add("/cart", CartController.class);
    me.add("/cartlist", CartlistController.class);
    me.add("/payment", PaymentController.class);
    me.add("/order", OrderController.class);
    me.add("/orderview", OrderViewController.class);
    me.add("/spayment", SingePaymentController.class);
    me.add("/report", ReportController.class);
    me.add("/cancelorder", CancelOrderController.class);
    me.add("/editorder", EditOrderController.class);
    me.add("/salesseason", SalesSeasonController.class);
    me.add("/department", DepartmentController.class);
    me.add("/workflow", WorkflowController.class);
    me.add("/workflowtemplate", WorkflowtemplateController.class);
    me.add("/salary", SalaryController.class);
    me.add("/bonus", BonusController.class);
  }

  public static C3p0Plugin createC3p0Plugin() {
    return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"),
        PropKit.get("password").trim());
  }

  /**
   * 配置插件
   */
  public void configPlugin(Plugins me) {
    // 配置C3p0数据库连接池插件
    C3p0Plugin C3p0Plugin = createC3p0Plugin();
    me.add(C3p0Plugin);

    // 配置ActiveRecord插件
    ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
    me.add(arp);

    // 所有配置在 MappingKit 中搞定
    _MappingKit.mapping(arp);
  }

  /**
   * 配置全局拦截器
   */
  public void configInterceptor(Interceptors me) {
    me.add(new LoginInterceptor());
    me.add(new AttributeInterceptor());
  }

  /**
   * 配置处理器
   */
  public void configHandler(Handlers me) {

  }

  public void afterJFinalStart() {
    Properties p = loadPropertyFile("freemarker.properties");
    FreeMarkerRender.setProperties(p);
  }

  /**
   * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
   */
  public static void main(String[] args) {
    JFinal.start("WebRoot", 8088, "/", 5);
  }
}
