package com.hongqiang.shop.modules.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_delivery_corp")
public class DeliveryCorp extends OrderEntity
{
  private static final long serialVersionUID = 10595703086045998L;
  private String name;
  private String url;
  private String code;
  private Set<ShippingMethod> shippingMethods = new HashSet<ShippingMethod>();

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

  @Length(max=200)
  public String getUrl()
  {
    return this.url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  @Length(max=200)
  public String getCode()
  {
    return this.code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  @OneToMany(mappedBy="defaultDeliveryCorp", fetch=FetchType.LAZY)
  public Set<ShippingMethod> getShippingMethods()
  {
    return this.shippingMethods;
  }

  public void setShippingMethods(Set<ShippingMethod> shippingMethods)
  {
    this.shippingMethods = shippingMethods;
  }

  @PreRemove
  public void preRemove()
  {
    Set<ShippingMethod> localSet = getShippingMethods();
    if (localSet != null)
    {
      Iterator<ShippingMethod> localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        ShippingMethod localShippingMethod = (ShippingMethod)localIterator.next();
        localShippingMethod.setDefaultDeliveryCorp(null);
      }
    }
  }
}