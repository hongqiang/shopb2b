package com.hongqiang.shop.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Pageable
  implements Serializable
{
  private static final long serialVersionUID = -3930180379790344299L;
  private static final int minPageSize = 1;
  private static final int currentPageSize = 20;
  private static final int maxPageSize = 1000;
  private int pageNumber = 1;
  private int pageSize = 20;
  private String searchProperty;
  private String searchValue;
  private String orderProperty;
  private Order.Direction orderDirection;
  private List<Filter> filters = new ArrayList<Filter>();
  private List<Order> orders = new ArrayList<Order>();

  public Pageable()
  {
  }

  public Pageable(Integer pageNumber, Integer pageSize)
  {
    if ((pageNumber != null) && (pageNumber.intValue() >= pageNumber))
      this.pageNumber = pageNumber.intValue();
    if ((pageSize != null) && (pageSize.intValue() >= minPageSize) && (pageSize.intValue() <= maxPageSize))
      this.pageSize = pageSize.intValue();
  }

  public int getPageNumber()
  {
    return this.pageNumber;
  }

  public void setPageNumber(int pageNumber)
  {
    if (pageNumber < 1)
      pageNumber = 1;
    this.pageNumber = pageNumber;
  }

  public int getPageSize()
  {
    return this.pageSize;
  }

  public void setPageSize(int pageSize)
  {
    if ((pageSize < minPageSize) || (pageSize > maxPageSize))
      pageSize = currentPageSize;
    this.pageSize = pageSize;
  }

  public String getSearchProperty()
  {
    return this.searchProperty;
  }

  public void setSearchProperty(String searchProperty)
  {
    this.searchProperty = searchProperty;
  }

  public String getSearchValue()
  {
    return this.searchValue;
  }

  public void setSearchValue(String searchValue)
  {
    this.searchValue = searchValue;
  }

  public String getOrderProperty()
  {
    return this.orderProperty;
  }

  public void setOrderProperty(String orderProperty)
  {
    this.orderProperty = orderProperty;
  }

  public Order.Direction getOrderDirection()
  {
    return this.orderDirection;
  }

  public void setOrderDirection(Order.Direction orderDirection)
  {
    this.orderDirection = orderDirection;
  }

  public List<Filter> getFilters()
  {
    return this.filters;
  }

  public void setFilters(List<Filter> filters)
  {
    this.filters = filters;
  }

  public List<Order> getOrders()
  {
    return this.orders;
  }

  public void setOrders(List<Order> orders)
  {
    this.orders = orders;
  }

  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    if (this == obj)
      return true;
    Pageable localPageable = (Pageable)obj;
    return new EqualsBuilder().append(getPageNumber(), localPageable.getPageNumber()).append(getPageSize(), localPageable.getPageSize()).append(getSearchProperty(), localPageable.getSearchProperty()).append(getSearchValue(), localPageable.getSearchValue()).append(getOrderProperty(), localPageable.getOrderProperty()).append(getOrderDirection(), localPageable.getOrderDirection()).append(getFilters(), localPageable.getFilters()).append(getOrders(), localPageable.getOrders()).isEquals();
  }

  public int hashCode()
  {
    return new HashCodeBuilder(17, 37).append(getPageNumber()).append(getPageSize()).append(getSearchProperty()).append(getSearchValue()).append(getOrderProperty()).append(getOrderDirection()).append(getFilters()).append(getOrders()).toHashCode();
  }
}