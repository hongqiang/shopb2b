package com.hongqiang.shop.modules.content.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.content.dao.MessageDao;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Message;

@Service
public class MessageServiceImpl extends BaseService
  implements MessageService
{

  @Autowired
  private MessageDao messageDao;

  @Transactional(readOnly=true)
  public Page<Message> findPage(Member member, Pageable pageable)
  {
    return this.messageDao.findPage(member, pageable);
  }

  @Transactional(readOnly=true)
  public Page<Message> findDraftPage(Member sender, Pageable pageable)
  {
    return this.messageDao.findDraftPage(sender, pageable);
  }

  @Transactional(readOnly=true)
  public Long count(Member member, Boolean read)
  {
    return this.messageDao.count(member, read);
  }

  public void delete(Long id, Member member)
  {
    this.messageDao.remove(id, member);
  }
  
    @Transactional(readOnly=true)
	public Message find(Long id) {
		return this.messageDao.find(id);
	}
	
	@Transactional
  public void save(Message message)
  {
	this.messageDao.persist(message);
  }

  @Transactional
  public Message update(Message message)
  {
    return (Message)this.messageDao.merge(message);
  }

  @Transactional
  public Message update(Message message, String[] ignoreProperties)
  {
    return (Message)this.messageDao.update(message, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.messageDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.messageDao.delete(localSerializable);
  }

  @Transactional
  public void delete(Message message)
  {
    this.messageDao.delete(message);
  }
}