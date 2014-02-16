package com.hongqiang.shop.modules.content.service;

import java.util.Date;
import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.entity.Tag;

public interface ArticleService 
{
  public List<Article> findList(ArticleCategory paramArticleCategory, List<Tag> paramList, Integer paramInteger, List<Filter> paramList1, List<Order> paramList2);

  public List<Article> findList(ArticleCategory paramArticleCategory, List<Tag> paramList, Integer paramInteger, List<Filter> paramList1, List<Order> paramList2, String paramString);

  public List<Article> findList(ArticleCategory paramArticleCategory, Date paramDate1, Date paramDate2, Integer paramInteger1, Integer paramInteger2);

  public Page<Article> findPage(ArticleCategory paramArticleCategory, List<Tag> paramList, Pageable paramPageable);

  public Page<Article> findPage(Pageable pageable);
  
  public Article find(Long id);
  
  public long viewHits(Long id);
  
  public void save(Article article);
  
  public Article update(Article article);
  
  public Article update(Article article, String[] ignoreProperties);
  
  public void delete(Long id);
  
  public void delete(Long[] ids);
  
  public void delete(Article article);
  
  public int build(Article article);
  
  public int deleteStaticArticle(Article article);
}