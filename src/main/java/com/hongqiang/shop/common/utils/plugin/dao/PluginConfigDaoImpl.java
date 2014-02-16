package com.hongqiang.shop.common.utils.plugin.dao;

import javax.persistence.FlushModeType;
import org.springframework.stereotype.Repository;
import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.website.entity.PluginConfig;

@Repository
public class PluginConfigDaoImpl extends BaseDaoImpl<PluginConfig, Long>
		implements PluginConfigDaoCustom {
	@Override
	public boolean pluginIdExists(String pluginId) {
		if (pluginId == null)
			return false;
		String str = "select count(*) from PluginConfig pluginConfig "+
						"where pluginConfig.pluginId = :pluginId";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("pluginId", pluginId).getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public PluginConfig findByPluginId(String pluginId) {
		if (pluginId == null)
			return null;
		try {
			String str = "select pluginConfig from PluginConfig pluginConfig "+
					"where pluginConfig.pluginId = :pluginId";
			return (PluginConfig) this.getEntityManager()
					.createQuery(str, PluginConfig.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("pluginId", pluginId).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}