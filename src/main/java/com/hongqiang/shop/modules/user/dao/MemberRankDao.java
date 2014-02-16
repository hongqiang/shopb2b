package com.hongqiang.shop.modules.user.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.MemberRank;

public interface MemberRankDao extends MemberRankDaoCustom,
		CrudRepository<MemberRank, Long> {
	public MemberRank findById(Long id);

	public MemberRank findByAmount(BigDecimal amount);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface MemberRankDaoCustom extends BaseDao<MemberRank,Long> {
	public boolean nameExists(String paramString);

	public boolean amountExists(BigDecimal paramBigDecimal);

	public MemberRank findDefault();

	public MemberRank findByAmount(BigDecimal amount);

	public Page<MemberRank> findPage(Pageable pageable);

	public void persist(MemberRank memberRank);

	public MemberRank merge(MemberRank memberRank);

	public void remove(MemberRank memberRank);

	public List<MemberRank> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<MemberRank> findAll();
}
