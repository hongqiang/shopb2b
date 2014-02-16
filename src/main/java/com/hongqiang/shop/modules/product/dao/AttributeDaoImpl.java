package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Attribute;

@Repository
public class AttributeDaoImpl extends BaseDaoImpl<Attribute, Long> implements
		AttributeDaoCustom {

	@Override
	public Page<Attribute> findPage(Pageable pageable) {
		String qlString = "select attribute from Attribute attribute where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable) ;
	}
}