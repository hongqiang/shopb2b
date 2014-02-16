package com.hongqiang.shop.common.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.hongqiang.shop.modules.entity.BaseEntity;

public class EntityListener
{
  @PrePersist
  public void prePersist(BaseEntity entity)
  {
    entity.setCreateDate(new Date());
    entity.setUpdateDate(new Date());
  }

  @PreUpdate
  public void preUpdate(BaseEntity entity)
  {
    entity.setUpdateDate(new Date());
  }
}