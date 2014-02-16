package com.hongqiang.shop.website.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.dao.AdPositionDao;
import com.hongqiang.shop.website.entity.AdPosition;

@Service
public class AdPositionServiceImpl extends BaseService
  implements AdPositionService
{

  @Autowired
  private AdPositionDao adPositionDao;

  @Transactional(readOnly=true)
  public AdPosition find(Long id){
	return (AdPosition)this.adPositionDao.find(id);
  }
  
  @Transactional(readOnly=true)
  @Cacheable({"adPosition"})
  public AdPosition find(Long id, String cacheRegion)
  {
    return (AdPosition)this.adPositionDao.find(id);
  }

  @Transactional(readOnly=true)
  public List<AdPosition> findAll(){
	return this.adPositionDao.findAll();
  }
  
  @Transactional(readOnly=true)
  @Cacheable({"adPosition"})
  public Page<AdPosition> findPage(Pageable pageable){
	return this.adPositionDao.findPage(pageable);
  }
  
  @Transactional
  @CacheEvict(value={"adPosition"}, allEntries=true)
  public void save(AdPosition adPosition)
  {
    this.adPositionDao.persist(adPosition);
  }

  @Transactional
  @CacheEvict(value={"adPosition"}, allEntries=true)
  public AdPosition update(AdPosition adPosition)
  {
    return (AdPosition)this.adPositionDao.merge(adPosition);
  }

  @Transactional
  @CacheEvict(value={"adPosition"}, allEntries=true)
  public AdPosition update(AdPosition adPosition, String[] ignoreProperties)
  {
    return (AdPosition)this.adPositionDao.update(adPosition, ignoreProperties);
  }

  @Transactional
  @CacheEvict(value={"adPosition"}, allEntries=true)
  public void delete(Long id)
  {
    this.adPositionDao.delete(id);
  }

  @Transactional
  @CacheEvict(value={"adPosition"}, allEntries=true)
  public void delete(Long[] ids)
  {
    if (ids != null)
		for (Long localSerializable : ids)
			this.adPositionDao.delete(localSerializable);
  }

  @Transactional
  @CacheEvict(value={"adPosition"}, allEntries=true)
  public void delete(AdPosition adPosition)
  {
    this.adPositionDao.delete(adPosition);
  }
}