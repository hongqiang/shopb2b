package com.hongqiang.shop.modules.product.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.modules.entity.Parameter;
import com.hongqiang.shop.modules.entity.ParameterGroup;
import com.hongqiang.shop.modules.product.dao.ParameterDao;

@Service
public class ParameterServiceImpl extends BaseService
  implements ParameterService{

	@Autowired
	private ParameterDao parameterDao;
	
	 @Transactional(readOnly=true)
	public  List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes){
		 return this.parameterDao.findList(parameterGroup, excludes);
	 }
	
}