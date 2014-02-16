package com.hongqiang.shop.modules.product.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Specification;

public interface SpecificationDao extends SpecificationDaoCustom,
		CrudRepository<Specification, Long> {
	public Specification findById(Long id);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface SpecificationDaoCustom extends BaseDao<Specification, Long> {

	public Page<Specification> findPage(Pageable pageable);

	public List<Specification> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<Specification> findAll();

}