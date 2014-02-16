package com.hongqiang.shop.modules.content.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Message;

public interface MessageService {
	public Page<Message> findPage(Member paramMember, Pageable paramPageable);

	public Page<Message> findDraftPage(Member paramMember,
			Pageable paramPageable);

	public Long count(Member paramMember, Boolean paramBoolean);

	public void delete(Long paramLong, Member paramMember);

	public Message find(Long id);

	public void save(Message message);

	public Message update(Message message);

	public Message update(Message message, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Message message);
}