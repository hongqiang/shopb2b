package com.hongqiang.shop.modules.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class SafeKey
  implements Serializable
{
  private static final long serialVersionUID = -8536541568286987548L;
  private String value;
  private Date expire;

  @Column(name="safe_key_value")
  public String getValue()
  {
    return this.value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  @Column(name="safe_key_expire")
  public Date getExpire()
  {
    return this.expire;
  }

  public void setExpire(Date expire)
  {
    this.expire = expire;
  }

  @Transient
  public boolean hasExpired()
  {
    return (getExpire() != null) && (new Date().after(getExpire()));
  }
}