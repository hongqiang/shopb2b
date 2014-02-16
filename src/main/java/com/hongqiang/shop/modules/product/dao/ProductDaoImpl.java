package com.hongqiang.shop.modules.product.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.FlushModeType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Goods;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.entity.Sn;
import com.hongqiang.shop.modules.entity.SpecificationValue;
import com.hongqiang.shop.modules.entity.Tag;

@Repository
class ProductDaoImpl extends BaseDaoImpl<Product,Long> implements ProductDaoCustom {

	class SortSpecificationValue implements Comparator<SpecificationValue> {
		public int compare(SpecificationValue a1, SpecificationValue a2) {
			return new CompareToBuilder().append(a1.getSpecification(),
					a2.getSpecification()).toComparison();
		}
	}

	private static final Pattern pattern = Pattern.compile("\\d*");

	@Autowired
	private GoodsDao goodsDao;

	@Autowired
	private SnDao snDao;

	public boolean snExists(String sn) {
		if (sn == null)
			return false;
		String str = "select count(*) from Product product where lower(product.sn) = lower(:sn)";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn)
				.getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public List<Product> search(String keyword, Boolean isGift, Integer count) {
		if (StringUtils.isEmpty(keyword))
			return Collections.emptyList();
		String sqlString = "select DISTINCT product from Product product where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (pattern.matcher(keyword).matches()) {
			sqlString += " and (product.id = ? or product.sn like (?) or product.fullName like (?)) ";
			params.add(Long.valueOf(keyword));
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
		} else {
			sqlString += " and (product.sn like (?) or product.fullName like (?)) ";
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
		}
		if (isGift != null) {
			sqlString += " and product.isGift =? ";
			params.add(isGift);
		}
		sqlString += " order by product.isTop DESC, product.updateDate DESC ";
		return super.findList(sqlString, params, null, count, null, null);
	}

	@Override
	public List<Product> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select product from Product product where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,
				orders);
	}

	@Override
	public List<Product> findList(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Integer count,
			List<Filter> filters, List<Order> orders) {
		List<Object> params = new ArrayList<Object>();
		String sqlString =  composeSql( productCategory,  brand,promotion,  tags,
				 attributeValue,  startPrice,endPrice,  isMarketable,  isList,
				 isTop,  isGift,  isOutOfStock,isStockAlert,  orderType,params, null);
		return super.findList(sqlString, params, null, count, filters, orders);
	}

	@Override
	public List<Product> findList(ProductCategory productCategory,
			Date beginDate, Date endDate, Integer first, Integer count) {
		String sqlString = "select product from Product product where product.isMarketable is true ";
		List<Object> params = new ArrayList<Object>();
		if (productCategory != null) {
			sqlString += " and (product.productCategory = ? ";
			params.add(productCategory);
			sqlString += " or product.productCategory.treePath like ?) ";
			params.add("%," + productCategory.getId() + ",%");
		}
		if (beginDate != null) {
			sqlString += " and product.createDate>=?";
			params.add(beginDate);
		}
		if (endDate != null) {
			sqlString += " and product.createDate<=?";
			params.add(endDate);
		}
		sqlString += " order by product.isTop DESC, product.updateDate DESC ";
		return super.findList(sqlString, params, first, count, null, null);
	}

	@Override
	public List<Product> findList(Goods goods, Set<Product> excludes) {
		String sqlString = "select DISTINCT product from Product product where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (goods != null) {
			sqlString += " and product.goods = ? ";
			params.add(goods);
		}
		if ((excludes != null) && (!excludes.isEmpty())) {
			sqlString += " and product not in (";
			for (Product product : excludes) {
					sqlString += " ?, ";
					params.add(product);
			}
			sqlString = sqlString.substring(0,sqlString.length()-2);
			sqlString +=")";
		}
		return super.findList(sqlString, params, null, null, null, null);
	}

	public Page<Product> findPage(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Pageable pageable) {
		List<Object> params = new ArrayList<Object>();
		String sqlString =  composeSql( productCategory,  brand,promotion,  tags,
				 attributeValue,  startPrice,endPrice,  isMarketable,  isList,
				 isTop,  isGift,  isOutOfStock,isStockAlert,  orderType, params, pageable);
		return super.findPage(sqlString, params, pageable);
	}

	@Override
	public long count(Filter[] filters) {
		String qlString = "select  product from Product product where 1=1 ";
		StringBuilder stringBuilder = new StringBuilder(qlString);
		List<Object> params = new ArrayList<Object>();
		if (filters == null) {
			return super.count(stringBuilder, null, params);
		}
		return super.count(stringBuilder, Arrays.asList(filters), params);
	}

	public Page<Product> findPage(Member member, Pageable pageable) {
		if (member == null){
			List<Product> products = new ArrayList<Product>();
			return new Page<Product>(products,0L,pageable);
		}
		String qlString = "select product from Product product "+
			"join product.favoriteMembers favoriteMember "+
				"where favoriteMember= ? ";
		qlString += " order by product.createDate DESC";
		List<Object> params = new ArrayList<Object>();
		params.add(member);
		return super.findPage(qlString, params, pageable);
	}

	@SuppressWarnings("unchecked")
	public Page<Object> findSalesPage(Date beginDate, Date endDate,
			Pageable pageable) {
		String qlString = "select product, sum(orderItems.quantity), "+
						"sum(orderItems.quantity * orderItems.price) "+
				"from Product product, OrderItem orderItems, Order o where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (beginDate != null) {
			qlString += " and orderItems.createDate >= ? ";
			params.add(beginDate);
		}
		if (endDate != null) {
			qlString += " and orderItems.createDate <= ? ";
			params.add(endDate);
		}
		qlString += " and o.orderStatus = ? ";
		params.add(com.hongqiang.shop.modules.entity.Order.OrderStatus.completed);
		qlString += " and o.paymentStatus = ? ";
		params.add(com.hongqiang.shop.modules.entity.Order.PaymentStatus.paid);
		qlString += " group by product.id ";
		StringBuilder stringBuilder = new StringBuilder(qlString);
		Long count = super.count(stringBuilder, null, params);
		int i = (int) Math.ceil(count.longValue() / pageable.getPageSize());
		if (i < pageable.getPageNumber())
			pageable.setPageNumber(i);
		qlString += " order by sum(orderItems.quantity * orderItems.price) DESC ";
		Query query = createQuery(qlString,params.toArray());
		 query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Object> list = query.list();
       if (list.size() > 0){
       	return new Page<Object>(query.list(),count,pageable);
       }
       List<Object> listTemp = new ArrayList<Object>();
		return new Page<Object>(listTemp,count,pageable);
	}

	public Long count(Member favoriteMember, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isGift,
			Boolean isOutOfStock, Boolean isStockAlert) {
		String qlString = "select product from Product product ";
		if (favoriteMember != null){
			qlString += "join product.favoriteMembers favoriteMembers ";
		}
		qlString += "where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (favoriteMember != null) {
			qlString += " and favoriteMembers= ?";
			params.add(favoriteMember);
		}
		if (isMarketable != null) {
			qlString += " and product.isMarketable= ?";
			params.add(isMarketable);
		}
		if (isList != null) {
			qlString += " and product.isList= ?";
			params.add(isList);
		}
		if (isTop != null) {
			qlString += " and product.isTop= ?";
			params.add(isTop);
		}
		if (isGift != null) {
			qlString += " and product.isGift= ?";
			params.add(isGift);
		}
		if (isOutOfStock != null) {
			if (isOutOfStock.booleanValue()) {
				qlString += " and (product.stock is not null and product.stock <= product.allocatedStock) ";
			} else {
				qlString += " and (product.stock is  null or product.stock> product.allocatedStock)";
			}
		}
		if (isStockAlert != null) {
			Integer stockAlertCount = SettingUtils.get().getStockAlertCount();
			if (isStockAlert.booleanValue()) {
				qlString += " and (product.stock is not null and product.stock<= (product.allocatedStock+?)) ";
				params.add(stockAlertCount);
			} else {
				qlString += " and (product.stock is null or product.stock> (product.allocatedStock+?)) ";
				params.add(stockAlertCount);
			}
		}
		StringBuilder stringBuilder = new StringBuilder(qlString);
		return super.count(stringBuilder, null, params);
	}

	public boolean isPurchased(Member member, Product product) {
		if ((member == null) || (product == null))
			return false;
		String str = "select count(*) from OrderItem orderItem where orderItem.product = :product and "+
			"orderItem.order.member = :member and orderItem.order.orderStatus = :orderStatus";
		Long localLong = (Long) this
				.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("product", product)
				.setParameter("member", member)
				.setParameter("orderStatus",com.hongqiang.shop.modules.entity.Order.OrderStatus.completed)
				.getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public void persist(Product product) {
		setProductFullName(product);
		super.persist(product);
	}

	@Override
	public Product merge(Product product) {
		String str;
		if (!product.getIsGift().booleanValue()) {
			str = "delete from GiftItem giftItem where giftItem.gift = :product";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("product", product).executeUpdate();
		}
		if ((!product.getIsMarketable().booleanValue())
				|| (product.getIsGift().booleanValue())) {
			str = "delete from CartItem cartItem where cartItem.product = :product";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("product", product).executeUpdate();
		}
		setProductFullName(product);
		return (Product) super.merge(product);
	}

	@Override
	public void remove(Product product) {
		if (product != null) {
			Goods localGoods = product.getGoods();
			if ((localGoods != null) && (localGoods.getProducts() != null)) {
				localGoods.getProducts().remove(product);
				if (localGoods.getProducts().isEmpty())
					this.goodsDao.remove(localGoods);
			}
		}
		super.remove(product);
	}

	@Override
	public void deleteAttributeOfProduct(Attribute attribute) {
		String str1 = "attributeValue" + attribute.getPropertyIndex();
		String str2 = "update Product product set product." + str1
				+ " = null where product.productCategory = :productCategory";
		this.getEntityManager()
				.createQuery(str2)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("productCategory", attribute.getProductCategory())
				.executeUpdate();
	}

	@Override
	public void updateAttributeOfProduct(Attribute attribute) {
		String str1 = "attributeValue" + attribute.getPropertyIndex();
		String str2 = "update Product product set product." + str1 + " = '"
				+ attribute.getName()
				+ "' where product.productCategory = :productCategory";
		this.getEntityManager()
				.createQuery(str2)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("productCategory", attribute.getProductCategory())
				.executeUpdate();
	}

	@Override
	public void persist(Goods goods) {
		if (goods.getProducts() != null) {
			Iterator<Product> localIterator = goods.getProducts().iterator();
			while (localIterator.hasNext()) {
				Product localProduct = (Product) localIterator.next();
				setProductFullName(localProduct);
			}
		}
	}
	
	private String composeSql(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, List<Object> params, Pageable pageable) {
		String sqlString = "select DISTINCT product from ";
		if (promotion != null) {
			sqlString += " Product product left join product.promotions pPromotion ";
			sqlString += " left join product.productCategory productCategory ";
			sqlString += " left join productCategory.promotions pcPromotion";
			sqlString += " left join product.brand brand ";
			sqlString += " left join brand.promotions bPromotion";
		}else{
			sqlString += " Product product";
		}
		if ((tags!=null) && (!tags.isEmpty())) {
			sqlString += " join product.tags tags ";
		}
		sqlString += " where 1=1 ";
		if (productCategory != null) {
			sqlString += " and (product.productCategory = ? ";
			params.add(productCategory);
			sqlString += " or product.productCategory.treePath like ?) ";
			params.add("%," + productCategory.getId() + ",%");
		}
		if (brand != null) {
			sqlString += " and product.brand=?";
			params.add(brand);
		}
		if (promotion != null) {
			sqlString += " and (pPromotion =?";
			params.add(promotion);
			sqlString += " or pcPromotion =?";
			params.add(promotion);
			sqlString += " or bPromotion =?)";
			params.add(promotion);
		}
		if (attributeValue != null) {
			Iterator<Entry<Attribute, String>> localIterator = attributeValue
					.entrySet().iterator();
			while (localIterator.hasNext()) {
				Entry<Attribute, String> pairs = (Entry<Attribute, String>) localIterator
						.next();
				String localString = "attributeValue"
						+ ((Attribute) pairs.getKey()).getPropertyIndex();
				sqlString += " and product." + localString + " = ? ";
				params.add(pairs.getValue());
			}
		}
		if ((startPrice != null) && (endPrice != null)
				&& (startPrice.compareTo(endPrice) > 0)) {
			BigDecimal localPrice = startPrice;
			startPrice = endPrice;
			endPrice = localPrice;
		}
		if ((startPrice != null)
				&& (startPrice.compareTo(new BigDecimal(0)) >= 0)) {
			sqlString += " and product.price>=?";
			params.add(startPrice);
		}
		if ((endPrice != null) && (endPrice.compareTo(new BigDecimal(0)) >= 0)) {
			sqlString += " and product.price<=?";
			params.add(endPrice);
		}
		if ((tags != null) && (!tags.isEmpty())) {
//			sqlString += " and tags in (?)";
//			params.add(tags);
			sqlString += " and tags in (";
			for (Tag tag : tags) {
					sqlString += " ?, ";
					params.add(tag);
			}
			sqlString = sqlString.substring(0,sqlString.length()-2);
			sqlString +=")";
		}
		if (isMarketable != null) {
			sqlString += " and product.isMarketable=?";
			params.add(isMarketable);
		}
		if (isList != null) {
			sqlString += " and product.isList=?";
			params.add(isList);
		}
		if (isTop != null) {
			sqlString += " and product.isTop=?";
			params.add(isTop);
		}
		if (isGift != null) {
			sqlString += " and product.isGift=?";
			params.add(isGift);
		}
		if (isOutOfStock != null) {
			if (isOutOfStock.booleanValue()) {
				sqlString += " and (product.stock is not null and product.stock<= product.allocatedStock) ";
			} else {
				sqlString += " and (product.stock is  null or product.stock> product.allocatedStock)";
			}
		}
		if (isStockAlert != null) {
			Integer stockAlertCount = SettingUtils.get().getStockAlertCount();
			if (isStockAlert.booleanValue()) {
				sqlString += " and (product.stock is not null and product.stock<= (product.allocatedStock+?)) ";
				params.add(stockAlertCount);
			} else {
				sqlString += " and (product.stock is null or product.stock> (product.allocatedStock+?)) ";
				params.add(stockAlertCount);
			}
		}
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		if (pageable != null) {
			super.addFilter(stringBuilder, pageable, params);
			super.addOrders(stringBuilder, pageable, params);
			pageable.setSearchProperty(null);
			pageable.setSearchValue(null);
			pageable.setFilters(null);
			pageable.setOrderProperty(null);
			pageable.setOrderDirection(null);
			pageable.setOrders(null);
		}
		sqlString = stringBuilder.toString();
		if (orderType == Product.OrderType.priceAsc) {
			sqlString += " order by product.price ASC, product.createDate DESC ";
		} else if (orderType == Product.OrderType.priceDesc) {
			sqlString += " order by product.price DESC, product.createDate DESC ";
		} else if (orderType == Product.OrderType.salesDesc) {
			sqlString += " order by product.sales DESC, product.createDate DESC ";
		} else if (orderType == Product.OrderType.scoreDesc) {
			sqlString += " order by product.score DESC, product.createDate DESC ";
		} else if (orderType == Product.OrderType.dateDesc) {
			sqlString += " order by product.createDate DESC ";
		} else {
			sqlString += " order by product.isTop DESC, product.updateDate DESC ";
		}
		return sqlString;
	}

	@Override
	public void mergeForDelete(Goods goods) {
		if (goods.getProducts() != null) {
			Iterator<Product> localIterator = goods.getProducts().iterator();
			while (localIterator.hasNext()) {
				Product product = (Product) localIterator.next();
				if (product.getId() != null) {
					String query;
					if (!product.getIsGift().booleanValue()) {
						query = "delete from GiftItem giftItem where giftItem.gift = :product";
						this.getEntityManager().createQuery(query)
								.setFlushMode(FlushModeType.COMMIT)
								.setParameter("product", product)
								.executeUpdate();
					}
					if ((!product.getIsMarketable().booleanValue())
							|| (product.getIsGift().booleanValue())) {
						query = "delete from CartItem cartItem where cartItem.product = :product";
						this.getEntityManager().createQuery(query)
								.setFlushMode(FlushModeType.COMMIT)
								.setParameter("product", product)
								.executeUpdate();
					}
				}
				setProductFullName(product);
			}
		}
	}

	private void setProductFullName(Product paramProduct) {
		if (paramProduct == null)
			return;
		if (StringUtils.isEmpty(paramProduct.getSn())) {
			String snString;
			do {
				snString = this.snDao.generate(Sn.Type.product);
			} while (snExists(snString));
			paramProduct.setSn(snString);
		}
		StringBuffer stringBuffer = new StringBuffer(paramProduct.getName());
		if ((paramProduct.getSpecificationValues() != null)
				&& (!paramProduct.getSpecificationValues().isEmpty())) {
			List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>(paramProduct.getSpecificationValues());
			Collections.sort(specificationValues, new SortSpecificationValue());
			stringBuffer.append("[");
			int i = 0;
			Iterator<SpecificationValue> localIterator = specificationValues.iterator();
			while (localIterator.hasNext()) {
				if (i != 0)
					stringBuffer.append(" ");
				stringBuffer.append(((SpecificationValue) localIterator.next()).getName());
				i++;
			}
			stringBuffer.append("]");
		}
		paramProduct.setFullName(stringBuffer.toString());
	}

}