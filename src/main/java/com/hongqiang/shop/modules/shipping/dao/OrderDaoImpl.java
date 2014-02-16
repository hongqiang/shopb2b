package com.hongqiang.shop.modules.shipping.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.OrderItem;
import com.hongqiang.shop.modules.entity.Product;

@Repository
public class OrderDaoImpl extends
		BaseDaoImpl<com.hongqiang.shop.modules.entity.Order,Long> implements OrderDaoCustom {

	@Override
	public com.hongqiang.shop.modules.entity.Order findBySn(String sn) {
		if (sn == null)
			return null;
		String str = "select orders from Order orders where lower(orders.sn) = lower(:sn)";
		try {
			return (com.hongqiang.shop.modules.entity.Order) this.getEntityManager()
					.createQuery(str, com.hongqiang.shop.modules.entity.Order.class)
					.setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn)
					.getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}
	
	@Override
	public List<com.hongqiang.shop.modules.entity.Order> findList(
			Member member, Integer count, List<Filter> filters,List<Order> orders) {
		if (member == null)
			return Collections.emptyList();
		String qlString = "select o from Order o where 1=1 and o.member = ? ";
		List<Object> parameter = new ArrayList<Object>();
		parameter.add(member);
		return super.findList(qlString, parameter, null, count, filters, orders);
	}

	@Override
	public Page<com.hongqiang.shop.modules.entity.Order> findPage(Member member, Pageable pageable) {
		if (member == null){
			List<com.hongqiang.shop.modules.entity.Order> orders = 
					new ArrayList<com.hongqiang.shop.modules.entity.Order>();
			return new Page<com.hongqiang.shop.modules.entity.Order>(orders,0L,pageable);
		}
		String qlString = "select o from Order o where 1=1 and o.member = ? ";
		List<Object> parameter = new ArrayList<Object>();
		parameter.add(member);
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public Page<com.hongqiang.shop.modules.entity.Order> findPage(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired, Pageable pageable) {
		String qlString = "select o from Order o where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		if (orderStatus != null) {
			qlString += " and o.orderStatus= ? ";
			parameter.add(orderStatus);
		}
		if (paymentStatus != null) {
			qlString += " and o.paymentStatus= ? ";
			parameter.add(paymentStatus);
		}
		if (shippingStatus != null) {
			qlString += " and o.shippingStatus= ? ";
			parameter.add(shippingStatus);
		}
		Date nowadays = new Date();
		if (hasExpired != null) {
			if (hasExpired.booleanValue()) {
				qlString += " and o.expire is not null and  o.expire < ? ";
				parameter.add(nowadays);
			} else {
				qlString += " and (o.expire is null or  o.expire >= ?) ";
				parameter.add(nowadays);
			}
		}
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public Long count(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired) {
		String qlString = "select o from Order o where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		if (orderStatus != null) {
			qlString += " and o.orderStatus= ? ";
			parameter.add(orderStatus);
		}
		if (paymentStatus != null) {
			qlString += " and o.paymentStatus= ? ";
			parameter.add(paymentStatus);
		}
		if (shippingStatus != null) {
			qlString += " and o.shippingStatus= ? ";
			parameter.add(shippingStatus);
		}
		Date nowadays = new Date();
		if (hasExpired != null) {
			if (hasExpired.booleanValue()) {
				qlString += " and o.expire is not null and  o.expire < ? ";
				parameter.add(nowadays);
			} else {
				qlString += " and (o.expire is null or  o.expire >= ?) ";
				parameter.add(nowadays);
			}
		}
		StringBuilder stringBuilder = new StringBuilder(qlString);
		return super.count(stringBuilder, null, parameter);
	}

	@Override
	public Long waitingPaymentCount(Member member) {
		String qlString = "select o from Order o where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		qlString += " and o.orderStatus <> ? and o.orderStatus <> ? ";
		parameter.add(com.hongqiang.shop.modules.entity.Order.OrderStatus.completed);
		parameter.add(com.hongqiang.shop.modules.entity.Order.OrderStatus.cancelled);
		qlString += " and (o.paymentStatus = ? or o.paymentStatus = ?) ";
		parameter.add(com.hongqiang.shop.modules.entity.Order.PaymentStatus.unpaid);
		parameter.add(com.hongqiang.shop.modules.entity.Order.PaymentStatus.partialPayment);
		qlString += " and (o.expire is null or o.expire >= ? ) ";
		parameter.add(new Date());
		if (member != null) {
			qlString += " and o.member = ? ";
			parameter.add(member);
		}
		StringBuilder stringBuilder = new StringBuilder(qlString);
		return super.count(stringBuilder, null, parameter);
	}

	@Override
	public Long waitingShippingCount(Member member) {
		String qlString = "select o from Order o where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		qlString += " and o.orderStatus <> ? and o.orderStatus <> ? ";
		parameter.add(com.hongqiang.shop.modules.entity.Order.OrderStatus.completed);
		parameter.add(com.hongqiang.shop.modules.entity.Order.OrderStatus.cancelled);
		qlString += " and (o.paymentStatus = ?  and o.shippingStatus = ?)";
		parameter.add(com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid);
		parameter.add(com.hongqiang.shop.modules.entity.Order.ShippingStatus.unshipped);
		qlString += " and (o.expire is null or  o.expire >= ? )";
		parameter.add(new Date());
		if (member != null) {
			qlString += " and o.member = ? ";
			parameter.add(member);
		}
		StringBuilder stringBuilder = new StringBuilder(qlString);
		return super.count(stringBuilder, null, parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
		CriteriaBuilder localCriteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<BigDecimal> localCriteriaQuery = localCriteriaBuilder.createQuery(BigDecimal.class);
		@SuppressWarnings("rawtypes")
		Root localRoot = localCriteriaQuery.from(com.hongqiang.shop.modules.entity.Order.class);
		localCriteriaQuery.select(localCriteriaBuilder.sum(localRoot.get("amountPaid")));
		Predicate localPredicate = localCriteriaBuilder.conjunction();
		localPredicate = localCriteriaBuilder.and(localPredicate,
				localCriteriaBuilder.equal(localRoot.get("orderStatus"),
						com.hongqiang.shop.modules.entity.Order.OrderStatus.completed));
		if (beginDate != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("createDate"), beginDate));
		if (endDate != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.lessThanOrEqualTo(localRoot.get("createDate"), endDate));
		localCriteriaQuery.where(localPredicate);
		return (BigDecimal) this.getEntityManager().createQuery(localCriteriaQuery)
				.setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getSalesVolume(Date beginDate, Date endDate) {
		CriteriaBuilder localCriteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Integer> localCriteriaQuery = localCriteriaBuilder.createQuery(Integer.class);
		@SuppressWarnings("rawtypes")
		Root localRoot = localCriteriaQuery.from(com.hongqiang.shop.modules.entity.Order.class);
		localCriteriaQuery.select(localCriteriaBuilder.sum(localRoot.join("orderItems").get("shippedQuantity")));
		Predicate localPredicate = localCriteriaBuilder.conjunction();
		localPredicate = localCriteriaBuilder.and(localPredicate,
				localCriteriaBuilder.equal(localRoot.get("orderStatus"),
						com.hongqiang.shop.modules.entity.Order.OrderStatus.completed));
		if (beginDate != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("createDate"), beginDate));
		if (endDate != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.lessThanOrEqualTo(localRoot.get("createDate"), endDate));
		localCriteriaQuery.where(localPredicate);
		return (Integer) this.getEntityManager().createQuery(localCriteriaQuery)
				.setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}

	@Override
	public void releaseStock() {
		String query = "select orders from Order orders where orders.isAllocatedStock = :isAllocatedStock "+
							"and orders.expire is not null and orders.expire <= :now";
		List<com.hongqiang.shop.modules.entity.Order> orders = this.getEntityManager()
				.createQuery(query, com.hongqiang.shop.modules.entity.Order.class)
				.setParameter("isAllocatedStock", Boolean.valueOf(true))
				.setParameter("now", new Date()).getResultList();
		if (orders != null) {
			Iterator<com.hongqiang.shop.modules.entity.Order> iterator = orders.iterator();
			while (iterator.hasNext()) {
				com.hongqiang.shop.modules.entity.Order order = (com.hongqiang.shop.modules.entity.Order) iterator.next();
				if ((order == null) || (order.getOrderItems() == null))
					continue;
				Iterator<OrderItem> orderItemIterator = order.getOrderItems().iterator();
				while (orderItemIterator.hasNext()) {
					OrderItem orderItem = (OrderItem) orderItemIterator.next();
					if (orderItem == null)
						continue;
					Product product = orderItem.getProduct();
					if (product == null)
						continue;
					this.getEntityManager().lock(product,LockModeType.PESSIMISTIC_WRITE);
					product.setAllocatedStock(Integer.valueOf(product.getAllocatedStock().intValue()
											- (orderItem.getQuantity().intValue() - orderItem.getShippedQuantity().intValue())));
				}
				order.setIsAllocatedStock(Boolean.valueOf(false));
			}
		}
	}
}