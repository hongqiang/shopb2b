package com.hongqiang.shop.modules.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="hq_member_attribute")
public class MemberAttribute extends OrderEntity
{
	public enum Type
	{
	  name, gender, birth, area, address, zipCode, phone, mobile, text, select, checkbox;
	}
	
  private static final long serialVersionUID = 4513705276569738136L;
  private String name;
  private Type type;
  private Boolean isEnabled;
  private Boolean isRequired;
  private Integer propertyIndex;
  private List<String> options = new ArrayList<String>();

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

  @NotNull(groups={BaseEntity.Save.class})
  @Column(nullable=false, updatable=false)
  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
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

  @NotNull
  @Column(nullable=false)
  public Boolean getIsRequired()
  {
    return this.isRequired;
  }

  public void setIsRequired(Boolean isRequired)
  {
    this.isRequired = isRequired;
  }

  @Column(updatable=false)
  public Integer getPropertyIndex()
  {
    return this.propertyIndex;
  }

  public void setPropertyIndex(Integer propertyIndex)
  {
    this.propertyIndex = propertyIndex;
  }

  @ElementCollection
  @CollectionTable(name="hq_member_attribute_option")
  public List<String> getOptions()
  {
    return this.options;
  }

  public void setOptions(List<String> options)
  {
    this.options = options;
  }
}