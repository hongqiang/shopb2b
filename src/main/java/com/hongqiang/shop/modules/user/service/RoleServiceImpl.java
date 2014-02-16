package com.hongqiang.shop.modules.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Role;
import com.hongqiang.shop.modules.user.dao.RoleDao;

@Service
public class RoleServiceImpl extends BaseService implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Transactional(readOnly = true)
	public Role find(Long id) {
		return this.roleDao.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<Role> findPage(Pageable pageable) {
		return this.roleDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	public List<Role> findList(Long[] ids) {
		List<Role> localArrayList = new ArrayList<Role>();
		if (ids != null)
			for (Long id : ids) {
				Role localObject = this.roleDao.findById(id);
				if (localObject == null)
					continue;
				localArrayList.add(localObject);
			}
		return localArrayList;
	}

	@Transactional(readOnly = true)
	public List<Role> findAll() {
		return this.roleDao.findList();
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void save(Role role) {
		this.roleDao.persist(role);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Role update(Role role) {
		return (Role) this.roleDao.merge(role);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Role update(Role role, String[] ignoreProperties) {
		return (Role) this.roleDao.update(role, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Long id) {
		this.roleDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.roleDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Role role) {
		this.roleDao.delete(role);
	}
}