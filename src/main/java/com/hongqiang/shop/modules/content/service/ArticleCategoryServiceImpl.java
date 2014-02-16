package com.hongqiang.shop.modules.content.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.modules.content.dao.ArticleCategoryDao;
import com.hongqiang.shop.modules.entity.ArticleCategory;

@Service
public class ArticleCategoryServiceImpl extends BaseService
  implements ArticleCategoryService
{

  @Autowired
  private ArticleCategoryDao articleCategoryDao;

  @Transactional(readOnly=true)
  public List<ArticleCategory> findRoots()
  {
    return this.articleCategoryDao.findRoots(null);
  }

  @Transactional(readOnly=true)
  public List<ArticleCategory> findRoots(Integer count)
  {
    return this.articleCategoryDao.findRoots(count);
  }

  @Transactional(readOnly=true)
  @Cacheable({"articleCategory"})
  public List<ArticleCategory> findRoots(Integer count, String cacheRegion)
  {
    return this.articleCategoryDao.findRoots(count);
  }

  @Transactional(readOnly=true)
  public List<ArticleCategory> findParents(ArticleCategory articleCategory)
  {
    return this.articleCategoryDao.findParents(articleCategory, null);
  }

  @Transactional(readOnly=true)
  public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count)
  {
    return this.articleCategoryDao.findParents(articleCategory, count);
  }

  @Transactional(readOnly=true)
  @Cacheable({"articleCategory"})
  public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count, String cacheRegion)
  {
    return this.articleCategoryDao.findParents(articleCategory, count);
  }

  @Transactional(readOnly=true)
  public List<ArticleCategory> findTree()
  {
    return this.articleCategoryDao.findChildren(null, null);
  }

  @Transactional(readOnly=true)
  public List<ArticleCategory> findChildren(ArticleCategory articleCategory)
  {
    return this.articleCategoryDao.findChildren(articleCategory, null);
  }

  @Transactional(readOnly=true)
  public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count)
  {
    return this.articleCategoryDao.findChildren(articleCategory, count);
  }

  @Transactional(readOnly=true)
  @Cacheable({"articleCategory"})
  public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count, String cacheRegion)
  {
    return this.articleCategoryDao.findChildren(articleCategory, count);
  }

  @Transactional
  @CacheEvict(value={"article", "articleCategory"}, allEntries=true)
  public void save(ArticleCategory articleCategory)
  {
    this.articleCategoryDao.persist(articleCategory);
  }

  @Transactional
  @CacheEvict(value={"article", "articleCategory"}, allEntries=true)
  public ArticleCategory update(ArticleCategory articleCategory)
  {
    return (ArticleCategory)this.articleCategoryDao.merge(articleCategory);
  }

  @Transactional
  @CacheEvict(value={"article", "articleCategory"}, allEntries=true)
  public ArticleCategory update(ArticleCategory articleCategory, String[] ignoreProperties)
  {
    return (ArticleCategory)this.articleCategoryDao.update(articleCategory, ignoreProperties);
  }

  @Transactional
  @CacheEvict(value={"article", "articleCategory"}, allEntries=true)
  public void delete(Long id)
  {
    this.articleCategoryDao.delete(id);
  }

  @Transactional
  @CacheEvict(value={"article", "articleCategory"}, allEntries=true)
  public void delete(Long[] ids)
  {
     if (ids != null)
			for (Long localSerializable : ids)
				this.articleCategoryDao.delete(localSerializable);
  }

  @Transactional
  @CacheEvict(value={"article", "articleCategory"}, allEntries=true)
  public void delete(ArticleCategory articleCategory)
  {
    this.articleCategoryDao.delete(articleCategory);
  }
  
  @Transactional
	public ArticleCategory find(Long id) {
		return this.articleCategoryDao.find(id);
	}
}