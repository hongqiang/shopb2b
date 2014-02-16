package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
//退货
@Entity
@Table(name="hq_returns")
public class Returns extends BaseEntity
{
  private static final long serialVersionUID = -8019074120457087212L;
  private String sn;// 发货编号
  private String shippingMethod;// 配送方式名称
  private String deliveryCorp;// 物流公司名称
  private String trackingNo;//
  private BigDecimal freight;// 物流费用
  private String shipper;//送货人
  private String area;// 收货地区
  private String address;// 收货地址
  private String zipCode;// 收货邮编
  private String phone;// 收货电话
  private String operator;//操作员
  private String memo;// 备注
  private Order order;// 订单
  private List<ReturnsItem> returnsItems = new ArrayList<ReturnsItem>();// 物流项

  @Column(nullable=false, updatable=false, unique=true)
  public String getSn()
  {
    return this.sn;
  }

  public void setSn(String sn)
  {
    this.sn = sn;
  }

  @Column(updatable=false)
  public String getShippingMethod()
  {
    return this.shippingMethod;
  }

  public void setShippingMethod(String shippingMethod)
  {
    this.shippingMethod = shippingMethod;
  }

  @Column(updatable=false)
  public String getDeliveryCorp()
  {
    return this.deliveryCorp;
  }

  public void setDeliveryCorp(String deliveryCorp)
  {
    this.deliveryCorp = deliveryCorp;
  }

  @Length(max=200)
  @Column(updatable=false)
  public String getTrackingNo()
  {
    return this.trackingNo;
  }

  public void setTrackingNo(String trackingNo)
  {
    this.trackingNo = trackingNo;
  }

  @Min(0L)
//  @Digits(integer=12, fraction=3)
  @Column(updatable=false, precision=21, scale=6)
  public BigDecimal getFreight()
  {
    return this.freight;
  }

  public void setFreight(BigDecimal freight)
  {
    this.freight = freight;
  }

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false, updatable=false)
  public String getShipper()
  {
    return this.shipper;
  }

  public void setShipper(String shipper)
  {
    this.shipper = shipper;
  }

  @NotEmpty
  @Column(nullable=false, updatable=false)
  public String getArea()
  {
    return this.area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false, updatable=false)
  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false, updatable=false)
  public String getZipCode()
  {
    return this.zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false, updatable=false)
  public String getPhone()
  {
    return this.phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
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

  @Valid
  @NotEmpty
  @OneToMany(mappedBy="returns", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL})
  public List<ReturnsItem> getReturnsItems()
  {
    return this.returnsItems;
  }

  public void setReturnsItems(List<ReturnsItem> returnsItems)
  {
    this.returnsItems = returnsItems;
  }

  @Transient
  public int getQuantity()
  {
    int i = 0;
    if (getReturnsItems() != null)
    {
      Iterator<ReturnsItem> localIterator = getReturnsItems().iterator();
      while (localIterator.hasNext())
      {
        ReturnsItem localReturnsItem = (ReturnsItem)localIterator.next();
        if ((localReturnsItem == null) || (localReturnsItem.getQuantity() == null))
          continue;
        i += localReturnsItem.getQuantity().intValue();
      }
    }
    return i;
  }
}