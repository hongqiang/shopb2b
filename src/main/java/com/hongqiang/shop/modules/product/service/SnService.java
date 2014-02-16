package com.hongqiang.shop.modules.product.service;

import com.hongqiang.shop.modules.entity.Sn;

public abstract interface SnService
{
  public abstract String generate(Sn.Type paramType);
}