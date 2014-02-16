package com.hongqiang.shop.modules.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_role")
public class Role extends BaseEntity
{
  private static final long serialVersionUID = -6614052029623997372L;
  private String name;
  private Boolean isSystem;
  private String description;
  private List<String> authorities = new ArrayList<String>();
  private Set<Admin> admins = new HashSet<Admin>();

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

  @Column(nullable=false, updatable=false)
  public Boolean getIsSystem()
  {
    return this.isSystem;
  }

  public void setIsSystem(Boolean isSystem)
  {
    this.isSystem = isSystem;
  }

  @Length(max=200)
  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  @ElementCollection
  @CollectionTable(name="hq_role_authority")
  public List<String> getAuthorities()
  {
    return this.authorities;
  }

  public void setAuthorities(List<String> authorities)
  {
    this.authorities = authorities;
  }

  @ManyToMany(mappedBy="roles", fetch=FetchType.LAZY)
  public Set<Admin> getAdmins()
  {
    return this.admins;
  }

  public void setAdmins(Set<Admin> admins)
  {
    this.admins = admins;
  }
}