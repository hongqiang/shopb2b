package com.hongqiang.shop.common.utils.plugin.dao;

import org.springframework.data.repository.CrudRepository;
import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.website.entity.PluginConfig;

public interface PluginConfigDao extends PluginConfigDaoCustom, CrudRepository<PluginConfig, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface PluginConfigDaoCustom extends BaseDao<PluginConfig,Long> {
  public boolean pluginIdExists(String pluginId);

  public PluginConfig findByPluginId(String pluginId);
}