package com.hongqiang.shop.modules.user.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Principal;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.Role;
import com.hongqiang.shop.modules.user.dao.AdminDao;

@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseService implements AdminService {

	@Autowired
	private AdminDao adminDao;

	@Transactional(readOnly = true)
	public Admin find(Long id) {
		return this.adminDao.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<Admin> findPage(Pageable pageable) {
		return this.adminDao.findPage(pageable);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void save(Admin admin) {
		this.adminDao.persist(admin);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Long id) {
		this.adminDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.adminDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Admin admin) {
		this.adminDao.delete(admin);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Admin update(Admin admin) {
		return (Admin) this.adminDao.merge(admin);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Admin update(Admin admin, String[] ignoreProperties){
		return (Admin) this.adminDao.update(admin, ignoreProperties);
	}
	
	@Transactional(readOnly = true)
	public Long count() {
		return (Long) this.adminDao.count();
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return this.adminDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return this.adminDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<String> findAuthorities(Long id) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		Admin localAdmin = (Admin) this.adminDao.findById(id);
		if (localAdmin != null) {
			Iterator<Role> localIterator = localAdmin.getRoles().iterator();
			while (localIterator.hasNext()) {
				Role localRole = (Role) localIterator.next();
				localArrayList.addAll(localRole.getAuthorities());
			}
		}
		return localArrayList;
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		Subject localSubject = SecurityUtils.getSubject();
		if (localSubject != null)
			return localSubject.isAuthenticated();
		return false;
	}

	@Transactional(readOnly = true)
	public Admin getCurrent() {
		Subject localSubject = SecurityUtils.getSubject();
		if (localSubject != null) {
			Principal localPrincipal = (Principal) localSubject.getPrincipal();
			if (localPrincipal != null)
				return (Admin) this.adminDao.findById(localPrincipal.getId());
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		Subject localSubject = SecurityUtils.getSubject();
		if (localSubject != null) {
			Principal localPrincipal = (Principal) localSubject.getPrincipal();
			if (localPrincipal != null)
				return localPrincipal.getUsername();
		}
		return null;
	}
}
