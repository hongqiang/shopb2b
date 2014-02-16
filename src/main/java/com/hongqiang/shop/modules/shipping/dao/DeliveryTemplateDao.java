package com.hongqiang.shop.modules.shipping.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.DeliveryTemplate;

public interface DeliveryTemplateDao extends DeliveryTemplateDaoCustom,
		CrudRepository<DeliveryTemplate, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface DeliveryTemplateDaoCustom extends BaseDao<DeliveryTemplate,Long> {
	public DeliveryTemplate findDefault();

	public List<DeliveryTemplate> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<DeliveryTemplate> findAll();

	public Page<DeliveryTemplate> findPage(Pageable pageable);

	public void persist(DeliveryTemplate deliveryTemplate);

	public DeliveryTemplate merge(DeliveryTemplate deliveryTemplate);
}
