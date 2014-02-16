package com.hongqiang.shop.website.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.Ad;

@Repository
public class AdDaoImpl extends BaseDaoImpl<Ad,Long>
  implements AdDaoCustom{
  
  @Override
  public Page<Ad>  findPage(Pageable pageable){
	String qlString = "select ad from Ad ad where 1=1 ";
	List<Object> parameter = new ArrayList<Object>();
	return super.findPage(qlString,  parameter, pageable);
  }
}