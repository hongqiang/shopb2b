package com.hongqiang.shop.modules.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
//商品到货通知
@Entity
@Table(name="hq_product_notify")
public class ProductNotify extends BaseEntity
{
  private static final long serialVersionUID = 3192904068727393421L;
  private String email;//电子邮件
  private Boolean hasSent;//是否出售
  private Member member;//会员
  private Product product;//商品

  @NotEmpty
  @Email
  @Length(max=200)
  @Column(nullable=false, updatable=false)
  public String getEmail()
  {
    return this.email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  @Column(nullable=false)
  public Boolean getHasSent()
  {
    return this.hasSent;
  }

  public void setHasSent(Boolean hasSent)
  {
    this.hasSent = hasSent;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(updatable=false)
  public Member getMember()
  {
    return this.member;
  }

  public void setMember(Member member)
  {
    this.member = member;
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
}