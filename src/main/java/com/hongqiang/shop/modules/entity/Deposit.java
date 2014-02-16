package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;
//预存款实体类
@Entity
@Table(name="hq_deposit")
public class Deposit extends BaseEntity
{
  private static final long serialVersionUID = -8323452873046981882L;
  private Type type;//预存款操作类型
  private BigDecimal credit;//存入金额
  private BigDecimal debit;//支出金额
  private BigDecimal balance;//当前余额
  private String operator;//操作
  private String memo;//备忘录
  private Member member;//会员
  private Order order;//订单
  private Payment payment;//收款

  // 预存款操作类型（会员充值、会员支付、后台代支付、后台代扣费、后台代充值、后台退款）
  public enum Type
{
  memberRecharge, memberPayment, adminRecharge, adminChargeback, adminPayment, adminRefunds;
}
  
  @Column(nullable=false, updatable=false)
  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @Column(nullable=false, updatable=false, precision=21, scale=6)
  public BigDecimal getCredit()
  {
    return this.credit;
  }

  public void setCredit(BigDecimal credit)
  {
    this.credit = credit;
  }

  @Column(nullable=false, updatable=false, precision=21, scale=6)
  public BigDecimal getDebit()
  {
    return this.debit;
  }

  public void setDebit(BigDecimal debit)
  {
    this.debit = debit;
  }

  @Column(nullable=false, updatable=false, precision=21, scale=6)
  public BigDecimal getBalance()
  {
    return this.balance;
  }

  public void setBalance(BigDecimal balance)
  {
    this.balance = balance;
  }

  @Column(updatable=false)
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

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false, updatable=false)
  public Member getMember()
  {
    return this.member;
  }

  public void setMember(Member member)
  {
    this.member = member;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="orders")
  public Order getOrder()
  {
    return this.order;
  }

  public void setOrder(Order order)
  {
    this.order = order;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  public Payment getPayment()
  {
    return this.payment;
  }

  public void setPayment(Payment payment)
  {
    this.payment = payment;
  }
}