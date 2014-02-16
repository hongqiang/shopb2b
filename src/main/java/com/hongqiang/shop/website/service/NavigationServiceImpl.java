package com.hongqiang.shop.website.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.website.dao.NavigationDao;
import com.hongqiang.shop.website.entity.Navigation;

@Service
public class NavigationServiceImpl extends BaseService
  implements NavigationService
{

  @Autowired
  private NavigationDao navigationDao;

  @Transactional(readOnly=true)
  public List<Navigation> findList(Navigation.Position position)
  {
    return this.navigationDao.findList(position);
  }

  @Transactional(readOnly=true)
  @Cacheable({"navigation"})
  public List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion)
  {
    return this.navigationDao.findList(null, count, filters, orders);
  }
  
  @Transactional(readOnly=true)
  public List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders)
  {
    return this.navigationDao.findList(null, count, filters, orders);
  }

    @Transactional
  @CacheEvict(value={"navigation"})
	public Navigation find(Long id) {
		return this.navigationDao.find(id);
	}
  
  @Transactional
  @CacheEvict(value={"navigation"}, allEntries=true)
  public void save(Navigation navigation)
  {
    this.navigationDao.persist(navigation);
  }

  @Transactional
  @CacheEvict(value={"navigation"}, allEntries=true)
  public Navigation update(Navigation navigation)
  {
    return (Navigation)this.navigationDao.merge(navigation);
  }

  @Transactional
  @CacheEvict(value={"navigation"}, allEntries=true)
  public Navigation update(Navigation navigation, String[] ignoreProperties)
  {
    return (Navigation)this.navigationDao.update(navigation, ignoreProperties);
  }

  @Transactional
  @CacheEvict(value={"navigation"}, allEntries=true)
  public void delete(Long id)
  {
    this.navigationDao.delete(id);
  }

  @Transactional
  @CacheEvict(value={"navigation"}, allEntries=true)
  public void delete(Long[] ids)
  {
    if (ids != null)
			for (Long id : ids)
				this.navigationDao.delete(id);
  }

  @Transactional
  @CacheEvict(value={"navigation"}, allEntries=true)
  public void delete(Navigation navigation)
  {
    this.navigationDao.delete(navigation);
  }
}