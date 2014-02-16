package com.hongqiang.shop.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.dao.SeoDao;
import com.hongqiang.shop.website.entity.Seo;

@Service
public class SeoServiceImpl extends BaseService implements SeoService {

	@Autowired
	private SeoDao seoDao;

	@Transactional(readOnly = true)
	public Seo find(Seo.Type type) {
		return this.seoDao.findByType(type);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "seo" })
	public Seo find(Seo.Type type, String cacheRegion) {
		return this.seoDao.findByType(type);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "seo" })
	public Seo find(Long id) {
		return this.seoDao.find(id);
	}

	@Transactional
	@Cacheable({ "seo" })
	public Page<Seo> findPage(Pageable pageable) {
		return this.seoDao.findPage(pageable);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void save(Seo seo) {
		this.seoDao.persist(seo);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public Seo update(Seo seo) {
		return (Seo) this.seoDao.merge(seo);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public Seo update(Seo seo, String[] ignoreProperties) {
		return (Seo) this.seoDao.update(seo, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void delete(Long id) {
		this.seoDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.seoDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void delete(Seo seo) {
		this.seoDao.delete(seo);
	}
}