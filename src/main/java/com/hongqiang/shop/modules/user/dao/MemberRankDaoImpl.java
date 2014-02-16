package com.hongqiang.shop.modules.user.dao;

import java.math.BigDecimal;
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
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.entity.Product;

@Repository
public class MemberRankDaoImpl extends BaseDaoImpl<MemberRank,Long> implements
		MemberRankDaoCustom {
	@Override
	public boolean nameExists(String name) {
		if (name == null)
			return false;
		String str = "select count(*) from MemberRank memberRank where lower(memberRank.name) = lower(:name)";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT).setParameter("name", name)
				.getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public boolean amountExists(BigDecimal amount) {
		if (amount == null)
			return false;
		String str = "select count(*) from MemberRank memberRank where memberRank.amount = :amount";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("amount", amount).getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public MemberRank findDefault() {
		try {
			String str = "select memberRank from MemberRank memberRank where memberRank.isDefault = true";
			return (MemberRank) this.getEntityManager()
					.createQuery(str, MemberRank.class)
					.setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException localNoResultException) {
		}
		return null;
	}

	@Override
	public MemberRank findByAmount(BigDecimal amount) {
		if (amount == null)
			return null;
		String str = "select memberRank from MemberRank memberRank "+
							"where memberRank.isSpecial = false and "+
							" memberRank.amount <= :amount order by memberRank.amount desc";
		return (MemberRank) this.getEntityManager()
				.createQuery(str, MemberRank.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("amount", amount).setMaxResults(1)
				.getSingleResult();
	}

	@Override
	public Page<MemberRank> findPage(Pageable pageable) {
		String sqlString = "select memberRank from MemberRank memberRank where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(sqlString,  parameter, pageable);
	}

	@Override
	public List<MemberRank> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select memberRank from MemberRank memberRank where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}

	@Override
	public List<MemberRank> findAll() {
		return findList(null, null, null, null);
	}

	@Override
	public void persist(MemberRank memberRank) {
		if (memberRank.getIsDefault().booleanValue()) {
			String str = "update MemberRank memberRank set memberRank.isDefault = false where memberRank.isDefault = true";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT).executeUpdate();
		}
		System.out.println("get here.");
		super.persist(memberRank);
	}

	@Override
	public MemberRank merge(MemberRank memberRank) {
		if (memberRank.getIsDefault().booleanValue()) {
			String str = "update MemberRank memberRank set memberRank.isDefault = false "+
								"where memberRank.isDefault = true and memberRank != :memberRank";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("memberRank", memberRank).executeUpdate();
		}
		return (MemberRank) super.merge(memberRank);
	}

	@Override
	public void remove(MemberRank memberRank) {
		if ((memberRank != null) && (!memberRank.getIsDefault().booleanValue())) {
			String str = "select product from Product product join product.memberPrice memberPrice "+
								"where index(memberPrice) = :memberRank";
			List<Product> localList = this.getEntityManager()
					.createQuery(str, Product.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("memberRank", memberRank).getResultList();
			for (int i = 0; i < localList.size(); i++) {
				Product localProduct = (Product) localList.get(i);
				localProduct.getMemberPrice().remove(memberRank);
				if (i % 20 != 0)
					continue;
				super.flush();
				super.clear();
			}
			super.remove((MemberRank) super.merge(memberRank));
		}
	}
}