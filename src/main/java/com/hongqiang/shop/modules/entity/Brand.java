package com.hongqiang.shop.modules.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hongqiang.shop.common.config.Global;

@Entity
@Table(name = "hq_brand")
public class Brand extends OrderEntity {
	private static final long serialVersionUID = -6109590619136943215L;
	private static final String filePath =  Global.getFrontPath()+"/brand/content/";
	private String name;// 商标名称
	private Type type;// 商标类型
	private String logo;// Logo
	private String url;// 网址
	private String introduction;// 介绍
	private Set<Product> products = new HashSet<Product>();// 商品
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();// 商品类别
	private Set<Promotion> promotions = new HashSet<Promotion>();// 促销

	public enum Type {
		text, image;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable = false)
	public Brand.Type getType() {
		return this.type;
	}

	public void setType(Brand.Type type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Length(max = 200)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Lob
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategory> getProductCategories() {
		return this.productCategories;
	}

	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@Transient
	public String getPath() {
		if (getId() != null)
			return filePath + getId() + fileSuffix;
		return null;
	}

	@PreRemove
	public void preRemove() {
		Set<Product> localSet = getProducts();
		if (localSet != null) {
			Iterator<Product> localObject2 = localSet.iterator();
			while (localObject2.hasNext()) {
				Product localObject1 = (Product) (localObject2).next();
				localObject1.setBrand(null);
			}
		}
		Set<ProductCategory> localObject1 = getProductCategories();
		if (localObject1 != null) {
			Iterator<ProductCategory> localObject3 = localObject1.iterator();
			while (localObject3.hasNext()) {
				ProductCategory localObject2 = (ProductCategory) (localObject3)
						.next();
				localObject2.getBrands().remove(this);
			}
		}
		Set<Promotion> localObject2 = getPromotions();
		if (localObject2 != null) {
			Iterator<Promotion> localIterator = localObject2.iterator();
			while (localIterator.hasNext()) {
				Promotion localObject3 = (Promotion) localIterator.next();
				localObject3.getBrands().remove(this);
			}
		}
	}
}