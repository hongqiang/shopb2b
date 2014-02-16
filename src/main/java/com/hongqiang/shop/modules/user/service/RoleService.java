package com.hongqiang.shop.modules.user.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Role;

public interface RoleService {

	public Role find(Long id);

	public Page<Role> findPage(Pageable pageable);

	public List<Role> findList(Long[] ids);

	public List<Role> findAll();

	public void save(Role role);

	public Role update(Role role);

	public Role update(Role brand, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Role role);
}