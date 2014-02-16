package com.hongqiang.shop.modules.account.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Promotion;

public interface PromotionDao extends PromotionDaoCustom,
		CrudRepository<Promotion, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface PromotionDaoCustom extends BaseDao<Promotion,Long> {
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders);

	public Page<Promotion> findPage(Pageable pageable);

	public List<Promotion> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<Promotion> findAll();
}