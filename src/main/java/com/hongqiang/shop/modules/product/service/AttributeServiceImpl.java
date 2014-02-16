package com.hongqiang.shop.modules.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.product.dao.AttributeDao;
import com.hongqiang.shop.modules.product.dao.ProductDao;

@Service
public class AttributeServiceImpl extends BaseService implements
		AttributeService {

	@Autowired
	private AttributeDao attributeDao;

	@Autowired
	private ProductDao productDao;

	@Transactional
	public Attribute find(Long id) {
		return this.attributeDao.findById(id);
	}

	@Transactional
	public Page<Attribute> findPage(Pageable pageable) {
		return this.attributeDao.findPage(pageable);
	}

	@Transactional
	public void save(Attribute attribute) {

		this.attributeDao.persist(attribute);
	}

	@Transactional
	public Attribute update(Attribute attribute) {
		if (attribute != null) {
			this.productDao.updateAttributeOfProduct(attribute);
		}
		return (Attribute) this.attributeDao.merge(attribute);
	}

	@Transactional
	public Attribute update(Attribute attribute, String[] ignoreProperties) {
		return (Attribute) this.attributeDao
				.update(attribute, ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {
		Attribute attribute = this.attributeDao.findById(id);
		if (attribute != null) {
			this.productDao.deleteAttributeOfProduct(attribute);
		}
		this.attributeDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.delete(localSerializable);
	}

	@Transactional
	public void delete(Attribute attribute) {
		if (attribute != null) {
			this.productDao.deleteAttributeOfProduct(attribute);
		}
		this.attributeDao.remove(attribute);
	}
}