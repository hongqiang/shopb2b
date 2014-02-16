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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;

//团购实体类
@Entity
@Table(name = "hq_coupon")
public class Coupon extends BaseEntity {
	private static final long serialVersionUID = -7907808728349149722L;
	private String name;// 团购名称
	private String prefix;// 后缀
	private Date beginDate;// 开始日期
	private Date endDate;// 结束日期
	private BigDecimal startPrice;// 开始价格
	private BigDecimal endPrice;// 结束价格
	private Boolean isEnabled;// 是否可用
	private Boolean isExchange;// 是否可交换
	private Integer point;// 返回点数
	private Operator priceOperator;// 价格操作员
	private BigDecimal priceValue;//
	private String introduction;// 介绍
	private Set<CouponCode> couponCodes = new HashSet<CouponCode>();// 团购代码
	private Set<Promotion> promotions = new HashSet<Promotion>();// 促销
	private List<Order> orders = new ArrayList<Order>();// 订单

	public enum Operator {
		add, subtract, multiply, divide;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Min(0L)
//	 @Digits(integer=12, fraction=3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getStartPrice() {
		return this.startPrice;
	}

	public void setStartPrice(BigDecimal startPrice) {
		this.startPrice = startPrice;
	}

	@Min(0L)
//	 @Digits(integer=12, fraction=3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getEndPrice() {
		return this.endPrice;
	}

	public void setEndPrice(BigDecimal endPrice) {
		this.endPrice = endPrice;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsExchange() {
		return this.isExchange;
	}

	public void setIsExchange(Boolean isExchange) {
		this.isExchange = isExchange;
	}

	@Min(0L)
	public Integer getPoint() {
		return this.point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	@NotNull
	@Column(nullable = false)
	public Coupon.Operator getPriceOperator() {
		return this.priceOperator;
	}

	public void setPriceOperator(Coupon.Operator priceOperator) {
		this.priceOperator = priceOperator;
	}

//	 @Digits(integer=12, fraction=3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getPriceValue() {
		return this.priceValue;
	}

	public void setPriceValue(BigDecimal priceValue) {
		this.priceValue = priceValue;
	}

	@Lob
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<CouponCode> getCouponCodes() {
		return this.couponCodes;
	}

	public void setCouponCodes(Set<CouponCode> couponCodes) {
		this.couponCodes = couponCodes;
	}

	@ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
	public List<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Transient
	public boolean hasBegun() {
		return (getBeginDate() == null) || (new Date().after(getBeginDate()));
	}

	@Transient
	public boolean hasExpired() {
		return (getEndDate() != null) && (new Date().after(getEndDate()));
	}

	//使用优惠劵后的价格
	@Transient
	public BigDecimal calculatePrice(BigDecimal price) {
		BigDecimal couponPrice = null;
		if ((price != null) && (getPriceOperator() != null) && (getPriceValue() != null)) {
			Setting setting = SettingUtils.get();
			if (getPriceOperator() == Coupon.Operator.add)
				couponPrice = price.add(getPriceValue());
			else if (getPriceOperator() == Coupon.Operator.subtract)
				couponPrice = price.subtract(getPriceValue());
			else if (getPriceOperator() == Coupon.Operator.multiply)
				couponPrice = price.multiply(getPriceValue());
			else
				couponPrice = price.divide(getPriceValue());
			BigDecimal realPrice = setting.setScale(couponPrice);
			return realPrice.compareTo(new BigDecimal(0)) > 0 ? realPrice : new BigDecimal(0);
		}
		return price;
	}

	@PreRemove
	public void preRemove() {
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			Iterator<Promotion> iterator = promotions.iterator();
			while (iterator.hasNext()) {
				Promotion promotion = (Promotion) iterator.next();
				promotion.getCoupons().remove(this);
			}
		}
		List<Order> orders = getOrders();
		if (orders != null) {
			Iterator<Order> iterator = orders.iterator();
			while (iterator.hasNext()) {
				Order order = (Order) iterator.next();
				order.getCoupons().remove(this);
			}
		}
	}
}