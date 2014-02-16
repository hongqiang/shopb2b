package com.hongqiang.shop.modules.shipping.web.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
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
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.DeliveryCorp;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Order;
import com.hongqiang.shop.modules.entity.OrderItem;
import com.hongqiang.shop.modules.entity.Payment;
import com.hongqiang.shop.modules.entity.PaymentMethod;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Refunds;
import com.hongqiang.shop.modules.entity.Returns;
import com.hongqiang.shop.modules.entity.ReturnsItem;
import com.hongqiang.shop.modules.entity.Shipping;
import com.hongqiang.shop.modules.entity.ShippingItem;
import com.hongqiang.shop.modules.entity.ShippingMethod;
import com.hongqiang.shop.modules.entity.Sn;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.product.service.SnService;
import com.hongqiang.shop.modules.shipping.service.DeliveryCorpService;
import com.hongqiang.shop.modules.shipping.service.OrderItemService;
import com.hongqiang.shop.modules.shipping.service.OrderService;
import com.hongqiang.shop.modules.shipping.service.ShippingMethodService;
import com.hongqiang.shop.modules.user.service.AdminService;
import com.hongqiang.shop.modules.user.service.AreaService;

@Controller("adminOrderController")
@RequestMapping({ "${adminPath}/order" })
public class OrderController extends BaseController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private ShippingMethodService shippingMethodService;

	@Autowired
	private DeliveryCorpService deliveryCorpService;

	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private SnService snService;

	@RequestMapping(value = { "/check_lock" }, method = RequestMethod.POST)
	@ResponseBody
	public Message checkLock(Long id) {
		Order order = (Order) this.orderService.find(id);
		if (order == null)
			return Message.warn("admin.common.invalid", new Object[0]);
		Admin operator = this.adminService.getCurrent();
		if (order.isLocked(operator)) {
			if (order.getOperator() != null)
				return Message.warn("admin.order.adminLocked",new Object[] { order.getOperator().getUsername() });
			return Message.warn("admin.order.memberLocked", new Object[0]);
		}
		order.setLockExpire(DateUtils.addSeconds(new Date(), 60));
		order.setOperator(operator);
		this.orderService.update(order);
		return ADMIN_SUCCESS;
	}

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("types", Payment.Type.values());
		model.addAttribute("refundsTypes", Refunds.Type.values());
		model.addAttribute("paymentMethods", this.paymentMethodService.findAll());
		model.addAttribute("shippingMethods", this.shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", this.deliveryCorpService.findAll());
		model.addAttribute("order", this.orderService.find(id));
		return "/admin/order/view";
	}

	@RequestMapping(value = { "/confirm" }, method = RequestMethod.POST)
	public String confirm(Long id, RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(id);
		Admin operator = this.adminService.getCurrent();
		if ((order != null)
				&& (!order.isExpired())
				&& (order.getOrderStatus() == Order.OrderStatus.unconfirmed)
				&& (!order.isLocked(operator))) {
			this.orderService.confirm(order, operator);
			addMessage(redirectAttributes, ADMIN_SUCCESS);
		} else {
			addMessage(redirectAttributes, Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/complete" }, method = RequestMethod.POST)
	public String complete(Long id, RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(id);
		Admin operator = this.adminService.getCurrent();
		if ((order != null) && (!order.isExpired())
				&& (order.getOrderStatus() == Order.OrderStatus.confirmed)
				&& (!order.isLocked(operator))) {
			this.orderService.complete(order, operator);
			addMessage(redirectAttributes, ADMIN_SUCCESS);
		} else {
			addMessage(redirectAttributes, Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/cancel" }, method = RequestMethod.POST)
	public String cancel(Long id, RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(id);
		Admin operator = this.adminService.getCurrent();
		if ((order != null)
				&& (!order.isExpired())
				&& (order.getOrderStatus() == Order.OrderStatus.unconfirmed)
				&& (!order.isLocked(operator))) {
			this.orderService.cancel(order, operator);
			addMessage(redirectAttributes, ADMIN_SUCCESS);
		} else {
			addMessage(redirectAttributes, Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/payment" }, method = RequestMethod.POST)
	public String payment(Long orderId, Long paymentMethodId, Payment payment,
			RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(orderId);
		payment.setOrder(order);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.paymentMethodService.find(paymentMethodId);
		payment.setPaymentMethod(localPaymentMethod != null ? localPaymentMethod.getName() : null);
		if (!beanValidator(redirectAttributes, payment, new Class[0]))
			return ERROR_PAGE;
		if ((order.isExpired()) || (order.getOrderStatus() != Order.OrderStatus.confirmed))
			return ERROR_PAGE;
		if ((order.getPaymentStatus() != Order.PaymentStatus.unpaid)
				&& (order.getPaymentStatus() != Order.PaymentStatus.partialPayment))
			return ERROR_PAGE;
		if ((payment.getAmount().compareTo(new BigDecimal(0)) <= 0)
				|| (payment.getAmount().compareTo(order.getAmountPayable()) > 0))
			return ERROR_PAGE;
		Member member = order.getMember();
		if ((payment.getType() == Payment.Type.deposit)
				&& (payment.getAmount().compareTo(member.getBalance()) > 0))
			return ERROR_PAGE;
		Admin operator = this.adminService.getCurrent();
		if (order.isLocked(operator))
			return ERROR_PAGE;
		payment.setSn(this.snService.generate(Sn.Type.payment));
		payment.setStatus(Payment.Status.success);
		payment.setFee(new BigDecimal(0));
		payment.setOperator(operator.getUsername());
		payment.setPaymentDate(new Date());
		payment.setPaymentPluginId(null);
		payment.setExpire(null);
		payment.setDeposit(null);
		payment.setMember(null);
		this.orderService.payment(order, payment, operator);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/refunds" }, method = RequestMethod.POST)
	public String refunds(Long orderId, Long paymentMethodId, Refunds refunds,
			RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(orderId);
		refunds.setOrder(order);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.paymentMethodService.find(paymentMethodId);
		refunds.setPaymentMethod(localPaymentMethod != null ? localPaymentMethod.getName() : null);
		if (!beanValidator(redirectAttributes, refunds, new Class[0]))
			return ERROR_PAGE;
		if ((order.isExpired()) || (order.getOrderStatus() != Order.OrderStatus.confirmed))
			return ERROR_PAGE;
		if ((order.getPaymentStatus() != Order.PaymentStatus.paid)
				&& (order.getPaymentStatus() != Order.PaymentStatus.partialPayment)
				&& (order.getPaymentStatus() != Order.PaymentStatus.partialRefunds))
			return ERROR_PAGE;
		if ((refunds.getAmount().compareTo(new BigDecimal(0)) <= 0)
				|| (refunds.getAmount().compareTo(order.getAmountPaid()) > 0))
			return ERROR_PAGE;
		Admin operator = this.adminService.getCurrent();
		if (order.isLocked(operator))
			return ERROR_PAGE;
		refunds.setSn(this.snService.generate(Sn.Type.refunds));
		refunds.setOperator(operator.getUsername());
		this.orderService.refunds(order, refunds, operator);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/shipping" }, method = RequestMethod.POST)
	public String shipping(Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, Shipping shipping,
			RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(orderId);
		if (order == null)
			return ERROR_PAGE;
		Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator();
		while (iterator.hasNext()) {
			ShippingItem shippingItem = (ShippingItem) (iterator.next());
			if ((shippingItem == null)
					|| (StringUtils.isEmpty(shippingItem.getSn()))
					|| (shippingItem.getQuantity() == null)
					|| (shippingItem.getQuantity().intValue() <= 0)) {
				iterator.remove();
			} else {
				OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
				if ((orderItem == null)
						|| (shippingItem.getQuantity().intValue() > orderItem.getQuantity().intValue()
								- orderItem.getShippedQuantity().intValue()))
					return ERROR_PAGE;
				if ((orderItem.getProduct() != null)
						&& (orderItem.getProduct().getStock() != null)
						&& (shippingItem.getQuantity().intValue() > orderItem.getProduct().getStock().intValue()))
					return ERROR_PAGE;
				shippingItem.setName(orderItem.getFullName());
				shippingItem.setShipping(shipping);
			}
		}
		shipping.setOrder(order);
		ShippingMethod shippingMethod = (ShippingMethod) this.shippingMethodService.find(shippingMethodId);
		shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		DeliveryCorp deliveryCorp = (DeliveryCorp) this.deliveryCorpService.find(deliveryCorpId);
		shipping.setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
		shipping.setDeliveryCorpUrl(deliveryCorp != null ? deliveryCorp.getUrl() : null);
		shipping.setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp.getCode() : null);
		Area area = (Area) this.areaService.find(areaId);
		shipping.setArea(area != null ? area.getFullName() : null);
		if (!beanValidator(redirectAttributes, shipping, new Class[0]))
			return ERROR_PAGE;
		if ((order.isExpired()) || (order.getOrderStatus() != Order.OrderStatus.confirmed))
			return ERROR_PAGE;
		if ((order.getShippingStatus() != Order.ShippingStatus.unshipped)
				&& (order.getShippingStatus() != Order.ShippingStatus.partialShipment))
			return ERROR_PAGE;
		Admin operator = this.adminService.getCurrent();
		if (order.isLocked(operator))
			return ERROR_PAGE;
		shipping.setSn(this.snService.generate(Sn.Type.shipping));
		shipping.setOperator(operator.getUsername());
		this.orderService.shipping(order, shipping, operator);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/returns" }, method = RequestMethod.POST)
	public String returns(Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, Returns returns,
			RedirectAttributes redirectAttributes) {
		Order order = (Order) this.orderService.find(orderId);
		if (order == null)
			return ERROR_PAGE;
		Iterator<ReturnsItem> iterator = returns.getReturnsItems().iterator();
		while (iterator.hasNext()) {
			ReturnsItem returnsItem = (ReturnsItem) iterator.next();
			if ((returnsItem == null)
					|| (StringUtils.isEmpty(returnsItem.getSn()))
					|| (returnsItem.getQuantity() == null)
					|| (returnsItem.getQuantity().intValue() <= 0)) {
				iterator.remove();
			} else {
				OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
				if ((orderItem == null)
						|| (returnsItem.getQuantity().intValue() > orderItem.getShippedQuantity().intValue()
								- orderItem.getReturnQuantity().intValue()))
					return ERROR_PAGE;
				returnsItem.setName(orderItem.getFullName());
				returnsItem.setReturns(returns);
			}
		}
		returns.setOrder(order);
		ShippingMethod shippingMethod = (ShippingMethod) this.shippingMethodService.find(shippingMethodId);
		returns.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		DeliveryCorp deliveryCorp = (DeliveryCorp) this.deliveryCorpService.find(deliveryCorpId);
		returns.setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
		Area area = (Area) this.areaService.find(areaId);
		returns.setArea(area != null ? area.getFullName() : null);
		if (!beanValidator(redirectAttributes, returns, new Class[0]))
			return ERROR_PAGE;
		if ((order.isExpired()) || (order.getOrderStatus() != Order.OrderStatus.confirmed))
			return ERROR_PAGE;
		if ((order.getShippingStatus() != Order.ShippingStatus.shipped)
				&& (order.getShippingStatus() != Order.ShippingStatus.partialShipment)
				&& (order.getShippingStatus() != Order.ShippingStatus.partialReturns))
			return ERROR_PAGE;
		Admin operator = this.adminService.getCurrent();
		if (order.isLocked(operator))
			return ERROR_PAGE;
		returns.setSn(this.snService.generate(Sn.Type.returns));
		returns.setOperator(operator.getUsername());
		this.orderService.returns(order, returns, operator);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("paymentMethods", this.paymentMethodService.findAll());
		model.addAttribute("shippingMethods", this.shippingMethodService.findAll());
		model.addAttribute("order", this.orderService.find(id));
		return "/admin/order/edit";
	}

	@RequestMapping(value = { "/order_item_add" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderItemAdd(String productSn) {
		Map<String, Object> map = new HashMap<String, Object>();
		Product product = this.productService.findBySn(productSn);
		if (product == null) {
			map.put("message", Message.warn("admin.order.productNotExist", new Object[0]));
			return map;
		}
		if (!product.getIsMarketable().booleanValue()) {
			map.put("message", Message.warn("admin.order.productNotMarketable", new Object[0]));
			return map;
		}
		if (product.getIsOutOfStock().booleanValue()) {
			map.put("message", Message.warn("admin.order.productOutOfStock", new Object[0]));
			return map;
		}
		map.put("sn", product.getSn());
		map.put("fullName", product.getFullName());
		map.put("price", product.getPrice());
		map.put("weight", product.getWeight());
		map.put("isGift", product.getIsGift());
		map.put("message", ADMIN_SUCCESS);
		return map;
	}

	@RequestMapping(value = { "/calculate" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> calculate(Order order, Long areaId,
			Long paymentMethodId, Long shippingMethodId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<OrderItem> iterator = order.getOrderItems().iterator();
		while (iterator.hasNext()) {
			OrderItem orderItem = (OrderItem) iterator.next();
			if ((orderItem != null) && (!StringUtils.isEmpty(orderItem.getSn())))
				continue;
			iterator.remove();
		}
		order.setArea((Area) this.areaService.find(areaId));
		order.setPaymentMethod((PaymentMethod) this.paymentMethodService.find(paymentMethodId));
		order.setShippingMethod((ShippingMethod) this.shippingMethodService.find(shippingMethodId));
		if (!beanValidator(order, new Class[0])) {
			map.put("message", Message.warn("admin.common.invalid", new Object[0]));
			return map;
		}
		Order localOrder = (Order) this.orderService.find(order.getId());
		if (localOrder == null) {
			map.put("message", Message.error("admin.common.invalid", new Object[0]));
			return map;
		}
		Iterator<OrderItem> iterator2 = order.getOrderItems().iterator();
		while (iterator2.hasNext()) {
			OrderItem orderItem = (OrderItem) iterator2.next();
			if (orderItem.getId() != null) {
				OrderItem localOrderItem = (OrderItem) this.orderItemService.find(orderItem.getId());
				if ((localOrderItem == null) || (!localOrder.equals(localOrderItem.getOrder()))) {
					map.put("message", Message.error("admin.common.invalid", new Object[0]));
					return map;
				}
				Product product = localOrderItem.getProduct();
				if ((product == null) || (product.getStock() == null))
					continue;
				if (localOrder.getIsAllocatedStock().booleanValue()) {
					if (orderItem.getQuantity().intValue() <= product.getAvailableStock().intValue()
							+ localOrderItem.getQuantity().intValue())
						continue;
					map.put("message", Message.warn("admin.order.lowStock", new Object[0]));
					return map;
				}
				if (orderItem.getQuantity().intValue() <= product.getAvailableStock().intValue())
					continue;
				map.put("message", Message.warn("admin.order.lowStock", new Object[0]));
				return map;
			}
			Product product = this.productService.findBySn(orderItem.getSn());
			if (product == null) {
				map.put("message",Message.error("admin.common.invalid", new Object[0]));
				return map;
			}
			if ((product.getStock() == null)
					|| (orderItem.getQuantity().intValue() <= product.getAvailableStock().intValue()))
				continue;
			map.put("message",Message.warn("admin.order.lowStock", new Object[0]));
			return map;
		}
		HashMap<String, OrderItem> orderItems = new HashMap<String, OrderItem>();
		Iterator<OrderItem> orderItemIterator = order.getOrderItems().iterator();
		while (orderItemIterator.hasNext()) {
			OrderItem orderItem = (OrderItem) orderItemIterator.next();
			orderItems.put(orderItem.getSn(), orderItem);
		}
		map.put("weight", Integer.valueOf(order.getWeight()));
		map.put("price", order.getPrice());
		map.put("quantity", Integer.valueOf(order.getQuantity()));
		map.put("amount", order.getAmount());
		map.put("orderItems", orderItems);
		map.put("message", ADMIN_SUCCESS);
		return map;
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(Order order, Long areaId, Long paymentMethodId,
			Long shippingMethodId, RedirectAttributes redirectAttributes) {
		Iterator<OrderItem> orderItemIterator = order.getOrderItems().iterator();
		while (orderItemIterator.hasNext()) {
			OrderItem orderItem = (OrderItem) orderItemIterator.next();
			if ((orderItem != null) && (!StringUtils.isEmpty(((OrderItem) orderItem).getSn())))
				continue;
			orderItemIterator.remove();
		}
		order.setArea((Area) this.areaService.find(areaId));
		order.setPaymentMethod((PaymentMethod) this.paymentMethodService.find(paymentMethodId));
		order.setShippingMethod((ShippingMethod) this.shippingMethodService.find(shippingMethodId));
		if (!beanValidator(redirectAttributes, order, new Class[0]))
			return ERROR_PAGE;
		Order localOrder = (Order) this.orderService.find(order.getId());
		if (localOrder == null)
			return ERROR_PAGE;
		if ((localOrder.isExpired()) || (localOrder.getOrderStatus() != Order.OrderStatus.unconfirmed))
			return ERROR_PAGE;
		Admin operator = this.adminService.getCurrent();
		if (localOrder.isLocked(operator))
			return ERROR_PAGE;
		if (!order.getIsInvoice().booleanValue()) {
			order.setInvoiceTitle(null);
			order.setTax(new BigDecimal(0));
		}
		Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
		while (localIterator.hasNext()) {
			OrderItem orderItem = (OrderItem) localIterator.next();
			if (orderItem.getId() != null) {
				OrderItem tempOrderItem = (OrderItem) this.orderItemService.find(orderItem.getId());
				if ((tempOrderItem == null) || (!localOrder.equals(tempOrderItem.getOrder())))
					return ERROR_PAGE;
				Product product = tempOrderItem.getProduct();
				if ((product != null) && (product.getStock() != null))
					if (localOrder.getIsAllocatedStock().booleanValue()) {
						if (orderItem.getQuantity().intValue() > product.getAvailableStock().intValue()
								+ tempOrderItem.getQuantity().intValue())
							return ERROR_PAGE;
					} else if (orderItem.getQuantity().intValue() > product.getAvailableStock().intValue())
						return ERROR_PAGE;
				BeanUtils.copyProperties(tempOrderItem, orderItem, new String[] { "price", "quantity" });
				if (!tempOrderItem.getIsGift().booleanValue())
					continue;
				orderItem.setPrice(new BigDecimal(0));
			} else {
				Product product = this.productService.findBySn(orderItem.getSn());
				if (product == null)
					return ERROR_PAGE;
				if ((product.getStock() != null)
						&& (orderItem.getQuantity().intValue() > product.getAvailableStock().intValue()))
					return ERROR_PAGE;
				orderItem.setName(product.getName());
				orderItem.setFullName(product.getFullName());
				if (product.getIsGift().booleanValue())
					orderItem.setPrice(new BigDecimal(0));
				orderItem.setWeight(product.getWeight());
				orderItem.setThumbnail(product.getThumbnail());
				orderItem.setIsGift(product.getIsGift());
				orderItem.setShippedQuantity(Integer.valueOf(0));
				orderItem.setReturnQuantity(Integer.valueOf(0));
				orderItem.setProduct(product);
				orderItem.setOrder(localOrder);
			}
		}
		order.setSn(localOrder.getSn());
		order.setOrderStatus(localOrder.getOrderStatus());
		order.setPaymentStatus(localOrder.getPaymentStatus());
		order.setShippingStatus(localOrder.getShippingStatus());
		order.setFee(localOrder.getFee());
		order.setAmountPaid(localOrder.getAmountPaid());
		order.setPromotion(localOrder.getPromotion());
		order.setExpire(localOrder.getExpire());
		order.setLockExpire(null);
		order.setIsAllocatedStock(localOrder.getIsAllocatedStock());
		order.setOperator(null);
		order.setMember(localOrder.getMember());
		order.setCouponCode(localOrder.getCouponCode());
		order.setCoupons(localOrder.getCoupons());
		order.setOrderLogs(localOrder.getOrderLogs());
		order.setDeposits(localOrder.getDeposits());
		order.setPayments(localOrder.getPayments());
		order.setRefunds(localOrder.getRefunds());
		order.setShippings(localOrder.getShippings());
		order.setReturns(localOrder.getReturns());
		this.orderService.update(order, operator);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Order.OrderStatus orderStatus,
			Order.PaymentStatus paymentStatus,
			Order.ShippingStatus shippingStatus, Boolean hasExpired,
			Pageable pageable, ModelMap model) {
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("paymentStatus", paymentStatus);
		model.addAttribute("shippingStatus", shippingStatus);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("page", this.orderService.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, pageable));
		return "/admin/order/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null) {
			Admin operator = this.adminService.getCurrent();
			for (Long id : ids) {
				Order order = (Order) this.orderService.find(id);
				if ((order != null) && (order.isLocked(operator)))
					return Message.error("admin.order.deleteLockedNotAllowed", new Object[] { order.getSn() });
			}
			this.orderService.delete(ids);
		}
		return ADMIN_SUCCESS;
	}
}