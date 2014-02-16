package com.hongqiang.shop.modules.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
//消息
@Entity
@Table(name="hq_message")
public class Message extends BaseEntity
{
  private static final long serialVersionUID = -5035343536762850722L;
  private String title;// 消息标题
  private String content;// 消息内容
  private String ip;//ip地址
  private Boolean isDraft;//是否草稿
  private Boolean senderRead;//发送者是否标记已读
  private Boolean receiverRead;//收件人是否已读
  private Boolean senderDelete;//发送者是否删除
  private Boolean receiverDelete;//收件人是否删除
  private Member sender;// 消息发出会员,为null时表示管理员
  private Member receiver;// 消息接收会员,为null时表示管理员
  private Message forMessage;//发送的消息
  private Set<Message> replyMessages = new HashSet<Message>();//回复的消息

  @Column(nullable=false, updatable=false)
  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  @NotEmpty
  @Length(max=1000)
  @Column(nullable=false, updatable=false, length=1000)
  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  @Column(nullable=false, updatable=false)
  public String getIp()
  {
    return this.ip;
  }

  public void setIp(String ip)
  {
    this.ip = ip;
  }

  @Column(nullable=false, updatable=false)
  public Boolean getIsDraft()
  {
    return this.isDraft;
  }

  public void setIsDraft(Boolean isDraft)
  {
    this.isDraft = isDraft;
  }

  @Column(nullable=false)
  public Boolean getSenderRead()
  {
    return this.senderRead;
  }

  public void setSenderRead(Boolean senderRead)
  {
    this.senderRead = senderRead;
  }

  @Column(nullable=false)
  public Boolean getReceiverRead()
  {
    return this.receiverRead;
  }

  public void setReceiverRead(Boolean receiverRead)
  {
    this.receiverRead = receiverRead;
  }

  @Column(nullable=false)
  public Boolean getSenderDelete()
  {
    return this.senderDelete;
  }

  public void setSenderDelete(Boolean senderDelete)
  {
    this.senderDelete = senderDelete;
  }

  @Column(nullable=false)
  public Boolean getReceiverDelete()
  {
    return this.receiverDelete;
  }

  public void setReceiverDelete(Boolean receiverDelete)
  {
    this.receiverDelete = receiverDelete;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(updatable=false)
  public Member getSender()
  {
    return this.sender;
  }

  public void setSender(Member sender)
  {
    this.sender = sender;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(updatable=false)
  public Member getReceiver()
  {
    return this.receiver;
  }

  public void setReceiver(Member receiver)
  {
    this.receiver = receiver;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(updatable=false)
  public Message getForMessage()
  {
    return this.forMessage;
  }

  public void setForMessage(Message forMessage)
  {
    this.forMessage = forMessage;
  }

  @OneToMany(mappedBy="forMessage", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
  @OrderBy("createDate asc")
  public Set<Message> getReplyMessages()
  {
    return this.replyMessages;
  }

  public void setReplyMessages(Set<Message> replyMessages)
  {
    this.replyMessages = replyMessages;
  }
}