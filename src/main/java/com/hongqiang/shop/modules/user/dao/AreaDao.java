package com.hongqiang.shop.modules.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.modules.entity.Area;

public interface AreaDao extends AreaDaoCustom, CrudRepository<Area, Long>{

	public Area findById(Long id);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface AreaDaoCustom extends BaseDao<Area,Long> {

  public List<Area> findRoots(Integer paramInteger);
  
}