package com.hongqiang.shop.modules.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.MemberAttribute;

public interface MemberAttributeDao extends MemberAttributeDaoCustom,
		CrudRepository<MemberAttribute, Long> {
	public MemberAttribute findById(Long id);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface MemberAttributeDaoCustom extends BaseDao<MemberAttribute,Long> {
	public Integer findUnusedPropertyIndex();

	public Page<MemberAttribute> findPage(Pageable pageable);

	public List<MemberAttribute> findList();
	
	public List<MemberAttribute> findAll();
	
	public List<MemberAttribute> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public void remove(MemberAttribute memberAttribute);
}