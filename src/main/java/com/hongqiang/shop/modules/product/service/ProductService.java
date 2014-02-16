package com.hongqiang.shop.modules.product.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.entity.Tag;

public abstract interface ProductService {
	// 判断sn是否存在
	public boolean snExists(String paramString);

	// 通过sn找到商品
	public Product findBySn(String paramString);

	public Product find(Long id);

	// 判断两个sn是否一样
	public boolean snUnique(String paramString1, String paramString2);

	// 检索商品
	public List<Product> search(String paramString, Boolean paramBoolean,
			Integer paramInteger);

	public List<Product> findList(Long[] ids);

	// 找到商品列表
	public List<Product> findList(ProductCategory paramProductCategory,
			Brand paramBrand, Promotion paramPromotion, List<Tag> paramList,
			Map<Attribute, String> paramMap, BigDecimal paramBigDecimal1,
			BigDecimal paramBigDecimal2, Boolean paramBoolean1,
			Boolean paramBoolean2, Boolean paramBoolean3,
			Boolean paramBoolean4, Boolean paramBoolean5,
			Boolean paramBoolean6, Product.OrderType paramOrderType,
			Integer paramInteger, List<Filter> paramList1,
			List<Order> paramList2);

	public List<Product> findList(ProductCategory paramProductCategory,
			Brand paramBrand, Promotion paramPromotion, List<Tag> paramList,
			Map<Attribute, String> paramMap, BigDecimal paramBigDecimal1,
			BigDecimal paramBigDecimal2, Boolean paramBoolean1,
			Boolean paramBoolean2, Boolean paramBoolean3,
			Boolean paramBoolean4, Boolean paramBoolean5,
			Boolean paramBoolean6, Product.OrderType paramOrderType,
			Integer paramInteger, List<Filter> paramList1,
			List<Order> paramList2, String paramString);

	public List<Product> findList(ProductCategory paramProductCategory,
			Date paramDate1, Date paramDate2, Integer paramInteger1,
			Integer paramInteger2);

	public Page<Product> findPage(ProductCategory paramProductCategory,
			Brand paramBrand, Promotion paramPromotion, List<Tag> paramList,
			Map<Attribute, String> paramMap, BigDecimal paramBigDecimal1,
			BigDecimal paramBigDecimal2, Boolean paramBoolean1,
			Boolean paramBoolean2, Boolean paramBoolean3,
			Boolean paramBoolean4, Boolean paramBoolean5,
			Boolean paramBoolean6, Product.OrderType paramOrderType,
			Pageable paramPageable);

	public Page<Product> findPage(Member paramMember, Pageable paramPageable);

	public Page<Object> findSalesPage(Date paramDate1, Date paramDate2,
			Pageable paramPageable);

	public Long count(Member paramMember, Boolean paramBoolean1,
			Boolean paramBoolean2, Boolean paramBoolean3,
			Boolean paramBoolean4, Boolean paramBoolean5, Boolean paramBoolean6);

	public boolean isPurchased(Member paramMember, Product paramProduct);

	public long viewHits(Long id);

	public void save(Product brand);

	public Product update(Product product);

	public Product update(Product brand, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Product product);
	
	//删除商品的静态文件
	public int deleteStaticProduct(Product product);
	//生成商品的静态文件
	public int build(Product product);
}