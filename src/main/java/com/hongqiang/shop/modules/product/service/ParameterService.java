package com.hongqiang.shop.modules.product.service;

import java.util.List;
import java.util.Set;

import com.hongqiang.shop.modules.entity.Parameter;
import com.hongqiang.shop.modules.entity.ParameterGroup;

public abstract interface ParameterService{
	
	public abstract List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes);
}