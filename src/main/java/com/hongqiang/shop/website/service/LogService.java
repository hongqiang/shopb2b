package com.hongqiang.shop.website.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.Log;


public interface LogService {
	public void clear();
	
	public Log find(Long id);
	
	public Page<Log> findPage(Pageable pageable);
	
	public void save(Log log);
	
	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Log log);
}