package com.hongqiang.shop.website.service;

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
import com.hongqiang.shop.website.dao.FriendLinkDao;
import com.hongqiang.shop.website.entity.FriendLink;

@Service
public class FriendLinkServiceImpl extends BaseService implements
		FriendLinkService {

	@Autowired
	public FriendLinkDao friendLinkDao;

	@Transactional(readOnly = true)
	public List<FriendLink> findList(FriendLink.Type type) {
		return this.friendLinkDao.findList(type);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "friendLink" })
	public List<FriendLink> findList(Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion) {
		return this.friendLinkDao.findList(null, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<FriendLink> findList(Integer count,
			List<Filter> filters, List<Order> orders) {
		return this.friendLinkDao.findList(null, count, filters, orders);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" })
	public FriendLink find(Long id) {
		return this.friendLinkDao.find(id);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" })
	public Page<FriendLink> findPage(Pageable pageable) {
		return this.friendLinkDao.findPage(pageable);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void save(FriendLink friendLink) {
		this.friendLinkDao.persist(friendLink);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public FriendLink update(FriendLink friendLink) {
		return (FriendLink) this.friendLinkDao.merge(friendLink);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public FriendLink update(FriendLink friendLink, String[] ignoreProperties) {
		return (FriendLink) this.friendLinkDao.update(friendLink,
				ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void delete(Long id) {
		this.friendLinkDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.friendLinkDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void delete(FriendLink friendLink) {
		this.friendLinkDao.delete(friendLink);
	}
}