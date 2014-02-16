package com.hongqiang.shop.modules.product.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ParameterGroup;

public abstract interface ParameterGroupService {

	public ParameterGroup find(Long id);
	
	Page<ParameterGroup> findPage(Pageable pageable);

  public void save(ParameterGroup parameterGroup);

  public ParameterGroup update(ParameterGroup parameterGroup);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(ParameterGroup parameterGroup);
}