package com.hongqiang.shop.modules.product.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Brand;

public interface BrandDao extends BrandDaoCustom, CrudRepository<Brand, Long>{
	public Brand findById(Long id);
	
}


/**
 * DAO自定义接口
 * @author Jack
 *
 */
interface BrandDaoCustom extends BaseDao<Brand,Long> {

	public Page<Brand>  findPage(Pageable pageable);
	
	public  List<Brand> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);
	
	public List<Brand> findAll();

}