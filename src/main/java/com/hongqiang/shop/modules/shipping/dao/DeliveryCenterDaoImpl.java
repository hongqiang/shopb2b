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
import com.hongqiang.shop.modules.entity.DeliveryCenter;

@Repository
public class DeliveryCenterDaoImpl extends BaseDaoImpl<DeliveryCenter,Long>
		implements DeliveryCenterDaoCustom {
	@Override
	public DeliveryCenter findDefault() {
		try {
			String str = "select deliveryCenter from DeliveryCenter deliveryCenter where deliveryCenter.isDefault = true";
			return (DeliveryCenter) this.getEntityManager()
					.createQuery(str, DeliveryCenter.class)
					.setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException localNoResultException) {
		}
		return null;
	}

	@Override
	public List<DeliveryCenter> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select deliveryCenter from DeliveryCenter deliveryCenter where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
	
	@Override
	public List<DeliveryCenter> findAll(){
		return findList(null, null, null, null);
	}
	
	@Override
	 public Page<DeliveryCenter> findPage(Pageable pageable){
			String qlString = "select deliveryCenter from DeliveryCenter deliveryCenter where 1=1 ";
			List<Object> parameter = new ArrayList<Object>();
			return super.findPage(qlString,  parameter, pageable) ;
	    }
	
	@Override
	public void persist(DeliveryCenter deliveryCenter) {
		if (deliveryCenter.getIsDefault().booleanValue()) {
			String str = "update DeliveryCenter deliveryCenter set deliveryCenter.isDefault = false where deliveryCenter.isDefault = true";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT).executeUpdate();
		}
		super.persist(deliveryCenter);
	}

	@Override
	public DeliveryCenter merge(DeliveryCenter deliveryCenter) {
		if (deliveryCenter.getIsDefault().booleanValue()) {
			String str = "update DeliveryCenter deliveryCenter set deliveryCenter.isDefault = false "+
								"where deliveryCenter.isDefault = true and deliveryCenter != :deliveryCenter";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("deliveryCenter", deliveryCenter)
					.executeUpdate();
		}
		return (DeliveryCenter) super.merge(deliveryCenter);
	}
}