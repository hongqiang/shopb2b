package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ProductCategory;

/**
 * DAO自定义接口实现
 * 
 * @author ThinkGem
 */
@Repository
class ProductCategoryDaoImpl extends BaseDaoImpl<ProductCategory,Long> implements
		ProductCategoryDaoCustom {

	@Override
	public List<ProductCategory> findRoots(Integer count) {
		String str = "select productCategory from ProductCategory productCategory where productCategory.parent is null order by productCategory.order asc";
		TypedQuery<ProductCategory> localTypedQuery = this.getEntityManager()
				.createQuery(str, ProductCategory.class)
				.setFlushMode(FlushModeType.COMMIT);
		if (count != null)
			localTypedQuery.setMaxResults(count.intValue());

		return localTypedQuery.getResultList();
	}

	@Override
	public List<ProductCategory> findParents(ProductCategory productCategory,
			Integer count) {
		if ((productCategory == null) || (productCategory.getParent() == null))
			return Collections.emptyList();
		String str = "select productCategory from ProductCategory productCategory where productCategory.id in (:ids) order by productCategory.grade asc";
		TypedQuery<ProductCategory> localTypedQuery = this.getEntityManager()
				.createQuery(str, ProductCategory.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("ids", productCategory.getTreePaths());
		if (count != null)
			localTypedQuery.setMaxResults(count.intValue());
		return localTypedQuery.getResultList();
	}

	/**
	 * 完成子级分类的封装，把ProductCategory的孩子都逐级封装成list，例如传入list为数据库表中的所有等级，
	 * ProductCategory为某个根等级
	 * ，则把该根等级下的所有子等级依次封装成树形结构的list，并返回list中的前count个；若传入null和null
	 * ，则把数据库中所有的等级组装后全部返回。 例子：（a,b）中a表示数据库表中的id，b表示其parent的id。
	 * (1,0),(2,0),(3,0)
	 * ,(4,0),(5,0),(6,1),(7,1),(8,1),(9,2),(10,2),(11,3),(12,4),(13,10) 该树形结构为
	 * 1 2 3 4 5 6 7 8 9 10 11 12 13 形成的list序列为：1,6,7,8,2,9,10,13,3,11,4,12,5
	 * 
	 * @param count
	 *            返回list中的结果数目
	 * @param paramProductCategory
	 *            某个等级的实例
	 * 
	 * @return 该等级下的所有子等级list
	 * 
	 */
	@Override
	public List<ProductCategory> findChildren(ProductCategory productCategory,
			Integer count) {
		String str;
		TypedQuery<ProductCategory> localTypedQuery;
		if (productCategory != null) {
			str = "select productCategory from ProductCategory productCategory where productCategory.treePath like :treePath order by productCategory.order asc";
			localTypedQuery = this
					.getEntityManager()
					.createQuery(str, ProductCategory.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("treePath",
							"%," + productCategory.getId() + "," + "%");
		} else {
			str = "select productCategory from ProductCategory productCategory order by productCategory.order asc";
			localTypedQuery = this.getEntityManager()
					.createQuery(str, ProductCategory.class)
					.setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null)
			localTypedQuery.setMaxResults(count.intValue());
		return packTheChildren(localTypedQuery.getResultList(), productCategory);
	}

	@Override
	public Page<ProductCategory> findPage(Pageable pageable) {
		String qlString = "select productCategory from ProductCategory productCategory where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter,pageable);
	}

	@Override
	public List<ProductCategory> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select productCategory from ProductCategory productCategory where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,orders);
	}

	@Override
	public List<ProductCategory> findAll() {
		return findList(null, null, null, null);
	}

	@Override
	public void persist(ProductCategory productCategory) {
		setProductCategoryOfTreepathAndGrade(productCategory);
		super.persist(productCategory);
	}

	@Override
	public ProductCategory merge(ProductCategory productCategory) {
		setProductCategoryOfTreepathAndGrade(productCategory);
		Iterator<ProductCategory> localIterator = findChildren(productCategory,
				null).iterator();
		while (localIterator.hasNext()) {
			ProductCategory localProductCategory = (ProductCategory) localIterator
					.next();
			setProductCategoryOfTreepathAndGrade(localProductCategory);
		}
		return (ProductCategory) super.merge(productCategory);
	}

	@Override
	public void delete(ProductCategory productCategory) {
		if (productCategory != null) {
			StringBuffer localStringBuffer = new StringBuffer(
					"update Product product set ");
			for (int i = 0; i < 20; i++) {
				String str = "attributeValue" + i;
				if (i == 0)
					localStringBuffer.append("product." + str + " = null");
				else
					localStringBuffer.append(", product." + str + " = null");
			}
			localStringBuffer
					.append(" where product.productCategory = :productCategory");
			this.getEntityManager().createQuery(localStringBuffer.toString())
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("productCategory", productCategory)
					.executeUpdate();
			super.remove(productCategory);
		}
	}

	/**
	 * 完成子级分类的封装，把ProductCategory的孩子都逐级封装成list，例如传入list为所有等级id，
	 * ProductCategory为某个根等级，则把该根等级下的所有子等级依次封装成树形结构的list，并返回该list
	 * 
	 * @param paramList
	 *            所有等待封装的ProductCategory的list
	 * @param paramProductCategory
	 *            某个等级的实例
	 * 
	 * @return 该等级下的所有子等级list
	 * 
	 */
	private List<ProductCategory> packTheChildren(
			List<ProductCategory> paramList,
			ProductCategory paramProductCategory) {
		ArrayList<ProductCategory> localArrayList = new ArrayList<ProductCategory>();
		if (paramList != null) {
			Iterator<ProductCategory> localIterator = paramList.iterator();
			while (localIterator.hasNext()) {
				ProductCategory localProductCategory = (ProductCategory) localIterator
						.next();
				if (localProductCategory.getParent() != paramProductCategory)
					continue;
				localArrayList.add(localProductCategory);
				localArrayList.addAll(packTheChildren(paramList,
						localProductCategory));
			}
		}
		return localArrayList;
	}

	/**
	 * 设置ProductCategory的treepath和grade属性。例如根级分类的treepath为',',grade为‘0’表示顶级分类，
	 * 次级分类的treepath为‘,1,’，其中这个1表示根级分类中第一个元素，grade为'1'表示一级分类，以此类推。
	 * 
	 * @param paramProductCategory
	 *            某个等级的实例
	 * 
	 * @return
	 * 
	 */
	private void setProductCategoryOfTreepathAndGrade(
			ProductCategory paramProductCategory) {
		if (paramProductCategory == null)
			return;
		ProductCategory localProductCategory = paramProductCategory.getParent();
		if (localProductCategory != null)
			paramProductCategory.setTreePath(localProductCategory.getTreePath()
					+ localProductCategory.getId() + ",");
		else
			paramProductCategory.setTreePath(",");
		paramProductCategory.setGrade(Integer.valueOf(paramProductCategory
				.getTreePaths().size()));
	}
}
