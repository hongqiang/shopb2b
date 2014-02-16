package com.hongqiang.shop.modules.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.common.interceptor.MemberInterceptor;
import com.hongqiang.shop.common.utils.JsonUtils;
import com.hongqiang.shop.common.utils.StringUtils;

//会员实体类
@Entity
@Table(name = "hq_member")
public class Member extends BaseEntity {
	private static final long serialVersionUID = 1533130686714725835L;
	public static final String PRINCIPAL_ATTRIBUTE_NAME = MemberInterceptor.class
			.getName() + ".PRINCIPAL";
	public static final String USERNAME_COOKIE_NAME = "username";
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";
	public static final Integer MAX_FAVORITE_COUNT = Integer.valueOf(10);

	private String username;// 用户名
	private String password;// 密码
	private String email;// E-mail
	private Long point;// 积分
	private BigDecimal amount;// 消费额
	private BigDecimal balance;// 余额
	private Boolean isEnabled;// 账号是否启用
	private Boolean isLocked;// 账号是否锁定
	private Integer loginFailureCount;// 连续登录失败的次数
	private Date lockedDate;// 账号锁定日期
	private String registerIp;// 注册IP
	private String loginIp;// 最后登录IP
	private Date loginDate;// 最后登录日期
	private String name;// 姓名
	private Gender gender;// 性别
	private Date birth;// 生日
	private String address;// 地址
	private String zipCode;// 区域代码
	private String phone;// 手机
	private String mobile;// 座机
	private String attributeValue0;// 属性0
	private String attributeValue1;// 属性1
	private String attributeValue2;
	private String attributeValue3;
	private String attributeValue4;
	private String attributeValue5;
	private String attributeValue6;
	private String attributeValue7;
	private String attributeValue8;
	private String attributeValue9;
	private SafeKey safeKey;// 密码找回Key
	private Area area;// 区域
	private MemberRank memberRank;// 会员等级
	private Cart cart;// 购物车
	private Set<Order> orders = new HashSet<Order>();// 订单
	private Set<Deposit> deposits = new HashSet<Deposit>();//
	private Set<Payment> payments = new HashSet<Payment>();// 付款方式
	private Set<CouponCode> couponCodes = new HashSet<CouponCode>();// 团购代码
	private Set<Receiver> receivers = new HashSet<Receiver>();//
	private Set<Review> reviews = new HashSet<Review>();//
	private Set<Consultation> consultations = new HashSet<Consultation>();// 咨询问题集
	private Set<Product> favoriteProducts = new HashSet<Product>();// 商品
	private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();// 商品通知
	private Set<Message> inMessages = new HashSet<Message>();// 发件信息
	private Set<Message> outMessages = new HashSet<Message>();// 收件信息

	public enum Gender {
		male, female;
	}

	@NotEmpty(groups = { BaseEntity.Save.class,BaseEntity.Update.class })
	@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]*$")
	@Column(nullable = false, updatable = false, unique = true)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotEmpty(groups = { BaseEntity.Save.class,BaseEntity.Update.class })
	@Pattern(regexp = "^[^\\s&\"<>]*$")
	@Column(nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotEmpty
	@Email
	@Length(max = 200)
	@Column(nullable = false)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotNull(groups = { BaseEntity.Save.class,BaseEntity.Update.class })
	@Min(0L)
	@Column(nullable = false)
	public Long getPoint() {
		return this.point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@NotNull(groups = { BaseEntity.Save.class,BaseEntity.Update.class })
	@Min(0L)
//	@Digits(integer = 15, fraction = 12)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBalance() {
		return this.balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Column(nullable = false)
	public Boolean getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Column(nullable = false)
	public Integer getLoginFailureCount() {
		return this.loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedDate() {
		return this.lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	@Column(nullable = false, updatable = false)
	public String getRegisterIp() {
		return this.registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@Length(max = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Member.Gender getGender() {
		return this.gender;
	}

	public void setGender(Member.Gender gender) {
		this.gender = gender;
	}

	public Date getBirth() {
		return this.birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	@Length(max = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(max = 200)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(max = 200)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(max = 200)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(max = 200)
	public String getAttributeValue0() {
		return this.attributeValue0;
	}

	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	@Length(max = 200)
	public String getAttributeValue1() {
		return this.attributeValue1;
	}

	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	@Length(max = 200)
	public String getAttributeValue2() {
		return this.attributeValue2;
	}

	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	@Length(max = 200)
	public String getAttributeValue3() {
		return this.attributeValue3;
	}

	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	@Length(max = 200)
	public String getAttributeValue4() {
		return this.attributeValue4;
	}

	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	@Length(max = 200)
	public String getAttributeValue5() {
		return this.attributeValue5;
	}

	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	@Length(max = 200)
	public String getAttributeValue6() {
		return this.attributeValue6;
	}

	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	@Length(max = 200)
	public String getAttributeValue7() {
		return this.attributeValue7;
	}

	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	@Length(max = 200)
	public String getAttributeValue8() {
		return this.attributeValue8;
	}

	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	@Length(max = 200)
	public String getAttributeValue9() {
		return this.attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	@Embedded
	public SafeKey getSafeKey() {
		return this.safeKey;
	}

	public void setSafeKey(SafeKey safeKey) {
		this.safeKey = safeKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public MemberRank getMemberRank() {
		return this.memberRank;
	}

	public void setMemberRank(MemberRank memberRank) {
		this.memberRank = memberRank;
	}

	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Cart getCart() {
		return this.cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Deposit> getDeposits() {
		return this.deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<CouponCode> getCouponCodes() {
		return this.couponCodes;
	}

	public void setCouponCodes(Set<CouponCode> couponCodes) {
		this.couponCodes = couponCodes;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("isDefault desc, createDate desc")
	public Set<Receiver> getReceivers() {
		return this.receivers;
	}

	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<Consultation> getConsultations() {
		return this.consultations;
	}

	public void setConsultations(Set<Consultation> consultations) {
		this.consultations = consultations;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hq_member_favorite_product")
	@OrderBy("createDate desc")
	public Set<Product> getFavoriteProducts() {
		return this.favoriteProducts;
	}

	public void setFavoriteProducts(Set<Product> favoriteProducts) {
		this.favoriteProducts = favoriteProducts;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<ProductNotify> getProductNotifies() {
		return this.productNotifies;
	}

	public void setProductNotifies(Set<ProductNotify> productNotifies) {
		this.productNotifies = productNotifies;
	}

	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Message> getInMessages() {
		return this.inMessages;
	}

	public void setInMessages(Set<Message> inMessages) {
		this.inMessages = inMessages;
	}

	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Message> getOutMessages() {
		return this.outMessages;
	}

	public void setOutMessages(Set<Message> outMessages) {
		this.outMessages = outMessages;
	}

	@Transient
	public Object getAttributeValue(MemberAttribute memberAttribute) {
		if (memberAttribute != null) {
			if (memberAttribute.getType() == MemberAttribute.Type.name)
				return getName();
			if (memberAttribute.getType() == MemberAttribute.Type.gender)
				return getGender();
			if (memberAttribute.getType() == MemberAttribute.Type.birth)
				return getBirth();
			if (memberAttribute.getType() == MemberAttribute.Type.area)
				return getArea();
			if (memberAttribute.getType() == MemberAttribute.Type.address)
				return getAddress();
			if (memberAttribute.getType() == MemberAttribute.Type.zipCode)
				return getZipCode();
			if (memberAttribute.getType() == MemberAttribute.Type.phone)
				return getPhone();
			if (memberAttribute.getType() == MemberAttribute.Type.mobile)
				return getMobile();
			if (memberAttribute.getType() == MemberAttribute.Type.checkbox) {
				if (memberAttribute.getPropertyIndex() != null)
					try {
						String attributeValue = "attributeValue" + memberAttribute.getPropertyIndex();
						String property = (String) PropertyUtils.getProperty(this, attributeValue);
						if (property == null)
							return null;
						return JsonUtils.toObject(property, List.class);
					} catch (IllegalAccessException illegalAccessException) {
						illegalAccessException.printStackTrace();
					} catch (InvocationTargetException invocationTargetException) {
						invocationTargetException.printStackTrace();
					} catch (NoSuchMethodException noSuchMethodException) {
						noSuchMethodException.printStackTrace();
					}
			} else if (memberAttribute.getPropertyIndex() != null)
				try {
					String attributeValue = "attributeValue" + memberAttribute.getPropertyIndex();
					return (String) PropertyUtils.getProperty(this, attributeValue);
				} catch (IllegalAccessException illegalAccessException) {
					illegalAccessException.printStackTrace();
				} catch (InvocationTargetException invocationTargetException) {
					invocationTargetException.printStackTrace();
				} catch (NoSuchMethodException noSuchMethodException) {
					noSuchMethodException.printStackTrace();
				}
		}
		return null;
	}

	@Transient
	public void setAttributeValue(MemberAttribute memberAttribute, Object attributeValue) {
		if (memberAttribute != null) {
			if (((attributeValue instanceof String))
					&& (StringUtils.isEmpty((String) attributeValue)))
				attributeValue = null;
			if ((memberAttribute.getType() == MemberAttribute.Type.name)
					&& (((attributeValue instanceof String)) || (attributeValue == null)))
				setName((String) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.gender)
					&& (((attributeValue instanceof Member.Gender)) || (attributeValue == null)))
				setGender((Member.Gender) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.birth)
					&& (((attributeValue instanceof Date)) || (attributeValue == null)))
				setBirth((Date) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.area)
					&& (((attributeValue instanceof Area)) || (attributeValue == null)))
				setArea((Area) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.address)
					&& (((attributeValue instanceof String)) || (attributeValue == null)))
				setAddress((String) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.zipCode)
					&& (((attributeValue instanceof String)) || (attributeValue == null)))
				setZipCode((String) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.phone)
					&& (((attributeValue instanceof String)) || (attributeValue == null)))
				setPhone((String) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.mobile)
					&& (((attributeValue instanceof String)) || (attributeValue == null)))
				setMobile((String) attributeValue);
			else if ((memberAttribute.getType() == MemberAttribute.Type.checkbox)
					&& (((attributeValue instanceof List)) || (attributeValue == null))) {
				if ((memberAttribute.getPropertyIndex() != null)
						&& ((attributeValue == null) || ((memberAttribute.getOptions() != null) 
								&& (memberAttribute.getOptions().containsAll((Collection<?>) attributeValue)))))
					try {
						String attrString = "attributeValue" + memberAttribute.getPropertyIndex();
						PropertyUtils.setProperty(this, attrString, JsonUtils.toJson(attributeValue));
					} catch (IllegalAccessException illegalAccessException) {
						illegalAccessException.printStackTrace();
					} catch (InvocationTargetException invocationTargetException) {
						invocationTargetException.printStackTrace();
					} catch (NoSuchMethodException noSuchMethodException) {
						noSuchMethodException.printStackTrace();
					}
			} else if (memberAttribute.getPropertyIndex() != null)
				try {
					String attrString = "attributeValue" + memberAttribute.getPropertyIndex();
					PropertyUtils.setProperty(this, attrString, attributeValue);
				} catch (IllegalAccessException illegalAccessException) {
					illegalAccessException.printStackTrace();
				} catch (InvocationTargetException invocationTargetException) {
					invocationTargetException.printStackTrace();
				} catch (NoSuchMethodException noSuchMethodException) {
					noSuchMethodException.printStackTrace();
				}
		}
	}

	@Transient
	public void removeAttributeValue() {
		setName(null);
		setGender(null);
		setBirth(null);
		setArea(null);
		setAddress(null);
		setZipCode(null);
		setPhone(null);
		setMobile(null);
		for (int i = 0; i < 10; i++) {
			String attribute = "attributeValue" + i;
			try {
				PropertyUtils.setProperty(this, attribute, null);
			} catch (IllegalAccessException illegalAccessException) {
				illegalAccessException.printStackTrace();
			} catch (InvocationTargetException invocationTargetException) {
				invocationTargetException.printStackTrace();
			} catch (NoSuchMethodException noSuchMethodException) {
				noSuchMethodException.printStackTrace();
			}
		}
	}
}