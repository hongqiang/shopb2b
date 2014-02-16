package com.hongqiang.shop.modules.content.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.entity.Tag;

@Repository
public class ArticleDaoImpl extends BaseDaoImpl<Article,Long>
  implements ArticleDaoCustom
{
  @Override
  public List<Article> findList(ArticleCategory articleCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders)
  {
	String qlString = "select DISTINCT article from Article article where 1=1 and article.isPublication = true ";
	List<Object> parameter = new ArrayList<Object>();
    if (articleCategory != null){
		qlString += " and (article.articleCategory = ? or article.articleCategory.treePath like ?) ";
		parameter.add(articleCategory);
		parameter.add("%," + articleCategory.getId() + "," + "%");
	}
    if ((tags != null) && (!tags.isEmpty())){
		qlString += " and (article.tags in (?)";
		parameter.add(tags);
    }
	qlString += " order by article.isTop DESC ";

    return super.findList(qlString, parameter,null, count, filters, orders);
  }

  @Override
  public List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count)
  {
	String qlString = "select DISTINCT article from Article article where 1=1 and article.isPublication = true ";
	List<Object> parameter = new ArrayList<Object>();
    if (articleCategory != null){
		qlString += " and (article.articleCategory = ? or article.articleCategory.treePath like ?) ";
		parameter.add(articleCategory);
		parameter.add("%," + articleCategory.getId() + "," + "%");
	}
    if (beginDate != null){
		qlString += " and article.createDate >= ? ";
		parameter.add(beginDate);
	}
    if (endDate != null){
		qlString += " and article.createDate <= ? ";
		parameter.add(endDate);
	}
	qlString += " order by article.isTop DESC ";

    return super.findList(qlString, parameter,first, count, null, null);
  }

  @Override
  public  List<Article> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
	String qlString = "select article from Article article where 1=1 ";
	List<Object> parameter = new ArrayList<Object>();
	return super.findList(qlString, parameter, first, count, filters, orders);
  }

  @Override  
  public Page<Article> findPage(Pageable pageable){
	String qlString = "select article from Article article where 1=1 ";
	List<Object> parameter = new ArrayList<Object>();
	return super.findPage(qlString,  parameter, pageable) ;
  }
  
  @Override
  public Page<Article> findPage(ArticleCategory articleCategory, List<Tag> tags, Pageable pageable)
  {
	String qlString = "select DISTINCT article from Article article where 1=1 and article.isPublication = true ";
	List<Object> parameter = new ArrayList<Object>();
    if (articleCategory != null){
		qlString += " and (article.articleCategory = ? or article.articleCategory.treePath like ?) ";
		parameter.add(articleCategory);
		parameter.add("%," + articleCategory.getId() + "," + "%");
	}
	if ((tags != null) && (!tags.isEmpty())){
		qlString += " and (article.tags in (?)";
		parameter.add(tags);
    }
	qlString += " order by article.isTop DESC ";
    return super.findPage(qlString, parameter, pageable);
  }
  
  @Override
  public long count(){
	  Filter [] filters = null;
	  return count(filters);
  }
  
  @Override
  public long count(Filter[] filters){
	  String qlString = "select DISTINCT article from Article article where 1=1 ";
	  StringBuilder stringBuilder = new StringBuilder(qlString);
	  List<Object> params = new ArrayList<Object>();
	  if (filters == null) {
		  return super.count(stringBuilder,null, params);
	}
	  return super.count(stringBuilder,Arrays.asList(filters), params);
  }
}