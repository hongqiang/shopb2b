package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;

//购物车项
@Entity
@Table(name="hq_cart_item")
public class CartItem extends BaseEntity
{
  private static final long serialVersionUID = 2979296789363163144L;
  public static final Integer MAX_QUANTITY = Integer.valueOf(10000);
  private Integer quantity;// 数量
  private Product product;// 商品
  private Cart cart;//购物车

  @Column(nullable=false)
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
  public Product getProduct()
  {
    return this.product;
  }

  public void setProduct(Product product)
  {
    this.product = product;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false)
  public Cart getCart()
  {
    return this.cart;
  }

  public void setCart(Cart cart)
  {
    this.cart = cart;
  }

  @Transient
  public long getPoint()
  {
    if ((getProduct() != null) && (getProduct().getPoint() != null) && (getQuantity() != null))
      return getProduct().getPoint().longValue() * getQuantity().intValue();
    return 0L;
  }

  @Transient
  public int getWeight()
  {
    if ((getProduct() != null) && (getProduct().getWeight() != null) && (getQuantity() != null))
      return getProduct().getWeight().intValue() * getQuantity().intValue();
    return 0;
  }

  @Transient
  public BigDecimal getUnitPrice()
  {
    if ((getProduct() != null) && (getProduct().getPrice() != null))
    {
      Setting localSetting = SettingUtils.get();
      if ((getCart() != null) && (getCart().getMember() != null) && (getCart().getMember().getMemberRank() != null))
      {
        MemberRank localMemberRank = getCart().getMember().getMemberRank();
        Map<MemberRank, BigDecimal> localMap = getProduct().getMemberPrice();
        if ((localMap != null) && (!localMap.isEmpty()) && (localMap.containsKey(localMemberRank)))
          return localSetting.setScale((BigDecimal)localMap.get(localMemberRank));
        if (localMemberRank.getScale() != null)
          return localSetting.setScale(getProduct().getPrice().multiply(new BigDecimal(localMemberRank.getScale().doubleValue())));
      }
      return localSetting.setScale(getProduct().getPrice());
    }
    return new BigDecimal(0);
  }

  @Transient
  public BigDecimal getSubtotal()
  {
    if (getQuantity() != null)
      return getUnitPrice().multiply(new BigDecimal(getQuantity().intValue()));
    return new BigDecimal(0);
  }

  @Transient
  public boolean getIsLowStock()
  {
    return (getQuantity() != null) && (getProduct() != null) && (getProduct().getStock() != null) && (getQuantity().intValue() > getProduct().getAvailableStock().intValue());
  }

  @Transient
  public void add(int quantity)
  {
    if (quantity > 0)
      if (getQuantity() != null)
        setQuantity(Integer.valueOf(getQuantity().intValue() + quantity));
      else
        setQuantity(Integer.valueOf(quantity));
  }
}