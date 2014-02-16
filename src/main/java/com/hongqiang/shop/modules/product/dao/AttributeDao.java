package com.hongqiang.shop.modules.product.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Attribute;

public interface AttributeDao extends AttributeDaoCustom,CrudRepository<Attribute, Long>{
	public Attribute findById(Long id);
}

/**
 * DAO自定义接口
 * @author Jack
 *
 */
interface AttributeDaoCustom extends BaseDao<Attribute, Long> {

	public Page<Attribute>  findPage(Pageable pageable);

}