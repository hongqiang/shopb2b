package com.hongqiang.shop.modules.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ParameterGroup;
import com.hongqiang.shop.modules.product.dao.ParameterGroupDao;

@Service
public class ParameterGroupServiceImpl extends BaseService implements
		ParameterGroupService {
	@Autowired
	private ParameterGroupDao parameterGroupDao;

	@Transactional(readOnly = true)
	public ParameterGroup find(Long id) {
		return this.parameterGroupDao.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<ParameterGroup> findPage(Pageable pageable) {
		return this.parameterGroupDao.findPage(pageable);
	}

	@Transactional
	public void save(ParameterGroup parameterGroup) {
		this.parameterGroupDao.persist(parameterGroup);
	}

	@Transactional
	public ParameterGroup update(ParameterGroup parameterGroup) {
		Assert.notNull(parameterGroup);
		return (ParameterGroup) this.parameterGroupDao.merge(parameterGroup);
	}

	@Transactional
	public void delete(Long id) {
		ParameterGroup parameterGroup = this.parameterGroupDao.findById(id);
		this.parameterGroupDao.remove(parameterGroup);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.delete(localSerializable);
	}

	@Transactional
	public void delete(ParameterGroup parameterGroup) {
		this.parameterGroupDao.remove(parameterGroup);
	}
}