package com.hongqiang.shop.common.base.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;

public interface BaseDao<T, ID extends Serializable> {

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager();

	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id
	 * 
	 * @return 实体对象
	 */
	public T find(ID id);

	 /**
     * QL 查询
     * @param qlString sql语句
     * @param parameter sql语句中的参数
     * @param first 返回firstResults个结果
     * @param count 限制最大结果数
     * @param filters  sql的where条件
     * @param orderList sql的order条件
     * @return
     */
	public List<T> findList(String qlString, List<Object> parameter,
			Integer first, Integer count, List<Filter> filters,
			List<Order> orders);

	 /**
     * QL 分页查询
     * @param page 
     * @param qlString sql语句
     * @param parameter sql语句中的参数
     * @param pageable 分页类
     * @return
     */
	public Page<T> findPage(String qlString, List<Object> parameter,
			Pageable pageable);

	/**
	 * 满足sql查询的结果数
	 * 
	 * @param qlString  sql语句
	 * @param filters sql的where条件
	 * @param params sql语句中的参数
	 * @return
	 */
	public Long count(StringBuilder qlString, List<Filter> filters,
			List<Object> params);

	/**
	 * 保存实体类 Make an instance managed and persistent.
	 */
	public void persist(T entity);

	/**
	 * 更新实体类 Merge the state of the given entity into the current persistence
	 * context.
	 */
	public T merge(T entity);

	/**
	 * 更新实体类
	 * 
	 * @param entity
	 * @param ignoreProperties
	 *            忽略的实体类属性
	 * @return
	 */
	public T update(T entity, String[] ignoreProperties);
	
	/**
	 * 从数据库删除实体类 Remove the entity instance.
	 */
	public void remove(T entity);

	/**
	 * 为数据库刷新实体类 Refresh the state of the instance from the database,
	 * overwriting changes made to the entity, if any.
	 */
	public void refresh(T entity);

	/**
	 * 根据实体类得到其id
	 * 
	 * @param entity
	 * @return
	 */
	public ID getIdentifier(T entity);

	/**
	 * 判断是否包含实体类
	 */
	public boolean isManaged(T entity);

	/**
	 * Remove the given entity from the persistence context, causing a managed
	 * entity to become detached.
	 */
	public void detach(T entity);

	/**
	 * Lock an entity instance that is contained in the persistence context with
	 * the specified lock mode type.
	 */
	public void lock(T entity, LockModeType lockModeType);

	/**
	 * 清除Session.
	 * 
	 */
	public void clear();

	/**
	 * 刷新session.
	 * 
	 */
	public void flush();
}
