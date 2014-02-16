package com.hongqiang.shop.modules.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.MemberAttribute;
import com.hongqiang.shop.modules.user.dao.MemberAttributeDao;

@Service
public class MemberAttributeServiceImpl extends BaseService implements
		MemberAttributeService {

	@Autowired
	private MemberAttributeDao memberAttributeDao;

	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex() {
		return this.memberAttributeDao.findUnusedPropertyIndex();
	}

	@Transactional(readOnly = true)
	public List<MemberAttribute> findList() {
		return this.memberAttributeDao.findList();
	}

	@Transactional(readOnly = true)
	@Cacheable({ "memberAttribute" })
	public List<MemberAttribute> findList(String cacheRegion) {
		return this.memberAttributeDao.findList();
	}

	@Transactional(readOnly = true)
	@Cacheable({ "memberAttribute" })
	public MemberAttribute find(Long id) {
		return this.memberAttributeDao.findById(id);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "memberAttribute" })
	public Page<MemberAttribute> findPage(Pageable pageable) {
		return this.memberAttributeDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	public List<MemberAttribute> findAll() {
		return this.memberAttributeDao.findAll();
	}

	@Transactional(readOnly = true)
	public long count() {
		return this.memberAttributeDao.count();
	}

	@Transactional
	@CacheEvict(value = { "memberAttribute" }, allEntries = true)
	public void save(MemberAttribute memberAttribute) {
		this.memberAttributeDao.persist(memberAttribute);
	}

	@Transactional
	@CacheEvict(value = { "memberAttribute" }, allEntries = true)
	public MemberAttribute update(MemberAttribute memberAttribute) {
		return (MemberAttribute) this.memberAttributeDao.merge(memberAttribute);
	}

	@Transactional
	@CacheEvict(value = { "memberAttribute" }, allEntries = true)
	public MemberAttribute update(MemberAttribute memberAttribute,
			String[] ignoreProperties) {
		return (MemberAttribute) this.memberAttributeDao.update(
				memberAttribute, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "memberAttribute" }, allEntries = true)
	public void delete(Long id) {
		this.memberAttributeDao.delete(find(id));
	}

	@Transactional
	@CacheEvict(value = { "memberAttribute" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				delete(id);
	}

	@Transactional
	@CacheEvict(value = { "memberAttribute" }, allEntries = true)
	public void delete(MemberAttribute memberAttribute) {
		this.memberAttributeDao.delete(memberAttribute);
	}
}