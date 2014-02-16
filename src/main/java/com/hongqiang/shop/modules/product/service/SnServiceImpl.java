package com.hongqiang.shop.modules.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.modules.entity.Sn;
import com.hongqiang.shop.modules.product.dao.SnDao;

@Service
public class SnServiceImpl
  implements SnService
{

  @Autowired
  private SnDao snDao;

  @Transactional
  public String generate(Sn.Type type)
  {
    return this.snDao.generate(type);
  }
}