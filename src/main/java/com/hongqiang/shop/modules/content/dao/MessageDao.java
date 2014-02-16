package com.hongqiang.shop.modules.content.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Message;

public interface MessageDao extends MessageDaoCustom, CrudRepository<Message, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface MessageDaoCustom extends BaseDao<Message,Long> {
  public Page<Message> findPage(Member paramMember, Pageable paramPageable);

  public Page<Message> findDraftPage(Member paramMember, Pageable paramPageable);

  public Long count(Member paramMember, Boolean paramBoolean);

  public void remove(Long paramLong, Member paramMember);
}