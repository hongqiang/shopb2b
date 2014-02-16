package com.hongqiang.shop.modules.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
//订单日志
@Entity
@Table(name="hq_order_log")
public class OrderLog extends BaseEntity
{
  private static final long serialVersionUID = -2704154761295319939L;
  private Type type;// 订单日志类型
  private String operator;// 操作员
  private String content;//内容
  private Order order;// 订单

  // 订单日志类型（订单创建、订单修改、订单确认、订单支付、订单退款、订单发货、订单退货、订单完成、订单取消、其他）
  public enum Type
{
  create, modify, confirm, payment, refunds, shipping, returns, complete, cancel, other;
}
  
  public OrderLog()
  {
  }

  public OrderLog(Type type, String operator)
  {
    this.type = type;
    this.operator = operator;
  }

  public OrderLog(Type type, String operator, String content)
  {
    this.type = type;
    this.operator = operator;
    this.content = content;
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

  @Column(updatable=false)
  public String getOperator()
  {
    return this.operator;
  }

  public void setOperator(String operator)
  {
    this.operator = operator;
  }

  @Column(updatable=false)
  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

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