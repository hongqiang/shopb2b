package com.hongqiang.shop.common.utils.plugin;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.plugin.service.PluginConfigService;
import com.hongqiang.shop.website.entity.FileInfo;
import com.hongqiang.shop.website.entity.PluginConfig;

public abstract class StoragePlugin
  implements Comparable<StoragePlugin>
{

  @Autowired
  private PluginConfigService pluginConfigService;

  public final String getId()
  {
    return ((Component)getClass().getAnnotation(Component.class)).value();
  }

  public abstract String getName();

  public abstract String getVersion();

  public abstract String getAuthor();

  public abstract String getSiteUrl();

  public abstract String getInstallUrl();

  public abstract String getUninstallUrl();

  public abstract String getSettingUrl();

  public boolean getIsInstalled()
  {
    return this.pluginConfigService.pluginIdExists(getId());
  }

  public PluginConfig getPluginConfig()
  {
    return this.pluginConfigService.findByPluginId(getId());
  }

  public boolean getIsEnabled()
  {
    PluginConfig localPluginConfig = getPluginConfig();
    return localPluginConfig != null ? localPluginConfig.getIsEnabled().booleanValue() : false;
  }

  public String getAttribute(String name)
  {
    PluginConfig localPluginConfig = getPluginConfig();
    return localPluginConfig != null ? localPluginConfig.getAttribute(name) : null;
  }

  public Integer getOrder()
  {
    PluginConfig localPluginConfig = getPluginConfig();
    return localPluginConfig != null ? localPluginConfig.getOrder() : null;
  }

  public abstract void upload(String paramString1, File paramFile, String paramString2);

  public abstract String getUrl(String paramString);

  public abstract List<FileInfo> browser(String paramString);

  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    if (this == obj)
      return true;
    StoragePlugin localStoragePlugin = (StoragePlugin)obj;
    return new EqualsBuilder().append(getId(), localStoragePlugin.getId()).isEquals();
  }

  public int hashCode()
  {
    return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
  }

  public int compareTo(StoragePlugin storagePlugin)
  {
    return new CompareToBuilder().append(getOrder(), storagePlugin.getOrder()).append(getId(), storagePlugin.getId()).toComparison();
  }
}