package com.hongqiang.shop.modules.product.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ParameterGroup;

public abstract interface ParameterGroupDao extends ParameterGroupDaoCustom, CrudRepository<ParameterGroup, Long>{
	
	public ParameterGroup findById(Long id);
	
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ParameterGroupDaoCustom extends BaseDao<ParameterGroup,Long> {

	public Page<ParameterGroup> findPage(Pageable pageable);
	
	public ParameterGroup merge(ParameterGroup parameterGroup);
	
	public void remove(ParameterGroup parameterGroup);

}