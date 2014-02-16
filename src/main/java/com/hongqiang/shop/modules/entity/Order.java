package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;

//订单实体类
@Entity
@Table(name = "hq_order")
public class Order extends BaseEntity {
	private static final long serialVersionUID = 8370942500343156156L;
	private static final String ORDER_STRING = " ";
	private String sn;// 订单编号
	private OrderStatus orderStatus;// 订单状态
	private PaymentStatus paymentStatus;// 支付状态
	private ShippingStatus shippingStatus;// 发货状态
	private BigDecimal fee;//
	private BigDecimal freight;// 配送费用
	private BigDecimal discount;// 折扣价
	private BigDecimal amountPaid;// 已付金额
	private Integer point;// 积分
	private String consignee;// 收件人
	private String areaName;// 城区地址，如“北京市西城区”，“新疆省乌鲁木齐市沙依巴克区”
	private String address;// 地址
	private String zipCode;// 邮编
	private String phone;// 手机
	private Boolean isInvoice;// 是否开具发票
	private String invoiceTitle;// 发票抬头
	private BigDecimal tax;// 税费
	private String memo;// 附言
	private String promotion;// 促销
	private Date expire;// 过期日期
	private Date lockExpire;// 锁定日期
	private Boolean isAllocatedStock;//
	private String paymentMethodName;// 付款方式名称
	private String shippingMethodName;// 送货方式名称
	private Area area;// 地区码
	private PaymentMethod paymentMethod;// 付款方式
	private ShippingMethod shippingMethod;// 送货方式
	private Admin operator;// 操作员
	private Member member;// 会员
	private CouponCode couponCode;// 优惠码
	private List<Coupon> coupons = new ArrayList<Coupon>();// 团购
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();// 订单项
	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();// 订单日志
	private Set<Deposit> deposits = new HashSet<Deposit>();// 预存款
	private Set<Payment> payments = new HashSet<Payment>();// 付款
	private Set<Refunds> refunds = new HashSet<Refunds>();// 资金
	private Set<Shipping> shippings = new HashSet<Shipping>();// 运输
	private Set<Returns> returns = new HashSet<Returns>();//

	// 订单状态（未确认、已确认、已完成、已取消）
	public enum OrderStatus {
		unconfirmed, confirmed, completed, cancelled;
	}

	// 付款状态（未支付、部分支付、已支付、部分退款、全额退款）
	public enum PaymentStatus {
		unpaid, partialPayment, paid, partialRefunds, refunded;
	}

	// 配送状态（未发货、部分发货、已发货、部分退货、已退货）
	public enum ShippingStatus {
		unshipped, partialShipment, shipped, partialReturns, returned;
	}

	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false)
	public Order.OrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(Order.OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(nullable = false)
	public Order.PaymentStatus getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(Order.PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(nullable = false)
	public Order.ShippingStatus getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(Order.ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return this.fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@NotNull
	@Min(0L)
//	 @Digits(integer=12, fraction=3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return this.freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	@NotNull
	@Min(0L)
//	 @Digits(integer=12, fraction=3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	@NotNull
	@Min(0L)
	@Column(nullable = false)
	public Integer getPoint() {
		return this.point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@Column(nullable = false)
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsInvoice() {
		return this.isInvoice;
	}

	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	@Length(max = 200)
	public String getInvoiceTitle() {
		return this.invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	@Min(0L)
//	 @Digits(integer=12, fraction=3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getTax() {
		return this.tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	@Length(max = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(updatable = false)
	public String getPromotion() {
		return this.promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
		
	}

	public Date getExpire() {
		return this.expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Date getLockExpire() {
		return this.lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	@Column(nullable = false)
	public Boolean getIsAllocatedStock() {
		return this.isAllocatedStock;
	}

	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	@Column(nullable = false)
	public String getPaymentMethodName() {
		return this.paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	@Column(nullable = false)
	public String getShippingMethodName() {
		return this.shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public ShippingMethod getShippingMethod() {
		return this.shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Admin getOperator() {
		return this.operator;
	}

	public void setOperator(Admin operator) {
		this.operator = operator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public CouponCode getCouponCode() {
		return this.couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hq_order_coupon")
	public List<Coupon> getCoupons() {
		return this.coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL }, orphanRemoval = true)
	@OrderBy("isGift asc")
	public List<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<OrderLog> getOrderLogs() {
		return this.orderLogs;
	}

	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	public Set<Deposit> getDeposits() {
		return this.deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Refunds> getRefunds() {
		return this.refunds;
	}

	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Shipping> getShippings() {
		return this.shippings;
	}

	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Returns> getReturns() {
		return this.returns;
	}

	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}

	@Transient
	public String getProductName() {
		StringBuffer stringBuffer = new StringBuffer();
		if (getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if ((orderItem == null) || (orderItem.getFullName() == null))
					continue;
				stringBuffer.append(ORDER_STRING).append(orderItem.getFullName());
			}
			if (stringBuffer.length() > 0)
				stringBuffer.deleteCharAt(0);
		}
		return stringBuffer.toString();
	}

	@Transient
	public int getWeight() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if (orderItem == null)
					continue;
				i += orderItem.getTotalWeight();
			}
		}
		return i;
	}

	@Transient
	public int getQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if ((orderItem == null) || (orderItem.getQuantity() == null))
					continue;
				i += orderItem.getQuantity().intValue();
			}
		}
		return i;
	}

	@Transient
	public int getShippedQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if ((orderItem == null) || (orderItem.getShippedQuantity() == null))
					continue;
				i += orderItem.getShippedQuantity().intValue();
			}
		}
		return i;
	}

	@Transient
	public int getReturnQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if ((orderItem == null) || (orderItem.getReturnQuantity() == null))
					continue;
				i += orderItem.getReturnQuantity().intValue();
			}
		}
		return i;
	}

	//购物车内商品总价
	@Transient
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if ((orderItem == null) || (orderItem.getSubtotal() == null))
					continue;
				price = price.add(orderItem.getSubtotal());
			}
		}
		return price;
	}

	//待付款金额
	@Transient
	public BigDecimal getAmount() {
		BigDecimal amount = getPrice().subtract(getDiscount() != null ? getDiscount() : new BigDecimal(0))
				.add(getFreight() != null ? getFreight() : new BigDecimal(0))
				.add(getFee() != null ? getFee() : new BigDecimal(0))
				.add(getTax() != null ? getTax() : new BigDecimal(0));
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}

	@Transient
	public BigDecimal getAmountPayable() {
		BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
		return amountPayable.compareTo(new BigDecimal(0)) > 0 ? amountPayable : new BigDecimal(0);
	}

	@Transient
	public boolean isExpired() {
		return (getExpire() != null) && (new Date().after(getExpire()));
	}

	@Transient
	public OrderItem getOrderItem(String sn) {
		if ((sn != null) && (getOrderItems() != null)) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if ((orderItem != null) && (sn.equalsIgnoreCase(orderItem.getSn())))
					return orderItem;
			}
		}
		return null;
	}

	@Transient
	public boolean isLocked(Admin operator) {
		return (getLockExpire() != null)
				&& (new Date().before(getLockExpire()))
				&& (getOperator() != operator);
	}

	@Transient
	public BigDecimal calculateTax() {
		Setting setting = SettingUtils.get();
		BigDecimal tax = null;
		if (setting.getIsTaxPriceEnabled().booleanValue())
			tax = getPrice().subtract(getDiscount()).multiply(new BigDecimal(setting.getTaxRate().toString()));
		else
			tax = new BigDecimal(0);
		return setting.setScale(tax);
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		if (getArea() != null)
			setAreaName(getArea().getFullName());
		if (getPaymentMethod() != null)
			setPaymentMethodName(getPaymentMethod().getName());
		if (getShippingMethod() != null)
			setShippingMethodName(getShippingMethod().getName());
	}

	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
		if (getArea() != null)
			setAreaName(getArea().getFullName());
		if (getPaymentMethod() != null)
			setPaymentMethodName(getPaymentMethod().getName());
		if (getShippingMethod() != null)
			setShippingMethodName(getShippingMethod().getName());
	}

	@PreRemove
	public void preRemove() {
		Set<Deposit> deposits = getDeposits();
		if (deposits != null) {
			Iterator<Deposit> iterator = deposits.iterator();
			while (iterator.hasNext()) {
				Deposit deposit = (Deposit) iterator.next();
				deposit.setOrder(null);
			}
		}
	}
}