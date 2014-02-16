package com.hongqiang.shop.modules.account.web.shop;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.common.utils.plugin.service.PluginService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PaymentService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Payment;
import com.hongqiang.shop.modules.entity.PaymentMethod;
import com.hongqiang.shop.modules.entity.Sn;
import com.hongqiang.shop.modules.product.service.SnService;
import com.hongqiang.shop.modules.shipping.service.OrderService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopPaymentController")
@RequestMapping({ "${frontPath}/payment" })
public class PaymentController extends BaseController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private PluginService pluginService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private SnService snService;

	@RequestMapping(value = { "/submit" }, method = RequestMethod.POST)
	public String submit(String sn, String paymentPluginId, HttpServletRequest request, ModelMap model) {
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		if (order == null)
			return SHOP_ERROR_PAGE;
		Member member = this.memberService.getCurrent();
		if ((member == null) || (order.getMember() != member) || (order.isExpired()))
			return SHOP_ERROR_PAGE;
		if ((order.getPaymentMethod() == null) || (order.getPaymentMethod().getType() == PaymentMethod.Type.offline))
			return SHOP_ERROR_PAGE;
		if ((order.getPaymentStatus() != com.hongqiang.shop.modules.entity.Order.PaymentStatus.unpaid)
				&& (order.getPaymentStatus() != com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment))
			return SHOP_ERROR_PAGE;
		if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0)
			return SHOP_ERROR_PAGE;
		PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(paymentPluginId);
		if ((paymentPlugin == null) || (!paymentPlugin.getIsEnabled()))
			return SHOP_ERROR_PAGE;
		BigDecimal fee = paymentPlugin.getFee(order.getAmountPayable());
		BigDecimal amount = order.getAmountPayable().add(fee);
		Payment payment = new Payment();
		payment.setSn(this.snService.generate(Sn.Type.payment));
		payment.setType(Payment.Type.online);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentMethod(order.getPaymentMethodName() + "-" + paymentPlugin.getPaymentName());
		payment.setFee(fee);
		payment.setAmount(amount);
		payment.setPaymentPluginId(paymentPluginId);
		payment.setExpire(paymentPlugin.getTimeout() != null ? 
				DateUtils .addMinutes(new Date(), paymentPlugin.getTimeout().intValue()) : null);
		payment.setMember(null);
		payment.setOrder(order);
		this.paymentService.save(payment);
		paymentPlugin.setTradeInfoMap(paymentPlugin.getConsigneeInfo(order));
		model.addAttribute("url", paymentPlugin.getUrl());
		model.addAttribute("method", paymentPlugin.getMethod());
		model.addAttribute("parameterMap", paymentPlugin.getParameterMap(
				payment.getSn(), amount,order.getProductName(), request));
		return "shop/payment/submit";
	}

	@RequestMapping({ "/return/{sn}" })
	public String returns(@PathVariable String sn, HttpServletRequest request,
			ModelMap model) {
		Payment payment = this.paymentService.findBySn(sn);
		if (payment == null)
			return SHOP_ERROR_PAGE;
		if (payment.getStatus() == Payment.Status.wait) {
			PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if ((paymentPlugin != null) && (paymentPlugin.verify(sn, request))) {
				BigDecimal amount = paymentPlugin.getAmount(sn,request);
				if (amount.compareTo(payment.getAmount()) >= 0) {
					com.hongqiang.shop.modules.entity.Order order = payment.getOrder();
					if (order != null) {
						if (amount.compareTo(order.getAmountPayable()) >= 0)
							this.orderService.payment(order, payment,null);
					} else {
						Member member = payment.getMember();
						if (member != null) {
							BigDecimal price = payment.getAmount().subtract(payment.getFee());
							this.memberService.update(member,null,price,
									addMessage("shop.payment.paymentName",new Object[] { paymentPlugin.getPaymentName() }), null);
						}
					}
				}
				payment.setStatus(Payment.Status.success);
				payment.setAmount(amount);
				payment.setPaymentDate(new Date());
			} else {
				payment.setStatus(Payment.Status.failure);
				payment.setPaymentDate(new Date());
			}
			this.paymentService.update(payment);
		}
		model.addAttribute("payment", payment);
		return "shop/payment/return";
	}

	@RequestMapping({ "/notify/{sn}" })
	public String notify(@PathVariable String sn, HttpServletRequest request, ModelMap model) {
		Payment payment = this.paymentService.findBySn(sn);
		if (payment != null) {
			PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if (paymentPlugin != null) {
				if ((payment.getStatus() == Payment.Status.wait) && (paymentPlugin.verify(sn, request))) {
					BigDecimal amount = paymentPlugin.getAmount(sn, request);
					if (amount.compareTo(payment.getAmount()) >= 0) {
						com.hongqiang.shop.modules.entity.Order order = payment.getOrder();
						if (order != null) {
							if (amount.compareTo(order.getAmountPayable()) >= 0)
								this.orderService.payment(order,payment, null);
						} else {
							Member member = payment.getMember();
							if (member != null) {
								BigDecimal price = payment.getAmount().subtract(payment.getFee());
								this.memberService.update(member,null,price,
												addMessage("shop.payment.paymentName",new Object[] { paymentPlugin.getPaymentName() }),
												null);
							}
						}
					}
					payment.setStatus(Payment.Status.success);
					payment.setAmount(amount);
					payment.setPaymentDate(new Date());
					this.paymentService.update(payment);
				}
				model.addAttribute("notifyContext",paymentPlugin.getNotifyContext(sn, request));
			}
		}
		return "shop/payment/notify";
	}
}