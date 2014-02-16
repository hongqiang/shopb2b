package com.hongqiang.shop.modules.product.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Attribute;

public abstract interface AttributeService 
{
	public Attribute find(Long id);
	
	public Page<Attribute> findPage(Pageable pageable);

  public void save(Attribute attribute);

  public Attribute update(Attribute attribute);

  public Attribute update(Attribute attribute, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(Attribute attribute);
}