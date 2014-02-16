package com.hongqiang.shop.modules.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
//咨询
@Entity
@Table(name="hq_consultation")
public class Consultation extends BaseEntity
{
  private static final long serialVersionUID = -3950317769006303385L;
  private static final String filePath = "/consultation/content";

  private String content;//咨询内容
  private Boolean isShow;//是否显示
  private String ip;//ip地址
  private Member member;//会员
  private Product product;//商品
  private Consultation forConsultation;//咨询问题项
  private Set<Consultation> replyConsultations = new HashSet<Consultation>();//咨询回复集

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false, updatable=false)
  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  @Column(nullable=false)
  public Boolean getIsShow()
  {
    return this.isShow;
  }

  public void setIsShow(Boolean isShow)
  {
    this.isShow = isShow;
  }

  @Column(nullable=false, updatable=false)
  public String getIp()
  {
    return this.ip;
  }

  public void setIp(String ip)
  {
    this.ip = ip;
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

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(updatable=false)
  public Consultation getForConsultation()
  {
    return this.forConsultation;
  }

  public void setForConsultation(Consultation forConsultation)
  {
    this.forConsultation = forConsultation;
  }

  @OneToMany(mappedBy="forConsultation", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
  @OrderBy("createDate asc")
  public Set<Consultation> getReplyConsultations()
  {
    return this.replyConsultations;
  }

  public void setReplyConsultations(Set<Consultation> replyConsultations)
  {
    this.replyConsultations = replyConsultations;
  }

  @Transient
  public String getPath()
  {
    if ((getProduct() != null) && (getProduct().getId() != null))
      return filePath + getProduct().getId() + fileSuffix;
    return null;
  }
}