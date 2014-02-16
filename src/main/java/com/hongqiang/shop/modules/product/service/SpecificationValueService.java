package com.hongqiang.shop.modules.product.service;

import java.util.List;

import com.hongqiang.shop.modules.entity.SpecificationValue;

public abstract interface SpecificationValueService {
	public SpecificationValue find(Long id);

	 public List<SpecificationValue> findAll();
	
	public void save(SpecificationValue specificationValue);

	public SpecificationValue update(SpecificationValue specificationValue);

	 public SpecificationValue update(SpecificationValue brand, String[]
	 ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(SpecificationValue specificationValue);
}
