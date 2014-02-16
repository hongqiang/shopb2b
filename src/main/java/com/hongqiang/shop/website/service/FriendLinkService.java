package com.hongqiang.shop.website.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.FriendLink;

public interface FriendLinkService {
	public List<FriendLink> findList(FriendLink.Type paramType);

	public List<FriendLink> findList(Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1, String paramString);

	public List<FriendLink> findList(Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1);
	
	public FriendLink find(Long id);

	public Page<FriendLink> findPage(Pageable pageable);

	public void save(FriendLink friendLink);

	public FriendLink update(FriendLink friendLink);

	public FriendLink update(FriendLink friendLink, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(FriendLink friendLink);
}