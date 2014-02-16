package com.hongqiang.shop.modules.user.dao;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.modules.entity.Area;

@Repository
public class AreaDaoImpl extends BaseDaoImpl<Area,Long> implements AreaDaoCustom {
	@Override
	public List<Area> findRoots(Integer count) {
		String str = "select area from Area area where area.parent is null order by area.order asc";
		TypedQuery<Area> localTypedQuery = this.getEntityManager()
				.createQuery(str, Area.class)
				.setFlushMode(FlushModeType.COMMIT);
		if (count != null)
			localTypedQuery.setMaxResults(count.intValue());
		return localTypedQuery.getResultList();
	}
}