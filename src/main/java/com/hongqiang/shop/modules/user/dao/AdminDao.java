package com.hongqiang.shop.modules.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Admin;

public  interface AdminDao extends AdminDaoCustom, CrudRepository<Admin, Long>{
	public Admin findById(Long id);
	
	public  Admin findByUsername(String userName);
	
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface AdminDaoCustom extends BaseDao<Admin,Long> {
  public  boolean usernameExists(String paramString);

  
  public Page<Admin> findPage(Pageable pageable);
}