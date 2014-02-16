package com.hongqiang.shop.modules.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.common.config.Global;

@Entity
@Table(name = "hq_product_category")
public class ProductCategory extends OrderEntity {
	private static final long serialVersionUID = 1L;
	public static final String TREE_PATH_SEPARATOR = ",";// 树路径分隔符
	private static final String filePath = Global.getFrontPath()+"/product/list/";
	
	private String name;// 分类名称
	private String seoKeywords;// 页面关键词
	private String seoDescription;// 页面描述
	private String seoTitle;// 页面标题
	private String treePath;// 树路径
	private Integer grade;// 所属分类级别，如0表示顶级，1表示第一级别
	private ProductCategory parent;// 上级分类
	private Set<ProductCategory> children = new HashSet<ProductCategory>();// 下级分类
	private Set<Product> products = new HashSet<Product>();// 商品
	private Set<Brand> brands = new HashSet<Brand>();// 商品品牌
	private Set<ParameterGroup> parameterGroups = new HashSet<ParameterGroup>();
	private Set<Attribute> attributes = new HashSet<Attribute>();
	private Set<Promotion> promotions = new HashSet<Promotion>();//促销

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(max = 200)
	public String getSeoTitle() {
		return this.seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	@Length(max = 200)
	public String getSeoKeywords() {
		return this.seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	@Length(max = 200)
	public String getSeoDescription() {
		return this.seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Column(nullable = false)
	public String getTreePath() {
		return this.treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@Column(nullable = false)
	public Integer getGrade() {
		return this.grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public ProductCategory getParent() {
		return this.parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategory> getChildren() {
		return this.children;
	}

	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
	}

	@OneToMany(mappedBy="productCategory", fetch=FetchType.LAZY)
	  public Set<Product> getProducts()
	  {
	    return this.products;
	  }

	  public void setProducts(Set<Product> products)
	  {
	    this.products = products;
	  }

	  @ManyToMany(fetch=FetchType.LAZY)
	  @JoinTable(name="hq_product_category_brand")
	  @OrderBy("order asc")
	  public Set<Brand> getBrands()
	  {
	    return this.brands;
	  }

	  public void setBrands(Set<Brand> brands)
	  {
	    this.brands = brands;
	  }

	  @OneToMany(mappedBy="productCategory", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
	  @OrderBy("order asc")
	  public Set<ParameterGroup> getParameterGroups()
	  {
	    return this.parameterGroups;
	  }

	  public void setParameterGroups(Set<ParameterGroup> parameterGroups)
	  {
	    this.parameterGroups = parameterGroups;
	  }

	  @OneToMany(mappedBy="productCategory", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
	  @OrderBy("order asc")
	  public Set<Attribute> getAttributes()
	  {
	    return this.attributes;
	  }

	  public void setAttributes(Set<Attribute> attributes)
	  {
	    this.attributes = attributes;
	  }

	  @ManyToMany(mappedBy="productCategories", fetch=FetchType.LAZY)
	  public Set<Promotion> getPromotions()
	  {
	    return this.promotions;
	  }

	  public void setPromotions(Set<Promotion> promotions)
	  {
	    this.promotions = promotions;
	  }

	@Transient
	public List<Long> getTreePaths() {
		ArrayList<Long> localArrayList = new ArrayList<Long>();
		String[] arrayOfString = StringUtils.split(getTreePath(), ",");
		if (arrayOfString != null)
			for (String str : arrayOfString)
				localArrayList.add(Long.valueOf(str));
		return localArrayList;
	}

	@Transient
	public String getPath() {
		if (getId() != null)
			return filePath + getId() + fileSuffix;
		return null;
	}

	@PreRemove
	public void preRemove() {
		Set<Promotion> localSet = getPromotions();
	    if (localSet != null)
	    {
	      Iterator<Promotion> localIterator = localSet.iterator();
	      while (localIterator.hasNext())
	      {
	        Promotion localPromotion = (Promotion)localIterator.next();
	        localPromotion.getProductCategories().remove(this);
	      }
	    }
	}
}