package com.hongqiang.shop.website.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.modules.entity.OrderEntity;

@Entity
@Table(name="hq_navigation")
public class Navigation extends OrderEntity
{
public enum Position
{
  top, middle, bottom;
}

  private static final long serialVersionUID = -7635757647887646795L;
  private String name;
  private Position position;
  private String url;
  private Boolean isBlankTarget;

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

  @NotNull
  @Column(nullable=false)
  public Position getPosition()
  {
    return this.position;
  }

  public void setPosition(Position position)
  {
    this.position = position;
  }

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getUrl()
  {
    return this.url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsBlankTarget()
  {
    return this.isBlankTarget;
  }

  public void setIsBlankTarget(Boolean isBlankTarget)
  {
    this.isBlankTarget = isBlankTarget;
  }
}