package com.hongqiang.shop.modules.product.service;

import java.util.List;

import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.modules.entity.ProductCategory;

public  interface ProductCategoryService  
{
	/**
	 * 获取顶级分类下的所有商品集合（只包含isMarketable=true的对象）
	 * 
	 * @param 
	 * 
	 * @return 顶级分类下的所有商品集合
	 */
  public  List<ProductCategory> findRoots();
	/**
	根据设定的结果数，获取顶级分类下的所有商品集合（只包含isMarketable=true的对象）
	 * 
	 * @param paramInteger 设定的结果数
	 * 
	 * @return 顶级分类下的所有商品集合
	 */
  public  List<ProductCategory> findRoots(Integer paramInteger);
	/**
	 *  
	根据最大结果数，获取顶级分类下的所有商品集合（只包含isMarketable=true的对象）
	 * 
	 * @param paramInteger 设定的结果数
	 * @param paramString 
	 * 
	 * @return 顶级分类下的所有商品集合
	 */
  public  List<ProductCategory> findRoots(Integer paramInteger, String paramString);
	/**
	 * 根据ProductCategory对象，获取父级分类下的所有商品（只包含isMarketable=true的对象）
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @return 父级分类下的所有商品集合
	 */
  public  List<ProductCategory> findParents(ProductCategory paramProductCategory);

  public  List<ProductCategory> findParents(ProductCategory paramProductCategory, Integer paramInteger);

  public  List<ProductCategory> findParents(ProductCategory paramProductCategory, Integer paramInteger, String paramString);

  public  List<ProductCategory> findTree();
	/**
	 * 根据ProductCategory对象，获取子级分类下的所有商品（只包含isMarketable=true的对象）
	 * 
	 * @param productCategory
	 *            商品分类
	 * 
	 * @return 子级分类下的所有商品集合
	 */
  public  List<ProductCategory> findChildren(ProductCategory paramProductCategory);

  public  List<ProductCategory> findChildren(ProductCategory paramProductCategory, Integer paramInteger);

  public  List<ProductCategory> findChildren(ProductCategory paramProductCategory, Integer paramInteger, String paramString);
  
public List<ProductCategory> findList(Integer count, List<Filter> filters,List<Order> orders, String cacheRegion);
  
  public List<ProductCategory> findList(Long[] ids);
  
  public List<ProductCategory> findAll();

  public void save(ProductCategory productCategory);
  
  public ProductCategory update(ProductCategory productCategory);
  
  public ProductCategory update(ProductCategory productCategory,String[] paramArrayOfString);
  
  public void delete(Long id);
  
  public void delete(Long[] ids);
  
  public void delete(ProductCategory productCategory);
  
  public ProductCategory find(Long id);
}
