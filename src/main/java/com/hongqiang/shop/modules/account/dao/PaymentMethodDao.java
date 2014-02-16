package com.hongqiang.shop.modules.account.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.PaymentMethod;

public interface PaymentMethodDao extends PaymentMethodDaoCustom,
		CrudRepository<PaymentMethod, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface PaymentMethodDaoCustom extends BaseDao<PaymentMethod, Long> {
	public Page<PaymentMethod> findPage(Pageable pageable);

	public List<PaymentMethod> findAll();

	public List<PaymentMethod> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);
}