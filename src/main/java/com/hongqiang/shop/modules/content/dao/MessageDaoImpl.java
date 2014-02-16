package com.hongqiang.shop.modules.content.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Message;

@Repository
public class MessageDaoImpl extends BaseDaoImpl<Message,Long>
  implements MessageDaoCustom
{
  public Page<Message> findPage(Member member, Pageable pageable)
  {
	String sqlString = "select message from Message message where message.forMessage is null and message.isDraft = false ";
	List<Object> params = new ArrayList<Object>();
    if (member != null){
		sqlString +=" and ((message.sender = ? and message.senderDelete = false) or "+
							"(message.receiver = ? and message.receiverDelete = false)) ";
		params.add(member);
		params.add(member);
	}
    else{
		sqlString +=" and ((message.sender is null and message.senderDelete = false) "+
							"or (message.receiver is null and message.receiverDelete = false)) ";
	}
	return super.findPage(sqlString,  params, pageable);
  }

  public Page<Message> findDraftPage(Member sender, Pageable pageable)
  {
	String sqlString = "select message from Message message where message.forMessage is null and message.isDraft = true ";
	List<Object> params = new ArrayList<Object>();
    if (sender != null){
		sqlString +=" and message.sender = ? ";
		params.add(sender);
	}
    else{
		sqlString +=" and message.sender is null ";
	}
	return super.findPage(sqlString,  params, pageable);
  }

  public Long count(Member member, Boolean read)
  {
	String sqlString = "select message from Message message where message.forMessage is null and message.isDraft = false ";
	List<Object> params = new ArrayList<Object>();
    if (member != null)
    {
      if (read != null){
		sqlString +=" and ((message.sender = ? and message.senderDelete = false and message.senderRead = ? ) "+
							"or (message.receiver = ? and message.receiverDelete = false and message.receiverRead = ?)) ";
		params.add(member);
		params.add(read);
		params.add(member);
		params.add(read);
	  }
      else{
		sqlString +=" and ((message.sender = ? and message.senderDelete = false) or (message.receiver = ? and "+
							"message.receiverDelete = false)) ";
		params.add(member);
		params.add(member);
	  }
    }
    else if (read != null){
		sqlString +=" and ((message.sender is null and message.senderDelete = false and message.senderRead = ? ) "+
							"or (message.receiver is null and message.receiverDelete = false and message.receiverRead = ?)) ";
		params.add(read);
		params.add(read);
	}
    else{
		sqlString +=" and ((message.sender is null and message.senderDelete = false) or (message.receiver is null and "+
							"message.receiverDelete = false)) ";
	}
	StringBuilder stringBuilder = new StringBuilder(sqlString);
    return super.count(stringBuilder, null, params);
  }

  public void remove(Long id, Member member)
  {
    Message localMessage = (Message)super.find(id);
    if ((localMessage == null) || (localMessage.getForMessage() != null))
      return;
    if (member == localMessage.getReceiver())
    {
      if (!localMessage.getIsDraft().booleanValue())
        if (localMessage.getSenderDelete().booleanValue())
        {
          super.remove(localMessage);
        }
        else
        {
          localMessage.setReceiverDelete(Boolean.valueOf(true));
          super.merge(localMessage);
        }
    }
    else if (member == localMessage.getSender())
      if (localMessage.getIsDraft().booleanValue())
      {
        super.remove(localMessage);
      }
      else if (localMessage.getReceiverDelete().booleanValue())
      {
        super.remove(localMessage);
      }
      else
      {
        localMessage.setSenderDelete(Boolean.valueOf(true));
        super.merge(localMessage);
      }
  }
}