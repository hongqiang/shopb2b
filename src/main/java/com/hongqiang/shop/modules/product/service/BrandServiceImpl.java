package com.hongqiang.shop.modules.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.product.dao.BrandDao;

@Service
public class BrandServiceImpl extends BaseService implements BrandService {

	@Autowired
	private BrandDao brandDao;

	@Transactional
	public Brand find(Long id) {
		return this.brandDao.findById(id);
	}

	@Transactional
	public Page<Brand> findPage(Pageable pageable) {
		return this.brandDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "brand" })
	public List<Brand> findList(Integer count, List<Filter> filters,List<Order> orders, String cacheRegion) {
		return this.brandDao.findList(null, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Brand> findList(Integer count, List<Filter> filters,List<Order> orders) {
		return this.brandDao.findList(null, count, filters, orders);
	}

	@Transactional
	public List<Brand> findList(Long[] ids) {
		List<Brand> localArrayList = new ArrayList<Brand>();
		if (ids != null)
			for (Long id : ids) {
				Brand localObject = find(id);
				if (localObject == null)
					continue;
				localArrayList.add(localObject);
			}
		return localArrayList;
	}

	@Transactional
	public List<Brand> findAll() {
		return this.brandDao.findAll();
	}

	@Transactional
	@CacheEvict(value = { "brand" }, allEntries = true)
	public void save(Brand brand) {
		this.brandDao.persist(brand);
	}

	@Transactional
	@CacheEvict(value = { "brand" }, allEntries = true)
	public Brand update(Brand brand) {
		return (Brand) this.brandDao.merge(brand);
	}

	@Transactional
	@CacheEvict(value = { "brand" }, allEntries = true)
	public Brand update(Brand brand, String[] ignoreProperties) {
		return (Brand) this.brandDao.update(brand, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "brand" }, allEntries = true)
	public void delete(Long id) {

		this.brandDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "brand" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.brandDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "brand" }, allEntries = true)
	public void delete(Brand brand) {
		this.brandDao.delete(brand);
	}
}