package com.hongqiang.shop.common.utils;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Order
  implements Serializable
{
public enum Direction
{
  asc, desc;

  public static Direction fromString(String value)
  {
    return valueOf(value.toLowerCase());
  }
}

  private static final long serialVersionUID = -3078342809727773232L;
  private static final Direction DESCEND = Direction.desc;
  private static final Direction ASCEND = Direction.asc;
  private String property;
  private Direction direction = Direction.asc;

  public Order()
  {
  }

  public Order(String property, Direction direction)
  {
    this.property = property;
    this.direction = direction;
  }

  public static Order asc(String property)
  {
    return new Order(property, ASCEND);
  }

  public static Order desc(String property)
  {
    return new Order(property, DESCEND);
  }

  public String getProperty()
  {
    return this.property;
  }

  public void setProperty(String property)
  {
    this.property = property;
  }

  public Direction getDirection()
  {
    return this.direction;
  }

  public void setDirection(Direction direction)
  {
    this.direction = direction;
  }

  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    if (this == obj)
      return true;
    Order localOrder = (Order)obj;
    return new EqualsBuilder().append(getProperty(), localOrder.getProperty()).append(getDirection(), localOrder.getDirection()).isEquals();
  }

  public int hashCode()
  {
    return new HashCodeBuilder(17, 37).append(getProperty()).append(getDirection()).toHashCode();
  }
}