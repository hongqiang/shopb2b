package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
//退款
@Entity
@Table(name="hq_refunds")
public class Refunds extends BaseEntity
{
  private static final long serialVersionUID = 354885216604823632L;
  private String sn;// 退款编号
  private Type type;// 退款类型
  private String paymentMethod;// 支付方式
  private String bank;// 退款银行名称
  private String account;// 退款银行账号
  private BigDecimal amount;// 退款金额
  private String payee;// 收款人
  private String operator;// 操作员
  private String memo;// 备注
  private Order order;// 订单

  // 退款类型（预存款支付、在线支付、线下支付）
  public enum Type
{
  online, offline, deposit;
}
  
  @Column(nullable=false, updatable=false, unique=true)
  public String getSn()
  {
    return this.sn;
  }

  public void setSn(String sn)
  {
    this.sn = sn;
  }

  @NotNull
  @Column(nullable=false, updatable=false)
  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @Column(updatable=false)
  public String getPaymentMethod()
  {
    return this.paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod)
  {
    this.paymentMethod = paymentMethod;
  }

  @Length(max=200)
  @Column(updatable=false)
  public String getBank()
  {
    return this.bank;
  }

  public void setBank(String bank)
  {
    this.bank = bank;
  }

  @Length(max=200)
  @Column(updatable=false)
  public String getAccount()
  {
    return this.account;
  }

  public void setAccount(String account)
  {
    this.account = account;
  }

  @NotNull
  @Min(0L)
//  @Digits(integer=12, fraction=3)
  @Column(nullable=false, updatable=false, precision=21, scale=6)
  public BigDecimal getAmount()
  {
    return this.amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  @Length(max=200)
  @Column(updatable=false)
  public String getPayee()
  {
    return this.payee;
  }

  public void setPayee(String payee)
  {
    this.payee = payee;
  }

  @Column(nullable=false, updatable=false)
  public String getOperator()
  {
    return this.operator;
  }

  public void setOperator(String operator)
  {
    this.operator = operator;
  }

  @Length(max=200)
  @Column(updatable=false)
  public String getMemo()
  {
    return this.memo;
  }

  public void setMemo(String memo)
  {
    this.memo = memo;
  }

  @NotNull
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="orders", nullable=false, updatable=false)
  public Order getOrder()
  {
    return this.order;
  }

  public void setOrder(Order order)
  {
    this.order = order;
  }
}