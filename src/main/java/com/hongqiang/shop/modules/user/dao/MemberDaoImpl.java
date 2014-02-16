package com.hongqiang.shop.modules.user.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Order;

@Repository
public class MemberDaoImpl extends BaseDaoImpl<Member, Long> implements
		MemberDaoCustom {

	@Override
	public Member findByUsername(String username) {
		if (username == null)
			return null;
		try {
			String str = "select members from Member members where lower(members.username) = lower(:username)";
			return (Member) this.getEntityManager()
					.createQuery(str, Member.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException localNoResultException1) {
		}
		return null;
	}

	@Override
	public List<Member> findListByEmail(String email) {
		if (email == null)
			return Collections.emptyList();
		String str = "select members from Member members where lower(members.email) = lower(:email)";
		return this.getEntityManager().createQuery(str, Member.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("email", email).getResultList();
	}

	public Long count(Date beginDate, Date endDate) {
		String sqlString = "select DISTINCT o.member "
				+ "from Order o where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (beginDate != null) {
			sqlString += " and o.createDate >= ?";
			params.add(beginDate);
		}
		if (endDate != null) {
			sqlString += " and o.createDate <= ?";
			params.add(endDate);
		}
		sqlString += " and o.orderStatus = ?";
		params.add(Order.OrderStatus.completed);
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		return super.count(stringBuilder, null, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Object> findPurchasePage(Date beginDate, Date endDate,
			Pageable pageable) {
		String sqlString = "select o.member, sum(o.amountPaid) "
				+ "from Order o where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (beginDate != null) {
			sqlString += " and o.createDate >= ?";
			params.add(beginDate);
		}
		if (endDate != null) {
			sqlString += " and o.createDate <= ?";
			params.add(endDate);
		}
		sqlString += " and o.orderStatus = ?";
		params.add(Order.OrderStatus.completed);

		sqlString += " and o.paymentStatus = ?";
		params.add(Order.PaymentStatus.paid);

		sqlString += " group by o.member.id";
		sqlString += " order by sum(o.amountPaid) DESC";
		Long count = count(beginDate, endDate);
		int i = (int) Math.ceil(count.longValue() / pageable.getPageSize());
		if (i < pageable.getPageNumber())
			pageable.setPageNumber(i);
		Query query = createQuery(sqlString, params.toArray());
		query.setFirstResult((pageable.getPageNumber() - 1)
				* pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Object> list = query.list();
		if (list.size() > 0) {
			return new Page<Object>(query.list(), count, pageable);
		}
		List<Object> listTemp = new ArrayList<Object>();
		return new Page<Object>(listTemp, count, pageable);
	}

	@Override
	public Page<Member> findPage(Pageable pageable) {
		String sqlString = "select members from Member members where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(sqlString, parameter, pageable);

	}

	@Override
	public boolean usernameExists(String username) {
		if (username == null)
			return false;
		String str = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("username", username).getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public boolean emailExists(String email) {
		if (email == null)
			return false;
		String str = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("email", email).getSingleResult();
		return localLong.longValue() > 0L;
	}
	
	@Override
	public Member update(Member member){
		Member retMember = null;
		if (member != null) {
			retMember = this.getEntityManager().merge(member);
		}
		return retMember;
	}
}