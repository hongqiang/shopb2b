package com.hongqiang.shop.website.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.hongqiang.shop.modules.entity.BaseEntity;

@Entity
@Table(name="hq_log")
public class Log extends BaseEntity
{
  private static final long serialVersionUID = -4494144902110236826L;
  public static final String LOG_CONTENT_ATTRIBUTE_NAME = Log.class.getName() + ".CONTENT";
  private String operation;
  private String operator;
  private String content;
  private String parameter;
  private String ip;

  @Column(nullable=false, updatable=false)
  public String getOperation()
  {
    return this.operation;
  }

  public void setOperation(String operation)
  {
    this.operation = operation;
  }

  @Column(updatable=false)
  public String getOperator()
  {
    return this.operator;
  }

  public void setOperator(String operator)
  {
    this.operator = operator;
  }

  @Column(length=3000, updatable=false)
  public String getContent()
  {
    return this.content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  @Lob
  @Column(updatable=false)
  public String getParameter()
  {
    return this.parameter;
  }

  public void setParameter(String parameter)
  {
    this.parameter = parameter;
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
}