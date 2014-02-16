package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

//会员等级
@Entity
@Table(name="hq_member_rank")
public class MemberRank extends BaseEntity
{
  private static final long serialVersionUID = 3599029355500655209L;
  private String name;//等級名称
  private Double scale;//优惠百分比
  private BigDecimal amount;//余额限制
  private Boolean isDefault;// 是否为默认等级
  private Boolean isSpecial;//是否为特别会员
  private Set<Member> members = new HashSet<Member>();//会员
  private Set<Promotion> promotions = new HashSet<Promotion>();//会员促销

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false, unique=true)
  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @NotNull
  @Min(0L)
//  @Digits(integer=3, fraction=3)
  @Column(nullable=false, precision=12, scale=6)
  public Double getScale()
  {
    return this.scale;
  }

  public void setScale(Double scale)
  {
    this.scale = scale;
  }

  @Min(0L)
//  @Digits(integer=12, fraction=3)
  @Column(unique=true, precision=21, scale=6)
  public BigDecimal getAmount()
  {
    return this.amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsDefault()
  {
    return this.isDefault;
  }

  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsSpecial()
  {
    return this.isSpecial;
  }

  public void setIsSpecial(Boolean isSpecial)
  {
    this.isSpecial = isSpecial;
  }

  @OneToMany(mappedBy="memberRank", fetch=FetchType.LAZY)
  public Set<Member> getMembers()
  {
    return this.members;
  }

  public void setMembers(Set<Member> members)
  {
    this.members = members;
  }

  @ManyToMany(mappedBy="memberRanks", fetch=FetchType.LAZY)
  public Set<Promotion> getPromotions()
  {
    return this.promotions;
  }

  public void setPromotions(Set<Promotion> promotions)
  {
    this.promotions = promotions;
  }

  @PreRemove
  public void preRemove()
  {
    Set<Promotion> localSet = getPromotions();
    if (localSet != null)
    {
      Iterator<Promotion> localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        Promotion localPromotion = (Promotion)localIterator.next();
        localPromotion.getMemberRanks().remove(this);
      }
    }
  }
}