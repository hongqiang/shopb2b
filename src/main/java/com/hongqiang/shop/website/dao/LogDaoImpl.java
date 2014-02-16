package com.hongqiang.shop.website.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.Log;

@Repository
public class LogDaoImpl extends BaseDaoImpl<Log,Long> implements LogDaoCustom {
	@Override
	public void removeAll() {
		String str = "delete from Log log";
		this.getEntityManager().createQuery(str)
				.setFlushMode(FlushModeType.COMMIT).executeUpdate();
	}

	@Override
	public Page<Log> findPage(Pageable pageable) {
		String qlString = "select log from Log log where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter, pageable);
	}
}