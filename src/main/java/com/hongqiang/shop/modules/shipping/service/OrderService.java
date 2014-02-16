package com.hongqiang.shop.modules.shipping.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Payment;
import com.hongqiang.shop.modules.entity.PaymentMethod;
import com.hongqiang.shop.modules.entity.Receiver;
import com.hongqiang.shop.modules.entity.Refunds;
import com.hongqiang.shop.modules.entity.Returns;
import com.hongqiang.shop.modules.entity.Shipping;
import com.hongqiang.shop.modules.entity.ShippingMethod;

public interface OrderService {
	public com.hongqiang.shop.modules.entity.Order findBySn(String sn);

	public List<com.hongqiang.shop.modules.entity.Order> findList(
			Member member, Integer count, List<Filter> filters,List<Order> orders);

	public Page<com.hongqiang.shop.modules.entity.Order> findPage(Member member, Pageable pageable);

	public Page<com.hongqiang.shop.modules.entity.Order> findPage(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired, Pageable pageable);

	public Long count(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired);

	public Long waitingPaymentCount(Member member);

	public Long waitingShippingCount(Member member);

	public BigDecimal getSalesAmount(Date beginDate, Date endDate);

	public Integer getSalesVolume(Date beginDate, Date endDate);

	public void releaseStock();

	public com.hongqiang.shop.modules.entity.Order build(Cart cart,
			Receiver receiver, PaymentMethod paymentMethod,
			ShippingMethod shippingMethod, CouponCode couponCode,
			boolean isInvoice, String invoiceTitle, boolean useBalance,
			String memo);

	public com.hongqiang.shop.modules.entity.Order create(Cart cart,Receiver receiver, PaymentMethod paymentMethod,
			ShippingMethod shippingMethod, CouponCode couponCode,boolean isInvoice, String invoiceTitle, boolean useBalance,
			String memo, Admin operator);

	public void update(com.hongqiang.shop.modules.entity.Order order, Admin operator);

	public void confirm(com.hongqiang.shop.modules.entity.Order order, Admin operator);

	public void complete(com.hongqiang.shop.modules.entity.Order order, Admin operator);

	public void cancel(com.hongqiang.shop.modules.entity.Order order, Admin operator);

	public void payment(com.hongqiang.shop.modules.entity.Order order, Payment payment, Admin operator);

	public void refunds(com.hongqiang.shop.modules.entity.Order order, Refunds refunds, Admin operator);

	public void shipping(com.hongqiang.shop.modules.entity.Order order, Shipping shipping, Admin operator);

	public void returns(com.hongqiang.shop.modules.entity.Order order, Returns returns, Admin operator);

	public com.hongqiang.shop.modules.entity.Order find(Long id);

	public void save(com.hongqiang.shop.modules.entity.Order order);

	public com.hongqiang.shop.modules.entity.Order update(com.hongqiang.shop.modules.entity.Order order);

	public com.hongqiang.shop.modules.entity.Order update(
			com.hongqiang.shop.modules.entity.Order order,
			String[] ignoreProperties);

	public void delete(com.hongqiang.shop.modules.entity.Order order);

	public void delete(Long id);

	public void delete(Long[] ids);
}