package com.hongqiang.shop.common.utils.plugin.alipay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.website.entity.PluginConfig;

@Component("alipayDirectPlugin")
public class AlipayDirectPlugin extends PaymentPlugin {

	public String getName() {
		return "支付宝即时交易";
	}

	public String getVersion() {
		return "1.0";
	}

	public String getAuthor() {
		return "Alipay";
	}

	public String getSiteUrl() {
		return "http://www.alipay.com";
	}

	public String getInstallUrl() {
		return "alipay_direct/install.jhtml";
	}

	public String getUninstallUrl() {
		return "alipay_direct/uninstall.jhtml";
	}

	public String getSettingUrl() {
		return "alipay_direct/setting.jhtml";
	}

	public String getUrl() {
		return "https://mapi.alipay.com/gateway.do";
	}

	public PaymentPlugin.Method getMethod() {
		return PaymentPlugin.Method.get;
	}

	public Integer getTimeout() {
		return Integer.valueOf(21600);
	}

	public Map<String, String> getConsigneeInfo(
			com.hongqiang.shop.modules.entity.Order order){
		Map<String, String> paramsMap = new HashMap<String, String>();
		return paramsMap;
	}
	
	public Map<String, String> getParameterMap(String sn, BigDecimal amount,
			String description, HttpServletRequest request) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		// 支付类型
		String payment_type = "1";
		// 服务器异步通知页面路径
		String notify_url = getNotifyUrl(sn);
		// 页面跳转同步通知页面路径
		String return_url = getReturnUrl(sn);
		// 付款金额
		BigDecimal total_fee = amount.setScale(2,  BigDecimal.ROUND_HALF_UP);
		// 防钓鱼时间戳,若要使用请调用类文件submit中的query_timestamp函数
//		String anti_phishing_key = String.valueOf(getTimeout());
		// 字符集
		String input_charset = "utf-8";
		PluginConfig pluginConfig = getPluginConfig();
		String key = (pluginConfig != null) ? pluginConfig.getAttribute("key") : null;
		String partner = (pluginConfig != null) ? pluginConfig.getAttribute("partner") : null;
		String seller_email = (pluginConfig != null) ? pluginConfig.getAttribute("seller_email") : null;
		paramsMap.put("service", "create_direct_pay_by_user");
		paramsMap.put("partner", partner);
		paramsMap.put("_input_charset", input_charset);
		paramsMap.put("payment_type", payment_type);
		paramsMap.put("notify_url", notify_url);
		paramsMap.put("return_url", return_url);
		paramsMap.put("seller_email", seller_email);
		paramsMap.put("out_trade_no", sn);
		paramsMap.put("subject", description);
		paramsMap.put("total_fee", total_fee.toString());
//		paramsMap.put("body", body);
//		paramsMap.put("show_url", show_url);
//		paramsMap.put("anti_phishing_key", anti_phishing_key);
//		paramsMap.put("exter_invoke_ip", exter_invoke_ip);
		String mysign = AlipayUtils.buildRequestMysign(paramsMap, key, input_charset);
		paramsMap.put("sign", mysign);
		paramsMap.put("sign_type", "MD5");
		System.out.println(paramsMap);
		return paramsMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean verify(String sn, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		String key = (pluginConfig != null) ? pluginConfig.getAttribute("key") : null;
		String partner = (pluginConfig != null) ? pluginConfig.getAttribute("partner") : null;
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		return AlipayUtils.verify(params, partner, key);
	}

	public BigDecimal getAmount(String sn, HttpServletRequest request) {
		return new BigDecimal(request.getParameter("total_fee"));
	}

	public String getNotifyContext(String sn, HttpServletRequest request) {
		if(verify(sn, request)){
			return "success";
		}else {
			return "fail";
		}
	}
}