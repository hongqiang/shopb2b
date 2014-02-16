package com.hongqiang.shop.modules.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Deposit;
import com.hongqiang.shop.modules.entity.Member;

public  interface DepositDao extends DepositDaoCustom, CrudRepository<Deposit, Long>{

}
/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface DepositDaoCustom extends BaseDao<Deposit,Long>{
  public  Page<Deposit> findPage(Member paramMember, Pageable paramPageable);
  
  public Page<Deposit> findPage(Pageable pageable);
}