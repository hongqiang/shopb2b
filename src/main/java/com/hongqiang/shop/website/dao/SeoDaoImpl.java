package com.hongqiang.shop.website.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.Seo;

@Repository
public class SeoDaoImpl extends BaseDaoImpl<Seo,Long>
  implements SeoDaoCustom
{
	@Override
	public Page<Seo>  findPage(Pageable pageable){
		String qlString = "select seo from Seo seo where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable) ;
	}
}