package com.hongqiang.shop.modules.account.web.admin;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PaymentMethodService;
import com.hongqiang.shop.modules.entity.PaymentMethod;
import com.hongqiang.shop.modules.entity.ShippingMethod;
import com.hongqiang.shop.modules.shipping.service.ShippingMethodService;

@Controller("adminPaymentMethodController")
@RequestMapping({ "${adminPath}/payment_method" })
public class PaymentMethodController extends BaseController {

	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private ShippingMethodService shippingMethodService;

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", PaymentMethod.Type.values());
		model.addAttribute("shippingMethods",
				this.shippingMethodService.findAll());
		return "/admin/payment_method/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(PaymentMethod paymentMethod, Long[] shippingMethodIds,
			RedirectAttributes redirectAttributes) {
		paymentMethod.setShippingMethods(new HashSet<ShippingMethod>(
				this.shippingMethodService.findList(shippingMethodIds)));
		if (!beanValidator(redirectAttributes, paymentMethod, new Class[0]))
			return ERROR_PAGE;
		paymentMethod.setOrders(null);
		this.paymentMethodService.save(paymentMethod);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", PaymentMethod.Type.values());
		model.addAttribute("shippingMethods",
				this.shippingMethodService.findAll());
		model.addAttribute("paymentMethod", this.paymentMethodService.find(id));
		return "/admin/payment_method/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(PaymentMethod paymentMethod, Long[] shippingMethodIds,
			RedirectAttributes redirectAttributes) {
		paymentMethod.setShippingMethods(new HashSet<ShippingMethod>(
				this.shippingMethodService.findList(shippingMethodIds)));
		if (!beanValidator(redirectAttributes, paymentMethod, new Class[0]))
			return ERROR_PAGE;
		this.paymentMethodService.update(paymentMethod,
				new String[] { "orders" });
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.paymentMethodService.findPage(pageable));
		return "/admin/payment_method/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids.length >= this.paymentMethodService.count())
			return Message.error("admin.common.deleteAllNotAllowed",
					new Object[0]);
		this.paymentMethodService.delete(ids);
		return ADMIN_SUCCESS;
	}
}