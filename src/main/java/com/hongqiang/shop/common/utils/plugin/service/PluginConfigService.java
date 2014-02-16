package com.hongqiang.shop.common.utils.plugin.service;

import com.hongqiang.shop.website.entity.PluginConfig;

public interface PluginConfigService {
	public boolean pluginIdExists(String pluginId);

	public PluginConfig findByPluginId(String pluginId);
	
	public void save(PluginConfig pluginConfig);

	  public PluginConfig update(PluginConfig pluginConfig);

	  public PluginConfig update(PluginConfig pluginConfig, String[] ignoreProperties);

	  public void delete(Long id);

	  public void delete(Long[] ids);

	  public void delete(PluginConfig pluginConfig);
}