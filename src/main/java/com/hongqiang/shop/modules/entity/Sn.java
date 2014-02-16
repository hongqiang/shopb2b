package com.hongqiang.shop.modules.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hq_sn")
public class Sn extends BaseEntity
{
//sn编号类型：货物，订单，付款，退款，运货，退货，用于自动生成这些类型的单子的编号
public enum Type
{
  product, orders, payment, refunds, shipping, returns;
}

  private static final long serialVersionUID = -2330598144835706164L;
  private Type type;
  private Long lastValue;

  @Column(nullable=false, updatable=false, unique=true)
  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @Column(nullable=false)
  public Long getLastValue()
  {
    return this.lastValue;
  }

  public void setLastValue(Long lastValue)
  {
    this.lastValue = lastValue;
  }
}