package com.hongqiang.shop.modules.user.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.user.dao.MemberRankDao;

@Service
public class MemberRankServiceImpl extends BaseService implements
		MemberRankService {

	@Autowired
	private MemberRankDao memberRankDao;

	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return this.memberRankDao.nameExists(name);
	}

	@Transactional(readOnly = true)
	public boolean nameUnique(String previousName, String currentName) {
		if (StringUtils.equalsIgnoreCase(previousName, currentName))
			return true;
		return !this.memberRankDao.nameExists(currentName);
	}

	@Transactional(readOnly = true)
	public boolean amountExists(BigDecimal amount) {
		return this.memberRankDao.amountExists(amount);
	}

	@Transactional(readOnly = true)
	public boolean amountUnique(BigDecimal previousAmount,
			BigDecimal currentAmount) {
		if ((previousAmount != null)
				&& (previousAmount.compareTo(currentAmount) == 0))
			return true;
		return !this.memberRankDao.amountExists(currentAmount);
	}

	@Transactional(readOnly = true)
	public MemberRank findDefault() {
		return this.memberRankDao.findDefault();
	}

	@Transactional(readOnly = true)
	public MemberRank findByAmount(BigDecimal amount) {
		return this.memberRankDao.findByAmount(amount);
	}

	@Transactional(readOnly = true)
	public MemberRank find(Long id) {
		return this.memberRankDao.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<MemberRank> findPage(Pageable pageable) {
		return this.memberRankDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	public List<MemberRank> findList(Integer count, List<Filter> filters,
			List<Order> orders) {
		return this.memberRankDao.findList(null, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<MemberRank> findList(Long[] ids) {
		List<MemberRank> localArrayList = new ArrayList<MemberRank>();
		if (ids != null)
			for (Long id : ids) {
				MemberRank localObject = find(id);
				if (localObject == null)
					continue;
				localArrayList.add(localObject);
			}
		return localArrayList;
	}

	@Transactional(readOnly = true)
	public List<MemberRank> findAll() {
		return this.memberRankDao.findAll();
	}

	@Transactional(readOnly = true)
	public void save(MemberRank memberRank) {
		this.memberRankDao.persist(memberRank);
	}

	@Transactional(readOnly = true)
	public void delete(Long id) {
		MemberRank memberRank = find(id);
		this.memberRankDao.remove(memberRank);
	}

	@Transactional(readOnly = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.memberRankDao.delete(localSerializable);
	}

	public void delete(MemberRank memberRank) {
		this.memberRankDao.remove(memberRank);
	}

	@Transactional(readOnly = true)
	public MemberRank update(MemberRank memberRank) {
		return (MemberRank) this.memberRankDao.merge(memberRank);
	}

	@Transactional
	public MemberRank update(MemberRank memberRank, String[] ignoreProperties) {
		return (MemberRank) this.memberRankDao.update(memberRank,
				ignoreProperties);
	}

	@Transactional(readOnly = true)
	public long count() {
		return this.memberRankDao.count();
	}

}