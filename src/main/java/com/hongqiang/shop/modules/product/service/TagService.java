package com.hongqiang.shop.modules.product.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Tag;

public abstract interface TagService {
	
	public Tag find(Long id); 

	public Page<Tag> findPage(Pageable pageable);

	public  List<Tag> findList(Tag.Type paramType);

	public  List<Tag> findList(Long[] ids);
	
	 public  List<Tag> findList(Integer paramInteger, List<Filter>
	 paramList, List<Order> paramList1, String paramString);

	 public  List<Tag> findList(Integer paramInteger, List<Filter>
	 paramList, List<Order> paramList1);
	 
	public void save(Tag tag);

	public Tag update(Tag tag);

	 public Tag update(Tag brand, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Tag tag);
}