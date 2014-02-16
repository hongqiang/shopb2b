package com.hongqiang.shop.common.base.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;

import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Reflections;
import com.hongqiang.shop.common.utils.StringUtils;
import com.hongqiang.shop.modules.entity.OrderEntity;

public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {
	// 忽略的属性集合。更新实体类时，不需要更新的属性集合
	private static final String[] ignoreBaseProperties = { "id", "createDate",
			"updateDate" };

	/**
	 * 获取实体工厂管理对象
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<T> entityClass;

	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDaoImpl() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id
	 * 
	 * @return 实体对象
	 */
	public T find(ID id) {
		// Find by primary key id and return the T.
		if (id != null)
			return this.entityManager.find(this.entityClass, id);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<T> findList(String qlString, List<Object> parameter,
			Integer first, Integer count, List<Filter> filters,
			List<Order> orders) {
		StringBuilder stringBuilder = new StringBuilder(qlString);
		addFilter(stringBuilder, filters, parameter);
		addOrders(stringBuilder, orders, parameter);
		qlString = stringBuilder.toString();
		if (qlString.indexOf("order by") == -1) {
			if (OrderEntity.class.isAssignableFrom(this.entityClass)) {
				qlString += "order by order ASC";
			} else {
				qlString += "order by createDate DESC";
			}
		}
		System.out.println("productQuery = " + qlString);
		System.out.println(parameter.size());
		for (Object object : parameter) {
			System.out.println("object=" + object);
		}
		Query query = createQuery(qlString, parameter.toArray());
		if (first != null) {
			query.setFirstResult(first);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPage(String qlString, List<Object> parameter,
			Pageable pageable) {
		if (pageable == null)
			pageable = new Pageable();
		StringBuilder stringBuilder = new StringBuilder(qlString);
		addFilter(stringBuilder, pageable, parameter);
		addOrders(stringBuilder, pageable, parameter);
		qlString = stringBuilder.toString();
		if (qlString.indexOf("order by") == -1) {
			if (OrderEntity.class.isAssignableFrom(this.entityClass)) {
				qlString += "order by order ASC";
			} else {
				qlString += "order by createDate DESC";
			}
		}
		long count = count(stringBuilder, null, parameter);
		int i = (int) Math.ceil((double) count / pageable.getPageSize());
		if (i < pageable.getPageNumber()) {
			pageable.setPageNumber(i);
		}
		System.out.println("query=" + qlString);
		for (Object object : parameter) {
			System.out.println("object=" + object);
		}
		Query query = createQuery(qlString, parameter.toArray());
		query.setFirstResult((pageable.getPageNumber() - 1)
				* pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Object> list = query.list();
		if (list.size() > 0) {
			return new Page<T>(query.list(), count, pageable);
		}
		List<T> listTemp = new ArrayList<T>();
		return new Page<T>(listTemp, count, pageable);
	}

	@SuppressWarnings("unchecked")
	public Long count(StringBuilder qlString, List<Filter> filters,
			List<Object> params) {
		addFilter(qlString, filters, params);
		String countQlString = "select count(*) " + removeSelect(removeOrders(qlString.toString()));  
		Query query = createQuery(countQlString, params.toArray());
		System.out.println("basedao.count.query = " + query);
		System.out.println("basedao.count.size = " + params.size());
		for (Object object : params) {
			System.out.println("basedao.count.object = " + object);
		}
		List<Object> list = query.list();
		Long countNumber = 0L;
		if (list.size() > 0){
			countNumber = Long.valueOf(list.get(0).toString());
		}
		return countNumber;
	}

	/**
	 * 保存实体类 Make an instance managed and persistent.
	 */
	public void persist(T entity) {
		if (entity != null)
			this.entityManager.persist(entity);
	}

	/**
	 * 更新实体类 Merge the state of the given entity into the current persistence
	 * context.
	 */
	public T merge(T entity) {
		if (entity != null)
			return this.entityManager.merge(entity);
		return null;
	}

	/**
	 * 更新实体类
	 * 
	 * @param entity
	 * @param ignoreProperties
	 *            忽略的实体类属性
	 * @return
	 */
	public T update(T entity, String[] ignoreProperties) {
		// Check if the instance is a managed entity instance belonging to the
		// current persistence context.
		if (isManaged(entity))
			throw new IllegalArgumentException("Entity must not be managed");
		T localObject = find(getIdentifier(entity));
		if (localObject != null) {
			BeanUtils.copyProperties(entity, localObject, (String[]) ArrayUtils
					.addAll(ignoreProperties, ignoreBaseProperties));
			return merge(localObject);
		}
		return merge(entity);
	}

	/**
	 * 从数据库删除实体类 Remove the entity instance.
	 */
	public void remove(T entity) {
		if (entity != null)
			this.entityManager.remove(entity);
	}

	/**
	 * 为数据库刷新实体类 Refresh the state of the instance from the database,
	 * overwriting changes made to the entity, if any.
	 */
	public void refresh(T entity) {
		if (entity != null)
			this.entityManager.refresh(entity);
	}

	/**
	 * 根据实体类得到其id
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ID getIdentifier(T entity) {
		if (entity != null) {
			return (ID) this.entityManager.getEntityManagerFactory()
					.getPersistenceUnitUtil().getIdentifier(entity);
		}
		return null;
	}

	/**
	 * 判断是否包含实体类
	 */
	public boolean isManaged(T entity) {
		return this.entityManager.contains(entity);
	}

	/**
	 * Remove the given entity from the persistence context, causing a managed
	 * entity to become detached.
	 */
	public void detach(T entity) {
		this.entityManager.detach(entity);
	}

	/**
	 * Lock an entity instance that is contained in the persistence context with
	 * the specified lock mode type.
	 */
	public void lock(T entity, LockModeType lockModeType) {
		if ((entity != null) && (lockModeType != null))
			this.entityManager.lock(entity, lockModeType);
	}

	/**
	 * 获取 Session
	 */
	public Session getSession() {
		return (Session) getEntityManager().getDelegate();
	}

	/**
	 * 强制与数据库同步
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear() {
		getSession().clear();
	}

	protected void addFilter(StringBuilder qlString, List<Filter> filters,
			List<Object> params) {
		if (filters != null && filters.size() > 0) {
			Iterator<Filter> localIterator = filters.iterator();
			while (localIterator.hasNext()) {
				Filter localFilter = (Filter) localIterator.next();
				if ((localFilter == null)
						|| (StringUtils.isEmpty(localFilter.getProperty())))
					continue;
				if ((localFilter.getOperator() == Filter.Operator.eq)
						&& (localFilter.getValue() != null)) {
					if ((localFilter.getIgnoreCase() != null)
							&& (localFilter.getIgnoreCase().booleanValue())
							&& ((localFilter.getValue() instanceof String))) {
						qlString.append(" and " + localFilter.getProperty()
								+ " = ? ");
						params.add(((String) localFilter.getValue())
								.toLowerCase());
					} else {
						qlString.append(" and " + localFilter.getProperty()
								+ " = ? ");
						params.add(localFilter.getValue());
					}
				} else if ((localFilter.getOperator() == Filter.Operator.ne)
						&& (localFilter.getValue() != null)) {
					if ((localFilter.getIgnoreCase() != null)
							&& (localFilter.getIgnoreCase().booleanValue())
							&& ((localFilter.getValue() instanceof String))) {
						qlString.append(" and " + localFilter.getProperty()
								+ " <> ? ");
						params.add(((String) localFilter.getValue())
								.toLowerCase());
					} else {
						qlString.append(" and " + localFilter.getProperty()
								+ " <> ? ");
						params.add(localFilter.getValue());
					}
				} else if ((localFilter.getOperator() == Filter.Operator.gt)
						&& (localFilter.getValue() != null)) {
					qlString.append(" and " + localFilter.getProperty()
							+ " > ? ");
					params.add((Number) localFilter.getValue());
				} else if ((localFilter.getOperator() == Filter.Operator.lt)
						&& (localFilter.getValue() != null)) {
					qlString.append(" and " + localFilter.getProperty()
							+ " < ? ");
					params.add((Number) localFilter.getValue());
				} else if ((localFilter.getOperator() == Filter.Operator.ge)
						&& (localFilter.getValue() != null)) {
					qlString.append(" and " + localFilter.getProperty()
							+ " >= ? ");
					params.add((Number) localFilter.getValue());
				} else if ((localFilter.getOperator() == Filter.Operator.le)
						&& (localFilter.getValue() != null)) {
					qlString.append(" and " + localFilter.getProperty()
							+ " <= ? ");
					params.add((Number) localFilter.getValue());
				} else if ((localFilter.getOperator() == Filter.Operator.like)
						&& (localFilter.getValue() != null)
						&& ((localFilter.getValue() instanceof String))) {
					qlString.append(" and " + localFilter.getProperty()
							+ " like ? ");
					params.add((String) localFilter.getValue());
				} else if ((localFilter.getOperator() == Filter.Operator.in)
						&& (localFilter.getValue() != null)) {
					qlString.append(" and " + localFilter.getProperty()
							+ " in (?) ");
					params.add(new Object[] { localFilter.getValue() });
				} else if (localFilter.getOperator() == Filter.Operator.isNull) {
					qlString.append(" and " + localFilter.getProperty()
							+ " is null ");
				} else {
					if (localFilter.getOperator() != Filter.Operator.isNotNull)
						continue;
					qlString.append(" and " + localFilter.getProperty()
							+ " is not null ");
				}
			}
		}
	}

	protected void addFilter(StringBuilder qlString, Pageable pageable,
			List<Object> params) {
		if ((StringUtils.isNotEmpty(pageable.getSearchProperty()))
				&& (StringUtils.isNotEmpty(pageable.getSearchValue()))) {
			qlString.append(" and " + pageable.getSearchProperty() + " like ?");
			params.add("%" + pageable.getSearchValue() + "%");
		}
		if (pageable.getFilters() != null) {
			addFilter(qlString, pageable.getFilters(), params);
		}
	}

	protected void addOrders(StringBuilder qlString,
			List<com.hongqiang.shop.common.utils.Order> orderList,
			List<Object> params) {
		if (orderList != null && orderList.size() > 0) {
			if (qlString.indexOf("order by") == -1) {
				qlString.append("order by ");
			}else {
				qlString.append(" , ");
			}
			Iterator<com.hongqiang.shop.common.utils.Order> localIterator = orderList.iterator();
			while (localIterator.hasNext()) {
				com.hongqiang.shop.common.utils.Order localOrder = (com.hongqiang.shop.common.utils.Order) localIterator.next();
				if (localOrder.getDirection() == com.hongqiang.shop.common.utils.Order.Direction.asc) {
					qlString.append(localOrder.getProperty() + " ASC,");
				} else {
					if (localOrder.getDirection() != com.hongqiang.shop.common.utils.Order.Direction.desc)
						continue;
					qlString.append(localOrder.getProperty() + " DESC,");
				}
			}
			if (qlString.charAt(qlString.length() - 1) == ',') {
				qlString.deleteCharAt(qlString.length() - 1);
			}
		}
	}

	protected void addOrders(StringBuilder qlString, Pageable pageable,
			List<Object> params) {
		int tag = 0;
		if ((StringUtils.isNotEmpty(pageable.getOrderProperty()))&& (pageable.getOrderDirection() != null)) {
			tag = 1;
			if (qlString.indexOf("order by") == -1) {
				qlString.append("order by ");
			}else {
				qlString.append(" , ");
			}
			if (pageable.getOrderDirection() == com.hongqiang.shop.common.utils.Order.Direction.asc) {
				qlString.append(" "+pageable.getOrderProperty() + " ASC ");
			} else if (pageable.getOrderDirection() == com.hongqiang.shop.common.utils.Order.Direction.desc) {
				qlString.append(" "+pageable.getOrderProperty() + " DESC ");
			}
		}
		if (pageable.getOrders() != null && pageable.getOrders().size() > 0) {
			if (tag == 0) {
				qlString.append("order by ");
			} else {
				qlString.append(" , ");
			}
			Iterator<com.hongqiang.shop.common.utils.Order> localIterator = pageable.getOrders().iterator();
			while (localIterator.hasNext()) {
				com.hongqiang.shop.common.utils.Order localOrder = (com.hongqiang.shop.common.utils.Order) localIterator.next();
				if (localOrder.getDirection() == com.hongqiang.shop.common.utils.Order.Direction.asc) {
					qlString.append(" "+localOrder.getProperty() + " ASC,");
				} else {
					if (localOrder.getDirection() != com.hongqiang.shop.common.utils.Order.Direction.desc)
						continue;
					qlString.append(" "+localOrder.getProperty() + " DESC,");
				}
			}
			if (qlString.charAt(qlString.length() - 1) == ',') {
				qlString.deleteCharAt(qlString.length() - 1);
			}
		}
	}

	/**
	 * 创建 QL 查询对象
	 * 
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter) {
		Query query = getSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}

    /** 
     * 去除qlString的select子句。 
     * @param hql 
     * @return 
     */  
    private String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    } 
    
    /** 
     * 去除hql的orderBy子句。 
     * @param hql 
     * @return 
     */  
    private String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    } 
	
	/**
	 * 设置查询参数
	 * 
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Object... parameter) {
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				query.setParameter(i, parameter[i]);
			}
		}
	}
}
