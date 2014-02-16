package com.hongqiang.shop.modules.shipping.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.LockModeType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.modules.account.dao.CartDao;
import com.hongqiang.shop.modules.account.dao.CouponCodeDao;
import com.hongqiang.shop.modules.account.dao.PaymentDao;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.CartItem;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Deposit;
import com.hongqiang.shop.modules.entity.GiftItem;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.entity.OrderItem;
import com.hongqiang.shop.modules.entity.OrderLog;
import com.hongqiang.shop.modules.entity.Payment;
import com.hongqiang.shop.modules.entity.PaymentMethod;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.entity.Receiver;
import com.hongqiang.shop.modules.entity.Refunds;
import com.hongqiang.shop.modules.entity.Returns;
import com.hongqiang.shop.modules.entity.ReturnsItem;
import com.hongqiang.shop.modules.entity.Shipping;
import com.hongqiang.shop.modules.entity.ShippingItem;
import com.hongqiang.shop.modules.entity.ShippingMethod;
import com.hongqiang.shop.modules.entity.Sn;
import com.hongqiang.shop.modules.product.dao.ProductDao;
import com.hongqiang.shop.modules.product.dao.SnDao;
import com.hongqiang.shop.modules.shipping.dao.OrderDao;
import com.hongqiang.shop.modules.shipping.dao.OrderItemDao;
import com.hongqiang.shop.modules.shipping.dao.OrderLogDao;
import com.hongqiang.shop.modules.shipping.dao.RefundsDao;
import com.hongqiang.shop.modules.shipping.dao.ReturnsDao;
import com.hongqiang.shop.modules.shipping.dao.ShippingDao;
import com.hongqiang.shop.modules.user.dao.DepositDao;
import com.hongqiang.shop.modules.user.dao.MemberDao;
import com.hongqiang.shop.modules.user.dao.MemberRankDao;
import com.hongqiang.shop.modules.util.service.StaticService;

@Service
public class OrderServiceImpl extends BaseService implements OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private OrderLogDao orderLogDao;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private CouponCodeDao couponCodeDao;

	@Autowired
	private SnDao snDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private MemberRankDao memberRankDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private DepositDao depositDao;

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private RefundsDao refundsDao;

	@Autowired
	private ShippingDao shippingDao;

	@Autowired
	private ReturnsDao returnsDao;

	@Autowired
	private StaticService staticService;

	@Transactional(readOnly = true)
	public com.hongqiang.shop.modules.entity.Order findBySn(String sn) {
		return this.orderDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<com.hongqiang.shop.modules.entity.Order> findList(
			Member member, Integer count, List<Filter> filters,List<Order> orders) {
		return this.orderDao.findList(member, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<com.hongqiang.shop.modules.entity.Order> findPage(Member member, Pageable pageable) {
		return this.orderDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<com.hongqiang.shop.modules.entity.Order> findPage(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired, Pageable pageable) {
		return this.orderDao.findPage(orderStatus, paymentStatus,shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired) {
		return this.orderDao.count(orderStatus, paymentStatus, shippingStatus,hasExpired);
	}

	@Transactional(readOnly = true)
	public Long waitingPaymentCount(Member member) {
		return this.orderDao.waitingPaymentCount(member);
	}

	@Transactional(readOnly = true)
	public Long waitingShippingCount(Member member) {
		return this.orderDao.waitingShippingCount(member);
	}

	@Transactional(readOnly = true)
	public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
		return this.orderDao.getSalesAmount(beginDate, endDate);
	}

	@Transactional(readOnly = true)
	public Integer getSalesVolume(Date beginDate, Date endDate) {
		return this.orderDao.getSalesVolume(beginDate, endDate);
	}

	@Transactional
	public void releaseStock() {
		this.orderDao.releaseStock();
	}

	@Transactional
	public com.hongqiang.shop.modules.entity.Order build(Cart cart,
			Receiver receiver, PaymentMethod paymentMethod,
			ShippingMethod shippingMethod, CouponCode couponCode,
			boolean isInvoice, String invoiceTitle, boolean useBalance,
			String memo) {
		com.hongqiang.shop.modules.entity.Order order = new com.hongqiang.shop.modules.entity.Order();
		if (cart == null || cart.getMember() == null || cart.getCartItems().isEmpty()) {
			return order;
		}
		//设置订单的运输状态，费用，折扣，积分，备注，会员信息
		order.setShippingStatus(com.hongqiang.shop.modules.entity.Order.ShippingStatus.unshipped);
		order.setFee(new BigDecimal(0));
		order.setDiscount(cart.getDiscount());
		order.setPoint(Integer.valueOf(cart.getPoint()));
		order.setMemo(memo);
		order.setMember(cart.getMember());
		//设置订单的收件人信息
		if (receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}
		//设置定的促销信息
		if (!cart.getPromotions().isEmpty()) {
			StringBuffer stringBuffer = new StringBuffer();
			Iterator<Promotion> promotionIterator = cart.getPromotions().iterator();
			while (promotionIterator.hasNext()) {
				Promotion promotion = (Promotion) promotionIterator.next();
				if ((promotion == null) || (promotion.getName() == null))
					continue;
				stringBuffer.append(" " + promotion.getName());
			}
			if (stringBuffer.length() > 0)
				 stringBuffer.deleteCharAt(0);
			order.setPromotion(stringBuffer.toString());
		}
		//设置订单的付款方式
		order.setPaymentMethod(paymentMethod);
		//设置订单的配送方式和配送费
		if ((shippingMethod != null) && (paymentMethod != null)
				&& (paymentMethod.getShippingMethods().contains(shippingMethod))) {
			//根据重量计算运输价格
			BigDecimal freightPrice = shippingMethod.calculateFreight(Integer.valueOf(cart.getWeight()));
			Iterator<Promotion> iterator = cart.getPromotions().iterator();
			while (iterator.hasNext()) {
				Promotion promotion = (Promotion) iterator.next();
				if (!promotion.getIsFreeShipping().booleanValue())
					continue;
				freightPrice = new BigDecimal(0);
				break;
			}
			order.setFreight(freightPrice);
			order.setShippingMethod(shippingMethod);
		} else {
			order.setFreight(new BigDecimal(0));
		}
		//使用优惠劵
		if ((couponCode != null) && (cart.isCouponAllowed())) {
			this.couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_READ);
			if ((!couponCode.getIsUsed().booleanValue())
					&& (couponCode.getCoupon() != null)
					&& (cart.isValid(couponCode.getCoupon()))) {
				//计算使用优惠劵后的价格
				BigDecimal realPrice = couponCode.getCoupon().calculatePrice(cart.getAmount());
				BigDecimal discount = cart.getAmount().subtract(realPrice);
				if (discount.compareTo(new BigDecimal(0)) > 0)
					order.setDiscount(cart.getDiscount().add(discount));
				order.setCouponCode(couponCode);
			}
		}
		//从cartItem和giftItem中得到商品，放入orderItems
		List<OrderItem> orderItems = order.getOrderItems();
		Iterator<CartItem> cartItemIterator = cart.getCartItems().iterator();
		while (cartItemIterator.hasNext()) {
			CartItem cartItem = (CartItem) cartItemIterator.next();
			if ((cartItem == null) || (cartItem.getProduct() == null))
				continue;
			Product product =  cartItem.getProduct();
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setFullName(product.getFullName());
			orderItem.setPrice(cartItem.getUnitPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setIsGift(Boolean.valueOf(false));
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setShippedQuantity(Integer.valueOf(0));
			orderItem.setReturnQuantity(Integer.valueOf(0));
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}
		Iterator<GiftItem> gitftItemIterator = cart.getGiftItems().iterator();
		while (gitftItemIterator.hasNext()) {
			GiftItem giftItem = (GiftItem) gitftItemIterator.next();
			if ((giftItem == null) || (giftItem.getGift() == null))
				continue;
			Product product = giftItem.getGift();
			OrderItem orderItem = new OrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setFullName(product.getFullName());
			orderItem.setPrice(new BigDecimal(0));
			orderItem.setWeight(product.getWeight());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setIsGift(Boolean.valueOf(true));
			orderItem.setQuantity(giftItem.getQuantity());
			orderItem.setShippedQuantity(Integer.valueOf(0));
			orderItem.setReturnQuantity(Integer.valueOf(0));
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}
		//设置发票项
		Setting setting = SettingUtils.get();
		if ((setting.getIsInvoiceEnabled().booleanValue()) && (isInvoice) && (StringUtils.isNotEmpty(invoiceTitle))) {
			order.setIsInvoice(Boolean.valueOf(true));
			order.setInvoiceTitle(invoiceTitle);
			order.setTax(order.calculateTax());
		} else {
			order.setIsInvoice(Boolean.valueOf(false));
			order.setTax(new BigDecimal(0));
		}
		//设置账户余额项
		if (useBalance) {
			Member member = cart.getMember();
			if (member.getBalance().compareTo(order.getAmount()) >= 0)
				order.setAmountPaid(order.getAmount());
			else
				order.setAmountPaid(member.getBalance());
		} else {
			order.setAmountPaid(new BigDecimal(0));
		}
		//设置订单状态和支付状态
		if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
			order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.confirmed);
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid);
		} else if ((order.getAmountPayable().compareTo(new BigDecimal(0)) > 0)
				&& (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0)) {
			order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.confirmed);
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment);
		} else {
			order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.unconfirmed);
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.unpaid);
		}
		//如果订单未支付，设置订单失效时间
		if ((paymentMethod != null) && (paymentMethod.getTimeout() != null)
				&& (order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.unpaid))
			order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout().intValue()));
		return order;
	}

	@Transactional
	public com.hongqiang.shop.modules.entity.Order create(Cart cart,Receiver receiver, PaymentMethod paymentMethod,
			ShippingMethod shippingMethod, CouponCode couponCode,boolean isInvoice, String invoiceTitle, boolean useBalance,
			String memo, Admin operator) {
		com.hongqiang.shop.modules.entity.Order order = new com.hongqiang.shop.modules.entity.Order();
		//购物车不存在或者接收人为空，则返回空
		if (cart == null || cart.getMember() == null
				|| cart.getCartItems().isEmpty() || receiver == null
				|| paymentMethod == null || shippingMethod == null) {
			return order;
		}
		//根据购物车，接收人，付款方式，配送方式，优惠劵，发票，账户余额项生成订单
		order = build(cart, receiver, paymentMethod, shippingMethod,
				couponCode, isInvoice, invoiceTitle, useBalance, memo);
		order.setSn(this.snDao.generate(Sn.Type.orders));
		//付款方式为在线付款时
		if (paymentMethod.getType() == PaymentMethod.Type.online) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 10));
			order.setOperator(operator);
		}
		//设置优惠劵
		if (order.getCouponCode() != null) {
			couponCode.setIsUsed(Boolean.valueOf(true));
			couponCode.setUsedDate(new Date());
			this.couponCodeDao.merge(couponCode);
		}
		//设置促销
		Iterator<Promotion> promotionIterator = cart.getPromotions().iterator();
		while (promotionIterator.hasNext()) {
			Promotion promotion = (Promotion) promotionIterator.next();
			Iterator<Coupon> couponIterator = promotion.getCoupons().iterator();
			while (couponIterator.hasNext()) {
				Coupon coupon = (Coupon) couponIterator.next();
				order.getCoupons().add(coupon);
			}
		}
		//
		Setting setting = SettingUtils.get();
		if ((setting.getStockAllocationTime() == Setting.StockAllocationTime.order)
				|| ((setting.getStockAllocationTime() == Setting.StockAllocationTime.payment)
						&& ((order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment) 
								|| (order.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid))))
			order.setIsAllocatedStock(Boolean.valueOf(true));
		else
			order.setIsAllocatedStock(Boolean.valueOf(false));
		//保存订单
		this.orderDao.persist(order);
		//存入订单日志表中
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
		//如果会员账号余额大于订单金额，则从账号余额中扣除
		Member member = cart.getMember();
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().subtract(order.getAmountPaid()));
			this.memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(order.getAmountPaid());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			this.depositDao.persist(deposit);
		}
		//设置订单中的商品的可分配库存
		if ((setting.getStockAllocationTime() == Setting.StockAllocationTime.order)
				|| ((setting.getStockAllocationTime() == Setting.StockAllocationTime.payment) && ((order
						.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment) || (order
						.getPaymentStatus() == com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid)))) {
			Iterator<OrderItem> iterator = order.getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product,LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										+ (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
		}
		//从购物车中移除商品
		this.cartDao.remove(cart);
		return order;
	}

	@Transactional
	public void update(com.hongqiang.shop.modules.entity.Order order, Admin operator) {
		if (order == null) {
			return;
		}
		com.hongqiang.shop.modules.entity.Order localOrder = (com.hongqiang.shop.modules.entity.Order) this.orderDao.find(order.getId());
		//订单的商品为可分配库存的状态,则修改商品的库存
		if (localOrder.getIsAllocatedStock().booleanValue()) {
			Iterator<OrderItem> iterator = localOrder.getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product,LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										- (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product,LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										+ (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
		}
		this.orderDao.merge(order);
		//存入订单日志表中
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.modify);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void confirm(com.hongqiang.shop.modules.entity.Order order, Admin operator) {
		if (order == null) {
			return;
		}
		order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.confirmed);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.confirm);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void complete(com.hongqiang.shop.modules.entity.Order order, Admin operator) {
		if (order == null) {
			return;
		}
		Member member = order.getMember();
		this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		//如果订单的配送状态为部分送达或者完成配送，就更改用户的积分，并设置优惠劵已被该用户使用
		if ((order.getShippingStatus() == com.hongqiang.shop.modules.entity.Order.ShippingStatus.partialShipment)
				|| (order.getShippingStatus() == com.hongqiang.shop.modules.entity.Order.ShippingStatus.shipped)) {
			member.setPoint(Long.valueOf(member.getPoint().longValue() + order.getPoint().intValue()));
			Iterator<Coupon> localIterator = order.getCoupons().iterator();
			while (localIterator.hasNext()) {
				Coupon coupon = (Coupon) localIterator.next();
				this.couponCodeDao.build(coupon, member);
			}
		}
		//如果订单的配送状态为未配送或者退货状态，则把订单中的优惠劵返回
		if ((order.getShippingStatus() == com.hongqiang.shop.modules.entity.Order.ShippingStatus.unshipped)
				|| (order.getShippingStatus() == com.hongqiang.shop.modules.entity.Order.ShippingStatus.returned)) {
			CouponCode couponCode = order.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(Boolean.valueOf(false));
				couponCode.setUsedDate(null);
				this.couponCodeDao.merge(couponCode);
				order.setCouponCode(null);
				this.orderDao.merge(order);
			}
		}
		//设置会员金额
		member.setAmount(member.getAmount().add(order.getAmountPaid()));
		//设置会员等级
		if (!member.getMemberRank().getIsSpecial().booleanValue()) {
			MemberRank memberRank = this.memberRankDao.findByAmount(member.getAmount());
			if ((memberRank != null) && (memberRank.getAmount().compareTo(member.getMemberRank().getAmount()) > 0))
				member.setMemberRank(memberRank);
		}
		this.memberDao.merge(member);
		//设置商品的可分配库存数量
		if (order.getIsAllocatedStock().booleanValue()) {
			Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product,LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										- (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			order.setIsAllocatedStock(Boolean.valueOf(false));
		}
		Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
		while (localIterator.hasNext()) {
			OrderItem orderItem = (OrderItem) localIterator.next();
			if (orderItem == null)
				continue;
			Product product = orderItem.getProduct();
			this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
			if (product == null)
				continue;
			//设置月销量和周销量
			Integer quantity = orderItem.getQuantity();
			Calendar currentDate = Calendar.getInstance();
			Calendar weekSalesDate = Calendar.getInstance();
			weekSalesDate.setTime(product.getWeekSalesDate());
			Calendar monthSalesDate = Calendar.getInstance();
			monthSalesDate.setTime(product.getMonthSalesDate());
			if ((currentDate.get(Calendar.YEAR) != weekSalesDate.get(Calendar.YEAR)) 
					|| (currentDate.get(Calendar.WEEK_OF_YEAR) > weekSalesDate.get(Calendar.WEEK_OF_YEAR)))
				product.setWeekSales(Long.valueOf(quantity.intValue()));
			else
				product.setWeekSales(Long.valueOf(product.getWeekSales().longValue() + quantity.intValue()));
			if ((currentDate.get(Calendar.YEAR) != monthSalesDate.get(Calendar.YEAR)) 
					|| (currentDate.get(Calendar.MONTH) > monthSalesDate.get(Calendar.MONTH)))
				product.setMonthSales(Long.valueOf(quantity.intValue()));
			else
				product.setMonthSales(Long.valueOf(product.getMonthSales().longValue() + quantity.intValue()));
			//设置总销量
			product.setSales(Long.valueOf(product.getSales().longValue() + quantity.intValue()));
			product.setWeekSalesDate(new Date());
			product.setMonthSalesDate(new Date());
			this.productDao.merge(product);
			this.orderDao.flush();
			this.staticService.build(product);
		}
		order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.completed);
		order.setExpire(null);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void cancel(com.hongqiang.shop.modules.entity.Order order, Admin operator) {
		if (order == null) {
			return;
		}
		//设置优惠劵
		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			couponCode.setIsUsed(Boolean.valueOf(false));
			couponCode.setUsedDate(null);
			this.couponCodeDao.merge(couponCode);
			order.setCouponCode(null);
			this.orderDao.merge(order);
		}
		//设置商品可用库存
		if (order.getIsAllocatedStock().booleanValue()) {
			Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										- (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			order.setIsAllocatedStock(Boolean.valueOf(false));
		}
		order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.cancelled);
		order.setExpire(null);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.cancel);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void payment(com.hongqiang.shop.modules.entity.Order order, Payment payment, Admin operator) {
		if (order == null || payment == null) {
			return;
		}
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		payment.setOrder(order);
		this.paymentDao.merge(payment);
		//设置会员账号预存款
		if (payment.getType() == Payment.Type.deposit) {
			Member member = order.getMember();
			this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().subtract(payment.getAmount()));
			this.memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(payment.getAmount());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			this.depositDao.persist(deposit);
		}
		//设置商品的可分配库存数量
		Setting setting = SettingUtils.get();
		if ((!order.getIsAllocatedStock().booleanValue())
				&& (((Setting) setting).getStockAllocationTime() == Setting.StockAllocationTime.payment)) {
			Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
						+ (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			order.setIsAllocatedStock(Boolean.valueOf(true));
		}
		//设置订单总额
		order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
		order.setFee(payment.getFee());
		order.setExpire(null);
		//设置订单状态和支付状态
		if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
			order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.confirmed);
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid);
		} else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(com.hongqiang.shop.modules.entity.Order.OrderStatus.confirmed);
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment);
		}
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.payment);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void refunds(com.hongqiang.shop.modules.entity.Order order, Refunds refunds, Admin operator) {
		if (order == null || refunds == null) {
			return;
		}
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		refunds.setOrder(order);
		this.refundsDao.persist(refunds);
		//退款类型为预付款，则把金额充值进账户余额
		if (refunds.getType() == Refunds.Type.deposit) {
			Member member = order.getMember();
			this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().add(refunds.getAmount()));
			this.memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.adminRefunds);
			deposit.setCredit(refunds.getAmount());
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember((Member) member);
			deposit.setOrder(order);
			this.depositDao.persist(deposit);
		}
		order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) == 0)
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.refunded);
		else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0)
			order.setPaymentStatus(com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialRefunds);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.refunds);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void shipping(com.hongqiang.shop.modules.entity.Order order, Shipping shipping, Admin operator) {
		if (order == null || shipping == null || shipping.getShippingItems().isEmpty()) {
			return;
		}
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		Setting setting = SettingUtils.get();
		if ((!order.getIsAllocatedStock().booleanValue())
				&& (setting.getStockAllocationTime() == Setting.StockAllocationTime.ship)) {
			Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										+ (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			order.setIsAllocatedStock(Boolean.valueOf(true));
		}
		shipping.setOrder(order);
		this.shippingDao.persist(shipping);
		Iterator<ShippingItem> localIterator = shipping.getShippingItems().iterator();
		while (localIterator.hasNext()) {
			ShippingItem shippingItem = (ShippingItem) localIterator.next();
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null)
				continue;
			Product product = orderItem.getProduct();
			this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
			if (product != null) {
				if (product.getStock() != null) {
					product.setStock(Integer.valueOf(product.getStock().intValue() - shippingItem.getQuantity().intValue()));
					if (order.getIsAllocatedStock().booleanValue())
						product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										- shippingItem.getQuantity().intValue()));
				}
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			this.orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
			orderItem.setShippedQuantity(Integer.valueOf(orderItem.getShippedQuantity().intValue()
							+ shippingItem.getQuantity().intValue()));
		}
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setShippingStatus(com.hongqiang.shop.modules.entity.Order.ShippingStatus.shipped);
			order.setIsAllocatedStock(Boolean.valueOf(false));
		} else if (order.getShippedQuantity() > 0) {
			order.setShippingStatus(com.hongqiang.shop.modules.entity.Order.ShippingStatus.partialShipment);
		}
		order.setExpire(null);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional
	public void returns(com.hongqiang.shop.modules.entity.Order order, Returns returns, Admin operator) {
		if (order == null || returns == null || returns.getReturnsItems().isEmpty()) {
			return;
		}
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		returns.setOrder(order);
		this.returnsDao.persist(returns);
		Iterator<ReturnsItem> localIterator = returns.getReturnsItems() .iterator();
		while (localIterator.hasNext()) {
			ReturnsItem returnsItem = (ReturnsItem) localIterator.next();
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null)
				continue;
			this.orderItemDao.lock(orderItem,LockModeType.PESSIMISTIC_WRITE);
			orderItem.setReturnQuantity(Integer.valueOf(orderItem.getReturnQuantity().intValue()
							+ returnsItem.getQuantity().intValue()));
		}
		if (order.getReturnQuantity() >= order.getShippedQuantity())
			order.setShippingStatus(com.hongqiang.shop.modules.entity.Order.ShippingStatus.returned);
		else if (order.getReturnQuantity() > 0)
			order.setShippingStatus(com.hongqiang.shop.modules.entity.Order.ShippingStatus.partialReturns);
		order.setExpire(null);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.returns);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	@Transactional(readOnly = true)
	public com.hongqiang.shop.modules.entity.Order find(Long id) {
		return this.orderDao.find(id);
	}

	@Transactional
	public void save(com.hongqiang.shop.modules.entity.Order order) {
		this.orderDao.persist(order);
	}

	@Transactional
	public com.hongqiang.shop.modules.entity.Order update(
			com.hongqiang.shop.modules.entity.Order order) {
		return (com.hongqiang.shop.modules.entity.Order) this.orderDao.merge(order);
	}

	@Transactional
	public com.hongqiang.shop.modules.entity.Order update(
			com.hongqiang.shop.modules.entity.Order order,
			String[] ignoreProperties) {
		return (com.hongqiang.shop.modules.entity.Order) this.orderDao.update(order, ignoreProperties);
	}

	@Transactional
	public void delete(com.hongqiang.shop.modules.entity.Order order) {
		if (order.getIsAllocatedStock().booleanValue()) {
			Iterator<OrderItem> localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product,LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
										- (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
		}
		this.orderDao.delete(order);
	}

	public void delete(Long id) {
		com.hongqiang.shop.modules.entity.Order order = this.find(id);
		this.delete(order);
	}

	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				delete(id);
	}
}