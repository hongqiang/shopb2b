package com.hongqiang.shop.modules.util.service;

import java.math.BigDecimal;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.Product;

public interface SearchService
{
  public void index();

  public void index(Class<?> paramClass);

  public void index(Article paramArticle);

  public void index(Product paramProduct);

  public void purge();

  public void purge(Class<?> paramClass);

  public void purge(Article paramArticle);

  public void purge(Product paramProduct);

  public Page<Article> search(String paramString, Pageable paramPageable);

  public Page<Product> search(String paramString, BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2, Product.OrderType paramOrderType, Pageable paramPageable);
}