package com.hongqiang.shop.modules.content.service;

import java.util.List;

import com.hongqiang.shop.modules.entity.ArticleCategory;

public interface ArticleCategoryService{

  public List<ArticleCategory> findRoots();

  public List<ArticleCategory> findRoots(Integer paramInteger);

  public List<ArticleCategory> findRoots(Integer paramInteger, String paramString);

  public List<ArticleCategory> findParents(ArticleCategory paramArticleCategory);

  public List<ArticleCategory> findParents(ArticleCategory paramArticleCategory, Integer paramInteger);

  public List<ArticleCategory> findParents(ArticleCategory paramArticleCategory, Integer paramInteger, String paramString);

  public List<ArticleCategory> findTree();

  public List<ArticleCategory> findChildren(ArticleCategory paramArticleCategory);

  public List<ArticleCategory> findChildren(ArticleCategory paramArticleCategory, Integer paramInteger);

  public List<ArticleCategory> findChildren(ArticleCategory paramArticleCategory, Integer paramInteger, String paramString);
  
  public void save(ArticleCategory articleCategory);
  
  public ArticleCategory update(ArticleCategory articleCategory);
  
  public ArticleCategory update(ArticleCategory articleCategory, String[] ignoreProperties);
  
  public void delete(Long id);
  
  public void delete(Long[] ids);
  
  public void delete(ArticleCategory articleCategory);
  
  public ArticleCategory find(Long id);
}