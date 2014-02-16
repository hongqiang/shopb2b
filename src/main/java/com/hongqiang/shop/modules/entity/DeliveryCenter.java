package com.hongqiang.shop.modules.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_delivery_center")
public class DeliveryCenter extends BaseEntity
{
  private static final long serialVersionUID = 3328996121729039075L;
  private String name;
  private String contact;
  private String areaName;
  private String address;
  private String zipCode;
  private String phone;
  private String mobile;
  private String memo;
  private Boolean isDefault;
  private Area area;

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

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getContact()
  {
    return this.contact;
  }

  public void setContact(String contact)
  {
    this.contact = contact;
  }

  @Column(nullable=false)
  public String getAreaName()
  {
    return this.areaName;
  }

  public void setAreaName(String areaName)
  {
    this.areaName = areaName;
  }

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  @Length(max=200)
  public String getZipCode()
  {
    return this.zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  @Length(max=200)
  public String getPhone()
  {
    return this.phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  @Length(max=200)
  public String getMobile()
  {
    return this.mobile;
  }

  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }

  @Length(max=200)
  public String getMemo()
  {
    return this.memo;
  }

  public void setMemo(String memo)
  {
    this.memo = memo;
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
  @ManyToOne(fetch=FetchType.LAZY)
  public Area getArea()
  {
    return this.area;
  }

  public void setArea(Area area)
  {
    this.area = area;
  }

  @PrePersist
  public void prePersist()
  {
	  super.prePersist();
    if (getArea() != null)
      setAreaName(getArea().getFullName());
  }

  @PreUpdate
  public void preUpdate()
  {
	  super.preUpdate();
    if (getArea() != null)
      setAreaName(getArea().getFullName());
  }
}