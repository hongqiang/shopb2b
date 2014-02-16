package com.hongqiang.shop.website.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.website.entity.Navigation;

@Repository
public class NavigationDaoImpl extends BaseDaoImpl<Navigation,Long> implements
		NavigationDaoCustom {
	@Override
	public List<Navigation> findList(Navigation.Position position) {
		CriteriaBuilder localCriteriaBuilder = this.getEntityManager()
				.getCriteriaBuilder();
		CriteriaQuery<Navigation> localCriteriaQuery = localCriteriaBuilder
				.createQuery(Navigation.class);
		Root<Navigation> localRoot = localCriteriaQuery.from(Navigation.class);
		localCriteriaQuery.select(localRoot);
		if (position != null)
			localCriteriaQuery.where(localCriteriaBuilder.equal(
					localRoot.get("position"), position));
		localCriteriaQuery.orderBy(new Order[] { localCriteriaBuilder
				.asc(localRoot.get("order")) });
		return this.getEntityManager().createQuery(localCriteriaQuery)
				.setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	@Override
	public List<Navigation> findList(Integer first, Integer count,
			List<Filter> filters,
			List<com.hongqiang.shop.common.utils.Order> orders) {
		String qlString = "select navigation from Navigation navigation where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,orders);
	}
}