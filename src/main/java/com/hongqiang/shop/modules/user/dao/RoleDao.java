package com.hongqiang.shop.modules.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Role;

public abstract interface RoleDao extends RoleDaoCustom,
		CrudRepository<Role, Long> {

	public Role findById(Long id);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface RoleDaoCustom extends BaseDao<Role,Long> {

	public Page<Role> findPage(Pageable pageable);

	public List<Role> findList();

}