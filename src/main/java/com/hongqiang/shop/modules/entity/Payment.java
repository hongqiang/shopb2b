package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

//支付实体类
@Entity
@Table(name = "hq_payment")
public class Payment extends BaseEntity {
	private static final long serialVersionUID = -5052430116564638634L;
	public static final String TYPE_SEPARATOR = "-";
	private String sn;// 支付编号
	private Type type;// 支付类型
	private Status status;// 支付状态
	private String paymentMethod;// 支付方式
	private String bank;// 收款银行名称
	private String account;// 收款银行账号
	private BigDecimal fee;// 支付手续费
	private BigDecimal amount;// 支付金额
	private String payer;// 付款人
	private String operator;// 操作员
	private Date paymentDate;// 支付日期
	private String memo;// 备注
	private String paymentPluginId;// 支付配置id
	private Date expire;// 过期日期
	private Deposit deposit;// 预存款
	private Member member;// 会员
	private Order order;// 订单

	// 支付状态（等待支付、成功、失败）
	public enum Status {
		wait, success, failure;
	}

	// 支付类型（在线支付、线下支付、预存款支付）
	public enum Type {
		online, offline, deposit;
	}

	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@NotNull
	@Column(nullable = false, updatable = false)
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(updatable = false)
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Length(max = 200)
	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Length(max = 200)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
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
//	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Length(max = 200)
	public String getPayer() {
		return this.payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	@Column(updatable = false)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Length(max = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@JoinColumn(updatable = false)
	public String getPaymentPluginId() {
		return this.paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	@JoinColumn(updatable = false)
	public Date getExpire() {
		return this.expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	@OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
	public Deposit getDeposit() {
		return this.deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", updatable = false)
	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Transient
	public boolean hasExpired() {
		return (getExpire() != null) && (new Date().after(getExpire()));
	}

	@PreRemove
	public void preRemove() {
		if (getDeposit() != null)
			getDeposit().setPayment(null);
	}
}