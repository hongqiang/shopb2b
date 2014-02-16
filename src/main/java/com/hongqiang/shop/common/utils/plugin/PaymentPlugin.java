package com.hongqiang.shop.common.utils.plugin;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.plugin.service.PluginConfigService;
import com.hongqiang.shop.website.entity.PluginConfig;

/**
 * 支付基类
 * 
 * @author jackial
 * 
 */
public abstract class PaymentPlugin implements Comparable<PaymentPlugin> {

	/**
	 * 税率类型：比例；固定
	 */
	public enum FeeType {
		scale, fixed;
	}

	/**
	 * 调用第三方支付方式的请求：post和get
	 */
	public enum Method {
		post, get;
	}

	public static final String PAYMENT_NAME_ATTRIBUTE_NAME = "paymentName";// 支付名称
	public static final String FEE_TYPE_ATTRIBUTE_NAME = "feeType";// 税率类型
	public static final String FEE_ATTRIBUTE_NAME = "fee";// 税率费用
	public static final String LOGO_ATTRIBUTE_NAME = "logo";// 支付logo
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";// 说明
	public static final String ORDER_ATTRIBUTE_NAME = PaymentPlugin.class
			.getName() + ".PRINCIPAL";
	
	private Map<String, String> tradeInfoMap;

	public Map<String, String> getTradeInfoMap() {
		return tradeInfoMap;
	}

	public void setTradeInfoMap(Map<String, String> tradeInfoMap) {
		this.tradeInfoMap = tradeInfoMap;
	}

	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * 根据支付插件component得到支付名称
	 * 
	 * @return 支付名称，如“支付宝即时交易”
	 */
	public final String getId() {
		return ((Component) getClass().getAnnotation(Component.class)).value();
	}

	/**
	 * 获得支付插件的名称
	 * 
	 * @return 支付插件的名称
	 */
	public abstract String getName();

	/**
	 * 获得支付插件的版本
	 * 
	 * @return 支付插件的版本
	 */
	public abstract String getVersion();

	/**
	 * 获得支付插件的作者
	 * 
	 * @return 支付插件的作者
	 */
	public abstract String getAuthor();

	/**
	 * 获得支付插件的站点
	 * 
	 * @return 支付插件的站点
	 */
	public abstract String getSiteUrl();

	/**
	 * 获得支付插件的安装地址
	 * 
	 * @return 支付插件的安装地址
	 */
	public abstract String getInstallUrl();

	/**
	 * 获得支付插件的卸载地址
	 * 
	 * @return 支付插件的卸载地址
	 */
	public abstract String getUninstallUrl();

	/**
	 * 获得支付插件的设置地址
	 * 
	 * @return 支付插件的设置地址
	 */
	public abstract String getSettingUrl();

	/**
	 * 判断是否安装
	 * 
	 * @return 已经安装返回true，否则返回flase
	 */
	public boolean getIsInstalled() {
		return this.pluginConfigService.pluginIdExists(getId());
	}

	/**
	 * 根据支付插件id得到支付插件实体类
	 * 
	 * @return 支付插件实体类
	 */
	public PluginConfig getPluginConfig() {
		return this.pluginConfigService.findByPluginId(getId());
	}

	/**
	 * 判断是否启用
	 * 
	 * @return 已经启用返回true，否则返回flase
	 */
	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled().booleanValue() : false;
	}

	/**
	 * 根据支付插件中key名称得到支付插件key对应的value
	 * 
	 * @return 支付插件key对应的value
	 */
	public String getAttribute(String name) {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获得支付插件的序号
	 * 
	 * @return 支付插件的序号
	 */
	public Integer getOrder() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getOrder() : null;
	}

	/**
	 * 根据支付插件中key名称得到支付插件key对应的支付名称
	 * 
	 * @return 支付插件key对应的支付名称
	 */
	public String getPaymentName() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig
				.getAttribute(PAYMENT_NAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获得税率类型
	 * 
	 * @return 税率类型e
	 */
	public FeeType getFeeType() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? FeeType.valueOf(pluginConfig
				.getAttribute(FEE_TYPE_ATTRIBUTE_NAME)) : null;
	}

	/**
	 * 获得税率
	 * 
	 * @return 税率
	 */
	public BigDecimal getFee() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? new BigDecimal(
				pluginConfig.getAttribute(FEE_ATTRIBUTE_NAME)) : null;
	}

	/**
	 * 获得插件logo
	 * 
	 * @return 插件logo
	 */
	public String getLogo() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig
				.getAttribute(LOGO_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获得插件说明
	 * 
	 * @return 插件说明
	 */
	public String getDescription() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig
				.getAttribute(DESCRIPTION_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获得支付网关
	 * 
	 * @return 支付网关url
	 */
	public abstract String getUrl();

	/**
	 * 获得支付方式的请求
	 * 
	 * @return 支付方式的请求，分为post和get
	 */
	public abstract Method getMethod();

	/**
	 * 获得超时时间
	 * 
	 * @return 超时时间，单位为秒，如：3000，表示3000秒
	 */
	public abstract Integer getTimeout();

	/**
	 * 
	 * @param order 订单实体类
	 * @return 收货人信息
	 */
	public abstract Map<String, String> getConsigneeInfo(
			com.hongqiang.shop.modules.entity.Order order);

	/**
	 * 
	 * @param sn 付款编号
	 * @param amount  付款总金额
	 * @param description  说明
	 * @param request httprequest
	 * @return map，包含需要传给支付接口的参数和参数名称
	 */
	public abstract Map<String, String> getParameterMap(String sn,
			BigDecimal amount, String description, HttpServletRequest request);

	/**
	 * 
	 * @param sn  付款编号
	 * @param request
	 * @return
	 */
	public abstract boolean verify(String sn, HttpServletRequest request);

	/**
	 * 
	 * @param sn  付款编号
	 * @param request
	 * @return
	 */
	public abstract BigDecimal getAmount(String sn, HttpServletRequest request);

	/**
	 * 
	 * @param sn  付款编号
	 * @param request
	 * @return
	 */
	public abstract String getNotifyContext(String sn,
			HttpServletRequest request);

	/**
	 * 
	 * @param sn  付款编号
	 * @return
	 */
	protected String getReturnUrl(String sn) {
		Setting setting = SettingUtils.get();
		return setting.getSiteUrl() + Global.getFrontPath()+ "/payment/return/" + sn + ".jhtml";
	}

	/**
	 * 
	 * @param sn   付款编号
	 * @return
	 */
	protected String getNotifyUrl(String sn) {
		Setting setting = SettingUtils.get();
		return setting.getSiteUrl() + Global.getFrontPath()+ "/payment/notify/" + sn + ".jhtml";
	}

	/**
	 * 根据订单总金额得到商城的税费
	 * 
	 * @param amount  订单总金额
	 * @return 商城的税费
	 */
	public final BigDecimal getFee(BigDecimal amount) {
		Setting setting = SettingUtils.get();
		BigDecimal fee;
		if (getFeeType() == FeeType.scale)
			fee = amount.multiply(getFee());
		else
			fee = getFee();
		return setting.setScale(fee);
	}

	/**
	 * 判断传入的object是否等于当前支付插件
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		PaymentPlugin paymentPlugin = (PaymentPlugin) obj;
		return new EqualsBuilder().append(getId(), paymentPlugin.getId()).isEquals();
	}

	/**
	 * 得到当前支付插件的hashcode
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	/**
	 * 比较两个支付插件
	 */
	public int compareTo(PaymentPlugin paymentPlugin) {
		return new CompareToBuilder()
				.append(getOrder(), paymentPlugin.getOrder())
				.append(getId(), paymentPlugin.getId()).toComparison();
	}
}