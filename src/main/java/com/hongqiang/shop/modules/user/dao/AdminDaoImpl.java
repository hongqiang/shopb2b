package com.hongqiang.shop.modules.user.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Admin;

@Repository
public class AdminDaoImpl extends BaseDaoImpl<Admin,Long> implements AdminDaoCustom {
	@Override
	public boolean usernameExists(String username) {
		if (username == null)
			return false;
		String str = "select count(*) from Admin admin where lower(admin.username) = lower(:username)";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("username", username).getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public Page<Admin> findPage(Pageable pageable) {
		String sqlString = "select admin from Admin admin where 1=1 ";
		List<Object> paramsList = new ArrayList<Object>();
		return super.findPage(sqlString, paramsList, pageable);
	}
}