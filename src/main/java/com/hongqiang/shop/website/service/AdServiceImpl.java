package com.hongqiang.shop.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.dao.AdDao;
import com.hongqiang.shop.website.entity.Ad;

@Service
public class AdServiceImpl extends BaseService implements AdService {
	@Autowired
	private AdDao adDao;

	@Transactional
	@CacheEvict(value = { "adPosition" })
	public Ad find(Long id) {
		return this.adDao.find(id);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" })
	public Page<Ad> findPage(Pageable pageable) {
		return this.adDao.findPage(pageable);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" }, allEntries = true)
	public void save(Ad ad) {
		this.adDao.persist(ad);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" }, allEntries = true)
	public Ad update(Ad ad) {
		return (Ad) this.adDao.merge(ad);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" }, allEntries = true)
	public Ad update(Ad ad, String[] ignoreProperties) {
		return (Ad) this.adDao.update(ad, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" }, allEntries = true)
	public void delete(Long id) {
		this.adDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.adDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "adPosition" }, allEntries = true)
	public void delete(Ad ad) {
		this.adDao.delete(ad);
	}
}