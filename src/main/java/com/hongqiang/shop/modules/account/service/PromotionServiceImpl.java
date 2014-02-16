package com.hongqiang.shop.modules.account.service;

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
import com.hongqiang.shop.modules.account.dao.PromotionDao;
import com.hongqiang.shop.modules.entity.Promotion;

@Service
public class PromotionServiceImpl extends BaseService
		implements PromotionService {

	@Autowired
	private PromotionDao promotionDao;

	@Transactional(readOnly = true)
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders) {
		return this.promotionDao.findList(hasBegun, hasEnded, count, filters,
				orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "promotion" })
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders,
			String cacheRegion) {
		return this.promotionDao.findList(hasBegun, hasEnded, count, filters,
				orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "promotion" })
	public Promotion find(Long id) {
		return this.promotionDao.find(id);
	}

	@Transactional(readOnly = true)
	public List<Promotion> findAll(){
		return this.promotionDao.findAll();
	}
	
	@Transactional(readOnly = true)
	@Cacheable({ "promotion" })
	public Page<Promotion> findPage(Pageable pageable) {
		return this.promotionDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "promotion" })
	public List<Promotion> findList(Integer count, List<Filter> filters,
			List<Order> orders) {
		return this.promotionDao.findList(null, count, filters, orders);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void save(Promotion promotion) {
		this.promotionDao.persist(promotion);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public Promotion update(Promotion promotion) {
		return (Promotion) this.promotionDao.merge(promotion);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public Promotion update(Promotion promotion, String[] ignoreProperties) {
		return (Promotion) this.promotionDao
				.update(promotion, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void delete(Long id) {
		this.promotionDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.promotionDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void delete(Promotion promotion) {
		this.promotionDao.delete(promotion);
	}
}