package com.hongqiang.shop.common.utils.plugin.alipay;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.common.utils.plugin.service.PluginConfigService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.PluginConfig;

@Controller("adminAlipayDirectController")
@RequestMapping({ "${adminPath}/payment_plugin/alipay_direct" })
public class AlipayDirectController extends BaseController {

	@Resource(name = "alipayDirectPlugin")
	private AlipayDirectPlugin alipayDirectPlugin;

	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	@RequestMapping(value = { "/install" }, method = RequestMethod.GET)
	public String install(RedirectAttributes redirectAttributes) {
		if (!this.alipayDirectPlugin.getIsInstalled()) {
			PluginConfig localPluginConfig = new PluginConfig();
			localPluginConfig.setPluginId(this.alipayDirectPlugin.getId());
			localPluginConfig.setIsEnabled(Boolean.valueOf(false));
			this.pluginConfigService.save(localPluginConfig);
		}
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:" + Global.getAdminPath()+ "/payment_plugin/list.jhtml";
	}

	@RequestMapping(value = { "/uninstall" }, method = RequestMethod.GET)
	public String uninstall(RedirectAttributes redirectAttributes) {
		if (this.alipayDirectPlugin.getIsInstalled()) {
			PluginConfig localPluginConfig = this.alipayDirectPlugin.getPluginConfig();
			this.pluginConfigService.delete(localPluginConfig);
		}
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:" + Global.getAdminPath()+ "/payment_plugin/list.jhtml";
	}

	@RequestMapping(value = { "/setting" }, method = RequestMethod.GET)
	public String setting(ModelMap model) {
		PluginConfig localPluginConfig = this.alipayDirectPlugin.getPluginConfig();
		model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
		model.addAttribute("pluginConfig", localPluginConfig);
		return "/admin/alipayDirect/setting";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(String paymentName, String seller_email, String partner, String key,
			PaymentPlugin.FeeType feeType, BigDecimal fee, String logo,
			String description,
			@RequestParam(defaultValue = "false") Boolean isEnabled,
			Integer order, RedirectAttributes redirectAttributes) {
		PluginConfig pluginConfig = this.alipayDirectPlugin.getPluginConfig();
		pluginConfig.setAttribute("paymentName", paymentName);
		pluginConfig.setAttribute("seller_email", seller_email);
		pluginConfig.setAttribute("partner", partner);
		pluginConfig.setAttribute("key", key);
		pluginConfig.setAttribute("feeType", feeType.toString());
		pluginConfig.setAttribute("fee", fee.toString());
		pluginConfig.setAttribute("logo", logo);
		pluginConfig.setAttribute("description", description);
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrder(order);
		this.pluginConfigService.update(pluginConfig);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:" + Global.getAdminPath()+ "/payment_plugin/list.jhtml";
	}
}