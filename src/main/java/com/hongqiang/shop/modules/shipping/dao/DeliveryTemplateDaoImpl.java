package com.hongqiang.shop.modules.shipping.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.DeliveryTemplate;

@Repository
public class DeliveryTemplateDaoImpl extends BaseDaoImpl<DeliveryTemplate,Long>
		implements DeliveryTemplateDaoCustom {
	@Override
	public DeliveryTemplate findDefault() {
		try {
			String str = "select deliveryTemplate from DeliveryTemplate deliveryTemplate where deliveryTemplate.isDefault = true";
			return (DeliveryTemplate) this.getEntityManager()
					.createQuery(str, DeliveryTemplate.class)
					.setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException localNoResultException) {
		}
		return null;
	}

	@Override
	public  List<DeliveryTemplate> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
		String qlString = "select deliveryTemplate from DeliveryTemplate deliveryTemplate where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
	
	@Override
	public List<DeliveryTemplate> findAll(){
		return findList(null, null, null, null);
	}
	
	@Override
	public Page<DeliveryTemplate> findPage(Pageable pageable) {
		String qlString = "select deliveryTemplate from DeliveryTemplate deliveryTemplate where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter,
				pageable);
	}

	@Override
	public void persist(DeliveryTemplate deliveryTemplate) {
		if (deliveryTemplate.getIsDefault().booleanValue()) {
			String str = "update DeliveryTemplate deliveryTemplate set deliveryTemplate.isDefault = false "+
								"where deliveryTemplate.isDefault = true";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT).executeUpdate();
		}
		super.persist(deliveryTemplate);
	}

	@Override
	public DeliveryTemplate merge(DeliveryTemplate deliveryTemplate) {
		if (deliveryTemplate.getIsDefault().booleanValue()) {
			String str = "update DeliveryTemplate deliveryTemplate set deliveryTemplate.isDefault = false "+
								"where deliveryTemplate.isDefault = true and deliveryTemplate != :deliveryTemplate";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("deliveryTemplate", deliveryTemplate)
					.executeUpdate();
		}
		return (DeliveryTemplate) super.merge(deliveryTemplate);
	}
}