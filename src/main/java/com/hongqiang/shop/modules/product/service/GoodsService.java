package com.hongqiang.shop.modules.product.service;

import com.hongqiang.shop.modules.entity.Goods;

public abstract interface GoodsService 
{
	
  public void save(Goods goods);

  public Goods update(Goods goods);

  public Goods update(Goods goods, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(Goods goods);
}