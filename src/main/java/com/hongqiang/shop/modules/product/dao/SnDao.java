package com.hongqiang.shop.modules.product.dao;

import com.hongqiang.shop.modules.entity.Sn;


public abstract interface SnDao
{
  public abstract String generate(Sn.Type paramType);
}