package com.hongqiang.shop.modules.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Embeddable
public class ProductImage
  implements Serializable, Comparable<ProductImage>
{
  private static final long serialVersionUID = -673883300094536107L;
  private String title;
  private String source;
  private String large;
  private String medium;
  private String thumbnail;
  private Integer order;
  private MultipartFile file;

  @Length(max=200)
  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getSource()
  {
    return this.source;
  }

  public void setSource(String source)
  {
    this.source = source;
  }

  public String getLarge()
  {
    return this.large;
  }

  public void setLarge(String large)
  {
    this.large = large;
  }

  public String getMedium()
  {
    return this.medium;
  }

  public void setMedium(String medium)
  {
    this.medium = medium;
  }

  public String getThumbnail()
  {
    return this.thumbnail;
  }

  public void setThumbnail(String thumbnail)
  {
    this.thumbnail = thumbnail;
  }

  @Min(0L)
  @Column(name="orders")
  public Integer getOrder()
  {
    return this.order;
  }

  public void setOrder(Integer order)
  {
    this.order = order;
  }

  @Transient
  public MultipartFile getFile()
  {
    return this.file;
  }

  public void setFile(MultipartFile file)
  {
    this.file = file;
  }

  @Transient
  public boolean isEmpty()
  {
    return ((getFile() == null) || (getFile().isEmpty())) && ((StringUtils.isEmpty(getSource())) || (StringUtils.isEmpty(getLarge())) || (StringUtils.isEmpty(getMedium())) || (StringUtils.isEmpty(getThumbnail())));
  }

  public int compareTo(ProductImage productImage)
  {
    return new CompareToBuilder().append(getOrder(), productImage.getOrder()).toComparison();
  }
}