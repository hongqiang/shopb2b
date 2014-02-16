package com.hongqiang.shop.website.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hongqiang.shop.modules.entity.OrderEntity;

@Entity
@Table(name="hq_plugin_config")
public class PluginConfig extends OrderEntity
{
  private static final long serialVersionUID = -4357367409438384806L;
  private String pluginId;
  private Boolean isEnabled;
  private Map<String, String> attributes = new HashMap<String, String>();

  @Column(nullable=false, updatable=false, unique=true)
  public String getPluginId()
  {
    return this.pluginId;
  }

  public void setPluginId(String pluginId)
  {
    this.pluginId = pluginId;
  }

  @Column(nullable=false)
  public Boolean getIsEnabled()
  {
    return this.isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled)
  {
    this.isEnabled = isEnabled;
  }

  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="hq_plugin_config_attribute")
  public Map<String, String> getAttributes()
  {
    return this.attributes;
  }

  public void setAttributes(Map<String, String> attributes)
  {
    this.attributes = attributes;
  }

  @Transient
  public String getAttribute(String name)
  {
    if ((getAttributes() != null) && (name != null))
      return (String)getAttributes().get(name);
    return null;
  }

  @Transient
  public void setAttribute(String name, String value)
  {
    if ((getAttributes() != null) && (name != null))
      getAttributes().put(name, value);
  }
}