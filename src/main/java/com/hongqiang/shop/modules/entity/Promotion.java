package com.hongqiang.shop.modules.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_promotion")
public class Promotion extends OrderEntity
{
  private static final long serialVersionUID = 3536993535267962279L;
  private static final String filePath = "/promotion/content";
  private String name;//促销名称
  private String title;//促销标题
  private Date beginDate;//开始日期
  private Date endDate;//结束日期
  private BigDecimal startPrice;//开始价格
  private BigDecimal endPrice;//结束价格
  private Operator priceOperator;//价格操作类型
  private BigDecimal priceValue;//
  private Operator pointOperator;//点数操作类型
  private BigDecimal pointValue;//
  private Boolean isFreeShipping;//免运费
  private Boolean isCouponAllowed;//只允许团购
  private String introduction;//介绍
  private Set<MemberRank> memberRanks = new HashSet<MemberRank>();//会员
  private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();//商品类别
  private Set<Product> products = new HashSet<Product>();//商品
  private Set<Brand> brands = new HashSet<Brand>();//商标
  private Set<Coupon> coupons = new HashSet<Coupon>();//团购 
  private List<GiftItem> giftItems = new ArrayList<GiftItem>();//赠品

  public enum Operator
{
  add, subtract, multiply, divide;
}
  
  @JsonProperty
  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @JsonProperty
  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  @JsonProperty
  public Date getBeginDate()
  {
    return this.beginDate;
  }

  public void setBeginDate(Date beginDate)
  {
    this.beginDate = beginDate;
  }

  @JsonProperty
  public Date getEndDate()
  {
    return this.endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  @JsonProperty
  @Min(0L)
//  @Digits(integer=12, fraction=3)
  @Column(precision=21, scale=6)
  public BigDecimal getStartPrice()
  {
    return this.startPrice;
  }

  public void setStartPrice(BigDecimal startPrice)
  {
    this.startPrice = startPrice;
  }

  @JsonProperty
  @Min(0L)
//  @Digits(integer=12, fraction=3)
  @Column(precision=21, scale=6)
  public BigDecimal getEndPrice()
  {
    return this.endPrice;
  }

  public void setEndPrice(BigDecimal endPrice)
  {
    this.endPrice = endPrice;
  }

  @NotNull
  @Column(nullable=false)
  public Promotion.Operator getPriceOperator()
  {
    return this.priceOperator;
  }

  public void setPriceOperator(Promotion.Operator priceOperator)
  {
    this.priceOperator = priceOperator;
  }

//  @Digits(integer=12, fraction=3)
  @Column(precision=21, scale=6)
  public BigDecimal getPriceValue()
  {
    return this.priceValue;
  }

  public void setPriceValue(BigDecimal priceValue)
  {
    this.priceValue = priceValue;
  }

  @NotNull
  @Column(nullable=false)
  public Promotion.Operator getPointOperator()
  {
    return this.pointOperator;
  }

  public void setPointOperator(Promotion.Operator pointOperator)
  {
    this.pointOperator = pointOperator;
  }

  public BigDecimal getPointValue()
  {
    return this.pointValue;
  }

  public void setPointValue(BigDecimal pointValue)
  {
    this.pointValue = pointValue;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsFreeShipping()
  {
    return this.isFreeShipping;
  }

  public void setIsFreeShipping(Boolean isFreeShipping)
  {
    this.isFreeShipping = isFreeShipping;
  }

  @JsonProperty
  @NotNull
  @Column(nullable=false)
  public Boolean getIsCouponAllowed()
  {
    return this.isCouponAllowed;
  }

  public void setIsCouponAllowed(Boolean isCouponAllowed)
  {
    this.isCouponAllowed = isCouponAllowed;
  }

  @Lob
  public String getIntroduction()
  {
    return this.introduction;
  }

  public void setIntroduction(String introduction)
  {
    this.introduction = introduction;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="hq_promotion_member_rank")
  public Set<MemberRank> getMemberRanks()
  {
    return this.memberRanks;
  }

  public void setMemberRanks(Set<MemberRank> memberRanks)
  {
    this.memberRanks = memberRanks;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="hq_promotion_product_category")
  public Set<ProductCategory> getProductCategories()
  {
    return this.productCategories;
  }

  public void setProductCategories(Set<ProductCategory> productCategories)
  {
    this.productCategories = productCategories;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="hq_promotion_product")
  public Set<Product> getProducts()
  {
    return this.products;
  }

  public void setProducts(Set<Product> products)
  {
    this.products = products;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="hq_promotion_brand")
  public Set<Brand> getBrands()
  {
    return this.brands;
  }

  public void setBrands(Set<Brand> brands)
  {
    this.brands = brands;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="hq_promotion_coupon")
  public Set<Coupon> getCoupons()
  {
    return this.coupons;
  }

  public void setCoupons(Set<Coupon> coupons)
  {
    this.coupons = coupons;
  }

  @Valid
  @OneToMany(mappedBy="promotion", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL}, orphanRemoval=true)
  public List<GiftItem> getGiftItems()
  {
    return this.giftItems;
  }

  public void setGiftItems(List<GiftItem> giftItems)
  {
    this.giftItems = giftItems;
  }

  @Transient
  public boolean hasBegun()
  {
    return (getBeginDate() == null) || (new Date().after(getBeginDate()));
  }

  @Transient
  public boolean hasEnded()
  {
    return (getEndDate() != null) && (new Date().after(getEndDate()));
  }

  @Transient
  public String getPath()
  {
    if (getId() != null)
      return filePath + getId() + fileSuffix;
    return null;
  }

  @Transient
  public BigDecimal calculatePrice(BigDecimal price)
  {
    if ((price != null) && (getPriceOperator() != null) && (getPriceValue() != null))
    {
      BigDecimal localBigDecimal;
      if (getPriceOperator() == Promotion.Operator.add)
        localBigDecimal = price.add(getPriceValue());
      else if (getPriceOperator() == Promotion.Operator.subtract)
        localBigDecimal = price.subtract(getPriceValue());
      else if (getPriceOperator() == Promotion.Operator.multiply)
        localBigDecimal = price.multiply(getPriceValue());
      else
        localBigDecimal = price.divide(getPriceValue());
      return localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal : new BigDecimal(0);
    }
    return price;
  }

  @Transient
  public Integer calculatePoint(Integer point)
  {
    if ((point != null) && (getPointOperator() != null) && (getPointValue() != null))
    {
      BigDecimal localBigDecimal;
      if (getPointOperator() == Promotion.Operator.add)
        localBigDecimal = new BigDecimal(point.intValue()).add(getPointValue());
      else if (getPointOperator() == Promotion.Operator.subtract)
        localBigDecimal = new BigDecimal(point.intValue()).subtract(getPointValue());
      else if (getPointOperator() == Promotion.Operator.multiply)
        localBigDecimal = new BigDecimal(point.intValue()).multiply(getPointValue());
      else
        localBigDecimal = new BigDecimal(point.intValue()).divide(getPointValue());
      return Integer.valueOf(localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal.intValue() : 0);
    }
    return point;
  }
}