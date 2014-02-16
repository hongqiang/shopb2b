package com.hongqiang.shop.website.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.modules.entity.OrderEntity;

@Entity
@Table(name="hq_ad")
public class Ad extends OrderEntity
{

public enum Type
{
  text, image, flash;
}

  private static final long serialVersionUID = -1307743303786909390L;
  private String title;
  private Type type;
  private String content;
  private String path;
  private Date beginDate;
  private Date endDate;
  private String url;
  private AdPosition adPosition;

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

  @NotNull
  @Column(nullable=false)
  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @Lob
  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  @Length(max=200)
  public String getPath()
  {
    return this.path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public Date getBeginDate()
  {
    return this.beginDate;
  }

  public void setBeginDate(Date beginDate)
  {
    this.beginDate = beginDate;
  }

  public Date getEndDate()
  {
    return this.endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  @Length(max=200)
  public String getUrl()
  {
    return this.url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  @NotNull
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false)
  public AdPosition getAdPosition()
  {
    return this.adPosition;
  }

  public void setAdPosition(AdPosition adPosition)
  {
    this.adPosition = adPosition;
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
}