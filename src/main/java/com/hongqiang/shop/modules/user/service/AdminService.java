package com.hongqiang.shop.modules.user.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Admin;

public abstract interface AdminService {

	public Admin find(Long id);

	public Page<Admin> findPage(Pageable pageable);

	public void save(Admin admin);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Admin admin);

	public Admin update(Admin admin);
	
	public Admin update(Admin admin, String[] ignoreProperties);

	public Long count();

	public abstract boolean usernameExists(String paramString);

	public abstract Admin findByUsername(String paramString);

	public abstract List<String> findAuthorities(Long paramLong);

	public abstract boolean isAuthenticated();

	public abstract Admin getCurrent();

	public abstract String getCurrentUsername();
}