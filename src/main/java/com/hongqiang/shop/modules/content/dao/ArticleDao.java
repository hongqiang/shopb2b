package com.hongqiang.shop.modules.content.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.entity.Tag;

public interface ArticleDao extends ArticleDaoCustom, CrudRepository<Article, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ArticleDaoCustom extends BaseDao<Article,Long> {
  public List<Article> findList(ArticleCategory paramArticleCategory, List<Tag> paramList, Integer paramInteger, List<Filter> paramList1, List<Order> paramList2);

  public List<Article> findList(ArticleCategory paramArticleCategory, Date paramDate1, Date paramDate2, Integer paramInteger1, Integer paramInteger2);
	
  public  List<Article> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);
  
  public Page<Article> findPage(Pageable pageable);
  
  public Page<Article> findPage(ArticleCategory paramArticleCategory, List<Tag> paramList, Pageable paramPageable);
  
  public long count();
  
  public long count(Filter[] filters);
  
}