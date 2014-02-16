package com.hongqiang.shop.modules.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_specification")
public class Specification extends OrderEntity
{

public enum Type
{
  text, image;
}

  private static final long serialVersionUID = -6346775052811140926L;
  private String name;
  private Type type;
  private String memo;
  private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
  private Set<Product> products = new HashSet<Product>();

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
  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
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

  @Valid
  @NotEmpty
  @OneToMany(mappedBy="specification", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL}, orphanRemoval=true)
  @OrderBy("order asc")
  public List<SpecificationValue> getSpecificationValues()
  {
    return this.specificationValues;
  }

  public void setSpecificationValues(List<SpecificationValue> specificationValues)
  {
    this.specificationValues = specificationValues;
  }

  @ManyToMany(mappedBy="specifications", fetch=FetchType.LAZY)
  public Set<Product> getProducts()
  {
    return this.products;
  }

  public void setProducts(Set<Product> products)
  {
    this.products = products;
  }
}