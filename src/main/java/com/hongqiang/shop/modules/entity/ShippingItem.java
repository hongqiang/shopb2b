package com.hongqiang.shop.modules.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_shipping_item")
public class ShippingItem extends BaseEntity
{
  private static final long serialVersionUID = 2756395514949325790L;
  private String sn;
  private String name;
  private Integer quantity;
  private Shipping shipping;

  @NotEmpty
  @Column(nullable=false, updatable=false)
  public String getSn()
  {
    return this.sn;
  }

  public void setSn(String sn)
  {
    this.sn = sn;
  }

  @NotEmpty
  @Column(nullable=false, updatable=false)
  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @NotNull
  @Min(1L)
  @Column(nullable=false, updatable=false)
  public Integer getQuantity()
  {
    return this.quantity;
  }

  public void setQuantity(Integer quantity)
  {
    this.quantity = quantity;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false, updatable=false)
  public Shipping getShipping()
  {
    return this.shipping;
  }

  public void setShipping(Shipping shipping)
  {
    this.shipping = shipping;
  }
}