package com.hongqiang.shop.modules.user.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role,Long> implements RoleDaoCustom {

	@Override
	public Page<Role> findPage(Pageable pageable) {
		String qlString = "select role from Role role where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable);
	}

	@Override
	public List<Role> findList() {
		String qlString =  "select role from Role role where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, null, null, null, null);
	}
}
