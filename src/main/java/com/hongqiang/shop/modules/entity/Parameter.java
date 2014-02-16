package com.hongqiang.shop.modules.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_parameter")
public class Parameter extends OrderEntity
{
  private static final long serialVersionUID = -5833568086582136314L;
  private String name;
  private ParameterGroup parameterGroup;

  @JsonProperty
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

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false)
  public ParameterGroup getParameterGroup()
  {
    return this.parameterGroup;
  }

  public void setParameterGroup(ParameterGroup parameterGroup)
  {
    this.parameterGroup = parameterGroup;
  }
}