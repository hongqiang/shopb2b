package com.hongqiang.shop.modules.content.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Review;

@Repository
public class ReviewDaoImpl extends BaseDaoImpl<Review,Long> implements
		ReviewDaoCustom {
	@Override
	public List<Review> findList(Member member, Product product,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders) {
		List<Object> parameter = new ArrayList<Object>();
		String qlString = composeSql(parameter, member, product, type, isShow);
		return super
				.findList(qlString, parameter, null, count, filters, orders);
	}

	@Override
	public Page<Review> findPage(Member member, Product product,
			Review.Type type, Boolean isShow, Pageable pageable) {
		List<Object> parameter = new ArrayList<Object>();
		String qlString = composeSql(parameter, member, product, type, isShow);
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public Long count(Member member, Product product, Review.Type type,
			Boolean isShow) {
		List<Object> parameter = new ArrayList<Object>();
		String qlString = composeSql(parameter, member, product, type, isShow);
		StringBuilder stringBuilder = new StringBuilder(qlString);
		return super.count(stringBuilder, null, parameter);
	}

	@Override
	public boolean isReviewed(Member member, Product product) {
		if ((member == null) || (product == null))
			return false;
		String str = "select count(*) from Review review where review.member = :member and review.product = :product";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("member", member)
				.setParameter("product", product).getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public long calculateTotalScore(Product product) {
		if (product == null)
			return 0L;
		String str = "select sum(review.score) from Review review where review.product = :product"
				+ " and review.isShow = :isShow";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("product", product)
				.setParameter("isShow", Boolean.valueOf(true))
				.getSingleResult();
		return localLong != null ? localLong.longValue() : 0L;
	}

	@Override
	public long calculateScoreCount(Product product) {
		if (product == null)
			return 0L;
		String str = "select count(*) from Review review where review.product = :product and review.isShow = :isShow";
		return ((Long) this.getEntityManager().createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("product", product)
				.setParameter("isShow", Boolean.valueOf(true))
				.getSingleResult()).longValue();
	}

	private String composeSql(List<Object> parameter, Member member,
			Product product, Review.Type type, Boolean isShow) {
		String qlString = "select review from Review review where 1=1 ";
		if (member != null) {
			qlString += " and review.member = ?";
			parameter.add(member);
		}
		if (product != null) {
			qlString += " and review.product = ?";
			parameter.add(product);
		}
		if (type == Review.Type.positive) {
			qlString += " and review.score >= ?";
			parameter.add(Integer.valueOf(4));
		} else if (type == Review.Type.moderate) {
			qlString += " and review.score = ?";
			parameter.add(Integer.valueOf(3));
		} else if (type == Review.Type.negative) {
			qlString += " and review.score <= ?";
			parameter.add(Integer.valueOf(2));
		}
		if (isShow != null) {
			qlString += " and review.isShow = ?";
			parameter.add(isShow);
		}
//		qlString += " order by review.member asc";
		return qlString;
	}
}