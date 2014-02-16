package com.hongqiang.shop.website.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.Seo;

public interface SeoService{

  public Seo find(Seo.Type paramType);

  public Seo find(Seo.Type paramType, String paramString);
  
  	public Seo find(Long id);
	
	public Page<Seo> findPage(Pageable pageable);
	
	public void save(Seo seo);

  public Seo update(Seo seo);

  public Seo update(Seo seo, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(Seo seo);
}