package com.hongqiang.shop.modules.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_admin")
public class Admin extends BaseEntity
{
  private static final long serialVersionUID = -7519486823153844426L;
  private String username;
  private String password;
  private String email;
  private String name;
  private String department;
  private Boolean isEnabled;
  private Boolean isLocked;
  private Integer loginFailureCount;
  private Date lockedDate;
  private Date loginDate;
  private String loginIp;
  private Set<Role> roles = new HashSet<Role>();
  private Set<Order> orders = new HashSet<Order>();

  @NotEmpty(groups={BaseEntity.Save.class})
  @Pattern(regexp="^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
  @Length(min=2, max=20)
  @Column(nullable=false, updatable=false, unique=true)
  public String getUsername()
  {
    return this.username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  @NotEmpty(groups={BaseEntity.Save.class})
  @Pattern(regexp="^[^\\s&\"<>]+$")
  @Length(min=4, max=40)
  @Column(nullable=false)
  public String getPassword()
  {
    return this.password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  @NotEmpty
  @Email
  @Length(max=200)
  @Column(nullable=false)
  public String getEmail()
  {
    return this.email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  @Length(max=200)
  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Length(max=200)
  public String getDepartment()
  {
    return this.department;
  }

  public void setDepartment(String department)
  {
    this.department = department;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsEnabled()
  {
    return this.isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled)
  {
    this.isEnabled = isEnabled;
  }

  @Column(nullable=false)
  public Boolean getIsLocked()
  {
    return this.isLocked;
  }

  public void setIsLocked(Boolean isLocked)
  {
    this.isLocked = isLocked;
  }

  @Column(nullable=false)
  public Integer getLoginFailureCount()
  {
    return this.loginFailureCount;
  }

  public void setLoginFailureCount(Integer loginFailureCount)
  {
    this.loginFailureCount = loginFailureCount;
  }

  public Date getLockedDate()
  {
    return this.lockedDate;
  }

  public void setLockedDate(Date lockedDate)
  {
    this.lockedDate = lockedDate;
  }

  public Date getLoginDate()
  {
    return this.loginDate;
  }

  public void setLoginDate(Date loginDate)
  {
    this.loginDate = loginDate;
  }

  public String getLoginIp()
  {
    return this.loginIp;
  }

  public void setLoginIp(String loginIp)
  {
    this.loginIp = loginIp;
  }

  @NotEmpty
  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="hq_admin_role")
  public Set<Role> getRoles()
  {
    return this.roles;
  }

  public void setRoles(Set<Role> roles)
  {
    this.roles = roles;
  }

  @OneToMany(mappedBy="operator", fetch=FetchType.LAZY)
  public Set<Order> getOrders()
  {
    return this.orders;
  }

  public void setOrders(Set<Order> orders)
  {
    this.orders = orders;
  }

  @PreRemove
  public void preRemove()
  {
    Set<Order> localSet = getOrders();
    if (localSet != null)
    {
      Iterator<Order> localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        Order localOrder = (Order)localIterator.next();
        localOrder.setLockExpire(null);
        localOrder.setOperator(null);
      }
    }
  }
}