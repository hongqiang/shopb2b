package com.hongqiang.shop.modules.content.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.modules.entity.ArticleCategory;

public interface ArticleCategoryDao extends ArticleCategoryDaoCustom, CrudRepository<ArticleCategory, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ArticleCategoryDaoCustom extends BaseDao<ArticleCategory,Long> {
  public List<ArticleCategory> findRoots(Integer paramInteger);

  public List<ArticleCategory> findParents(ArticleCategory paramArticleCategory, Integer paramInteger);

  public List<ArticleCategory> findChildren(ArticleCategory paramArticleCategory, Integer paramInteger);
  
  public void persist(ArticleCategory articleCategory);
  
  public ArticleCategory merge(ArticleCategory articleCategory);
  
  
}