package com.hongqiang.shop.modules.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.user.dao.AreaDao;

@Service
public class AreaServiceImpl extends BaseService implements AreaService {

	@Autowired
	private AreaDao areaDao;

	@Transactional(readOnly = true)
	public List<Area> findRoots() {
		return this.areaDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<Area> findRoots(Integer count) {
		return this.areaDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public Area find(Long id) {
		return this.areaDao.findById(id);
	}

	@Transactional
	@CacheEvict(value = { "area" }, allEntries = true)
	public void save(Area area) {
		this.areaDao.save(area);
	}

	@Transactional
	@CacheEvict(value = { "area" }, allEntries = true)
	public Area update(Area area) {
		return (Area) this.areaDao.merge(area);
	}

	@Transactional
	@CacheEvict(value = { "area" }, allEntries = true)
	public Area update(Area area, String[] ignoreProperties) {
		return (Area) this.areaDao.update(area, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "area" }, allEntries = true)
	public void delete(Long id) {
		this.areaDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "area" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.areaDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "area" }, allEntries = true)
	public void delete(Area area) {
		this.areaDao.delete(area);
	}
}