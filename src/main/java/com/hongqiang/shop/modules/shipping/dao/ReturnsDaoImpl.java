package com.hongqiang.shop.modules.shipping.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Returns;

@Repository
public class ReturnsDaoImpl extends BaseDaoImpl<Returns,Long>
  implements ReturnsDaoCustom{
  
	@Override
	public Page<Returns>  findPage(Pageable pageable){
		String qlString = "select returns from Returns returns where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable);
	}
}