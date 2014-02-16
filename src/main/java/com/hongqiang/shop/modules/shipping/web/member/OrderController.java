package com.hongqiang.shop.modules.shipping.web.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.common.utils.plugin.service.PluginService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CartService;
import com.hongqiang.shop.modules.account.service.CouponCodeService;
import com.hongqiang.shop.modules.account.service.PaymentMethodService;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.PaymentMethod;
import com.hongqiang.shop.modules.entity.Receiver;
import com.hongqiang.shop.modules.entity.Shipping;
import com.hongqiang.shop.modules.entity.ShippingMethod;
import com.hongqiang.shop.modules.shipping.service.OrderService;
import com.hongqiang.shop.modules.shipping.service.ReceiverService;
import com.hongqiang.shop.modules.shipping.service.ShippingMethodService;
import com.hongqiang.shop.modules.shipping.service.ShippingService;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberOrderController")
@RequestMapping({ "${memberPath}/order" })
public class OrderController extends BaseController {
	private static final int PAGE_SIZE = 10;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ReceiverService receiverService;

	@Autowired
	private CartService cartService;

	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private ShippingMethodService shippingMethodService;

	@Autowired
	private CouponCodeService couponCodeService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ShippingService shippingService;

	@Autowired
	private PluginService pluginService;

	@RequestMapping(value = { "/save_receiver" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveReceiver(Receiver receiver, Long areaId) {
		Map<String,Object> map = new HashMap<String,Object>();
		receiver.setArea((Area) this.areaService.find(areaId));
		if (!beanValidator(receiver, new Class[0])) {
			map.put("message", SHOP_ERROR);
			return map;
		}
		Member member = this.memberService.getCurrent();
		if ((Receiver.MAX_RECEIVER_COUNT != null)
				&& (member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT.intValue())) {
			map.put("message", Message.error("shop.order.addReceiverCountNotAllowed",
					new Object[] { Receiver.MAX_RECEIVER_COUNT }));
			return map;
		}
		receiver.setMember(member);
		this.receiverService.save(receiver);
		map.put("message", SHOP_SUCCESS);
		map.put("receiver", receiver);
		return map;
	}

	@RequestMapping(value = { "/check_lock" }, method = RequestMethod.POST)
	@ResponseBody
	public Message checkLock(String sn) {
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		if ((order != null)
				&& (order.getMember() == this.memberService.getCurrent())
				&& (!order.isExpired())
				&& (order.getPaymentMethod() != null)
				&& (order.getPaymentMethod().getType() == PaymentMethod.Type.online)
				&& ((order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.unpaid) || 
						(order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment))) {
			if (order.isLocked(null))
				return Message.warn("shop.order.locked", new Object[0]);
			order.setLockExpire(DateUtils.addSeconds(new Date(), 60));
			order.setOperator(null);
			this.orderService.update(order);
			return SHOP_SUCCESS;
		}
		return SHOP_ERROR;
	}

	@RequestMapping(value = { "/check_payment" }, method = RequestMethod.POST)
	@ResponseBody
	public boolean checkPayment(String sn) {
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		return (order != null)
				&& (order.getMember() == this.memberService.getCurrent())
				&& (order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid);
	}

	@RequestMapping(value = { "/coupon_info" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> couponInfo(String code) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		Cart cart = this.cartService.getCurrent();
		if ((cart == null) || (cart.isEmpty())) {
			map.put("message",Message.warn("shop.order.cartNotEmpty", new Object[0]));
			return map;
		}
		if (!cart.isCouponAllowed()) {
			map.put("message",Message.warn("shop.order.couponNotAllowed", new Object[0]));
			return map;
		}
		CouponCode couponCode = this.couponCodeService.findByCode(code);
		if ((couponCode != null) && (couponCode.getCoupon() != null)) {
			Coupon coupon = couponCode.getCoupon();
			if (!coupon.getIsEnabled().booleanValue()) {
				map.put("message", Message.warn("shop.order.couponDisabled", new Object[0]));
				return map;
			}
			if (!coupon.hasBegun()) {
				map.put("message", Message.warn("shop.order.couponNotBegin", new Object[0]));
				return map;
			}
			if (coupon.hasExpired()) {
				map.put("message", Message.warn("shop.order.couponHasExpired", new Object[0]));
				return map;
			}
			if (!cart.isValid(coupon)) {
				map.put("message", Message.warn("shop.order.couponInvalid", new Object[0]));
				return map;
			}
			if (couponCode.getIsUsed().booleanValue()) {
				map.put("message", Message.warn("shop.order.couponCodeUsed", new Object[0]));
				return map;
			}
			map.put("message", SHOP_SUCCESS);
			map.put("couponName", coupon.getName());
			return map;
		}
		map.put("message",Message.warn("shop.order.couponCodeNotExist", new Object[0]));
		return map;
	}

	@RequestMapping(value = { "/info" }, method = RequestMethod.GET)
	public String info(ModelMap model) {
		Cart cart = this.cartService.getCurrent();
		if ((cart == null) || (cart.isEmpty()))
			return "redirect:/cart/list.jhtml";
		if (!beanValidator( cart, new Class[0]))
			return SHOP_ERROR_PAGE;
		com.hongqiang.shop.modules.entity.Order order = 
				this.orderService.build(cart, null, null, null, null, false, null, false,null);
		model.addAttribute("order", order);
		model.addAttribute("cartToken", cart.getToken());
		model.addAttribute("paymentMethods",this.paymentMethodService.findAll());
		model.addAttribute("shippingMethods",this.shippingMethodService.findAll());
		return "/shop/member/order/info";
	}

	@RequestMapping(value = { "/calculate" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> calculate(Long paymentMethodId,Long shippingMethodId, String code,
			@RequestParam(defaultValue = "false") Boolean isInvoice,String invoiceTitle,
			@RequestParam(defaultValue = "false") Boolean useBalance,String memo) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		Cart cart = this.cartService.getCurrent();
		if ((cart == null) || (cart.isEmpty())) {
			map.put("message",Message.error("shop.order.cartNotEmpty", new Object[0]));
			return map;
		}
		PaymentMethod paymentMethod = (PaymentMethod) this.paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = (ShippingMethod) this.shippingMethodService.find(shippingMethodId);
		CouponCode couponCode = this.couponCodeService.findByCode(code);
		com.hongqiang.shop.modules.entity.Order order = this.orderService
				.build(cart, null, paymentMethod,shippingMethod, couponCode,isInvoice.booleanValue(),
						invoiceTitle,useBalance.booleanValue(), memo);
		map.put("message", SHOP_SUCCESS);
		map.put("quantity", Integer.valueOf(order.getQuantity()));
		map.put("price", order.getPrice());
		map.put("freight", order.getFreight());
		map.put("tax", order.getTax());
		map.put("amountPayable", order.getAmountPayable());
		return map;
	}

	@RequestMapping(value = { "/create" }, method = RequestMethod.POST)
	@ResponseBody
	public Message create(String cartToken, Long receiverId,
			Long paymentMethodId, Long shippingMethodId, String code,
			@RequestParam(defaultValue = "false") Boolean isInvoice,String invoiceTitle,
			@RequestParam(defaultValue = "false") Boolean useBalance,String memo) {
		Cart cart = this.cartService.getCurrent();
		if ((cart == null) || (cart.isEmpty()))
			return Message.warn("shop.order.cartNotEmpty", new Object[0]);
		if (!StringUtils.equals(cart.getToken(), cartToken))
			return Message.warn("shop.order.cartHasChanged", new Object[0]);
		if (cart.getIsLowStock())
			return Message.warn("shop.order.cartLowStock", new Object[0]);
		Receiver receiver = (Receiver) this.receiverService.find(receiverId);
		if (receiver == null)
			return Message.error("shop.order.receiverNotExsit", new Object[0]);
		PaymentMethod paymentMethod = (PaymentMethod) this.paymentMethodService.find(paymentMethodId);
		if (paymentMethod == null)
			return Message.error("shop.order.paymentMethodNotExsit",new Object[0]);
		ShippingMethod shippingMethod = (ShippingMethod) this.shippingMethodService.find(shippingMethodId);
		if (shippingMethod == null)
			return Message.error("shop.order.shippingMethodNotExsit",new Object[0]);
		if (!paymentMethod.getShippingMethods().contains(shippingMethod))
			return Message.error("shop.order.deliveryUnsupported",new Object[0]);
		CouponCode couponCode = this.couponCodeService.findByCode(code);
		com.hongqiang.shop.modules.entity.Order order = this.orderService
				.create(cart, receiver, paymentMethod,shippingMethod, couponCode,isInvoice.booleanValue(), 
						invoiceTitle,useBalance.booleanValue(), memo, null);
		return Message.success(order.getSn(), new Object[0]);
	}

	@RequestMapping(value = { "/payment" }, method = RequestMethod.GET)
	public String payment(String sn, ModelMap model) {
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		if ((order == null) || (order.getMember() != this.memberService.getCurrent())
				|| (order.isExpired()) || (order.getPaymentMethod() == null))
			return SHOP_ERROR_PAGE;
		if (order.getPaymentMethod().getType() == PaymentMethod.Type.online) {
			List<PaymentPlugin> paymentPlugins = this.pluginService.getPaymentPlugins(true);
			if (!paymentPlugins.isEmpty()) {
				PaymentPlugin paymentPlugin = (PaymentPlugin) paymentPlugins.get(0);
				order.setFee(paymentPlugin.getFee(order.getAmountPayable()));
				model.addAttribute("defaultPaymentPlugin", paymentPlugin);
				model.addAttribute("paymentPlugins", paymentPlugins);
			}
		}
		model.addAttribute("order", order);
		return "/shop/member/order/payment";
	}

	@RequestMapping(value = { "/payment_plugin_select" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> paymentPluginSelect(String sn,String paymentPluginId) {
		Map<String,Object> map = new HashMap<String,Object>();
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		PaymentPlugin localPaymentPlugin = this.pluginService.getPaymentPlugin(paymentPluginId);
		if ((order == null)
				|| (order.getMember() != this.memberService.getCurrent())
				|| (order.isExpired())
				|| (order.isLocked(null))
				|| (order.getPaymentMethod() == null)
				|| (order.getPaymentMethod().getType() == PaymentMethod.Type.offline)
				|| (localPaymentPlugin == null)
				|| (!localPaymentPlugin.getIsEnabled())) {
			map.put("message", SHOP_ERROR);
			return map;
		}
		order.setFee(localPaymentPlugin.getFee(order.getAmountPayable()));
		map.put("message", SHOP_SUCCESS);
		map.put("fee", order.getFee());
		map.put("amountPayable", order.getAmountPayable());
		return map;
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = this.memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber,Integer.valueOf(PAGE_SIZE));
		model.addAttribute("page",this.orderService.findPage(member, pageable));
		return "shop/member/order/list";
	}

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view(String sn, ModelMap model) {
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		if (order == null)
			return SHOP_ERROR_PAGE;
		Member member = this.memberService.getCurrent();
		if (!member.getOrders().contains(order))
			return SHOP_ERROR_PAGE;
		model.addAttribute("order", order);
		return "shop/member/order/view";
	}

	@RequestMapping(value = { "/cancel" }, method = RequestMethod.POST)
	@ResponseBody
	public Message cancel(String sn) {
		com.hongqiang.shop.modules.entity.Order order = this.orderService.findBySn(sn);
		if ((order != null)
				&& (order.getMember() == this.memberService.getCurrent())
				&& (!order.isExpired())
				&& (order.getOrderStatus() == com.hongqiang.shop.modules.entity.Order.OrderStatus.unconfirmed)
				&& (order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.unpaid)) {
			if (order.isLocked(null))
				return Message.warn("shop.member.order.locked", new Object[0]);
			try {
				this.orderService.cancel(order, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return SHOP_SUCCESS;
		}
		return SHOP_ERROR;
	}

	@RequestMapping(value = { "/delivery_query" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deliveryQuery(String sn) {
		Map<String, Object> map = new HashMap<String, Object>();
		Shipping shipping = this.shippingService.findBySn(sn);
		Setting setting = SettingUtils.get();
		if ((shipping != null)
				&& (shipping.getOrder() != null)
				&& (shipping.getOrder().getMember() == this.memberService.getCurrent())
				&& (StringUtils.isNotEmpty(setting.getKuaidi100Key()))
				&& (StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()))
				&& (StringUtils.isNotEmpty(shipping.getTrackingNo())))
			map = this.shippingService.query(shipping);
		return map;
	}
}