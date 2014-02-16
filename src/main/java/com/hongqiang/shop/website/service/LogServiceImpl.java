package com.hongqiang.shop.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.dao.LogDao;
import com.hongqiang.shop.website.entity.Log;

@Service
public class LogServiceImpl extends BaseService implements LogService {

	@Autowired
	private LogDao logDao;

	public void clear() {
		this.logDao.removeAll();
	}

	@Transactional
	public Log find(Long id) {
		return this.logDao.find(id);
	}

	@Transactional
	public void save(Log log){
		this.logDao.save(log);
	}
	
	@Transactional
	public Page<Log> findPage(Pageable pageable) {
		return this.logDao.findPage(pageable);
	}

	@Transactional
	public void delete(Long id) {
		this.logDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.logDao.delete(id);
	}

	@Transactional
	public void delete(Log log) {
		this.logDao.delete(log);
	}
}