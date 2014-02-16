package com.hongqiang.shop.modules.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_specification_value")
public class SpecificationValue extends OrderEntity
{
  private static final long serialVersionUID = -8624741017444123488L;
  private String name;
  private String image;
  private Specification specification;
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

  @Length(max=200)
  public String getImage()
  {
    return this.image;
  }

  public void setImage(String image)
  {
    this.image = image;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false)
  public Specification getSpecification()
  {
    return this.specification;
  }

  public void setSpecification(Specification specification)
  {
    this.specification = specification;
  }

  @ManyToMany(mappedBy="specificationValues", fetch=FetchType.LAZY)
  public Set<Product> getProducts()
  {
    return this.products;
  }

  public void setProducts(Set<Product> products)
  {
    this.products = products;
  }
}