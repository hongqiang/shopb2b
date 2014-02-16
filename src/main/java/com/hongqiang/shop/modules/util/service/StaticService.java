package com.hongqiang.shop.modules.util.service;

import java.util.Map;

import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.Product;

public interface StaticService
{
  public int build(String paramString1, String paramString2, Map<String, Object> paramMap);

  public int build(String paramString1, String paramString2);

  public int build(Article paramArticle);

  public int build(Product paramProduct);

  public int buildIndex();

  public int buildSitemap();

  public int buildOther();

  public int buildAll();

  public int delete(String paramString);

  public int delete(Article paramArticle);

  public int delete(Product paramProduct);

  public int deleteIndex();

  public int deleteOther();
}