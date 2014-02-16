package com.hongqiang.shop.modules.product.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.CacheUtils;
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
import com.hongqiang.shop.modules.product.dao.ProductDao;
import com.hongqiang.shop.modules.util.service.TemplateService;

@Service
public class ProductServiceImpl extends BaseService implements ProductService,
		DisposableBean,ServletContextAware {
	private long systemTime = System.currentTimeMillis();
	public static final String HITS_CACHE_NAME = "productHits";
	public static final int HITS_CACHE_INTERVAL = 600000;
	private ServletContext servletContext;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private TemplateService templateService;
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return this.productDao.snExists(sn);
	}

	@Transactional(readOnly = true)
	public Product findBySn(String sn) {
		return this.productDao.findBySn(sn);
	}

	public Product find(Long id) {
		return this.productDao.findById(id);
	}

	@Transactional(readOnly = true)
	public boolean snUnique(String previousSn, String currentSn) {
		if (StringUtils.equalsIgnoreCase(previousSn, currentSn))
			return true;
		return !this.productDao.snExists(currentSn);
	}

	@Transactional(readOnly = true)
	public List<Product> search(String keyword, Boolean isGift, Integer count) {
		return this.productDao.search(keyword, isGift, count);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(Long[] ids) {
		List<Product> localArrayList = new ArrayList<Product>();
		if (ids != null)
			for (Long id : ids) {
				Product localObject = this.productDao.findById(id);
				if (localObject == null)
					continue;
				localArrayList.add(localObject);
			}
		return localArrayList;
	}

	@Transactional(readOnly = true)
	public List<Product> findList(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Integer count,
			List<Filter> filters, List<Order> orders) {
		return this.productDao.findList(productCategory, brand, promotion,
				tags, attributeValue, startPrice, endPrice, isMarketable,
				isList, isTop, isGift, isOutOfStock, isStockAlert, orderType,
				count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "product" })
	public List<Product> findList(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Integer count,
			List<Filter> filters, List<Order> orders, String cacheRegion) {
		return this.productDao.findList(productCategory, brand, promotion,
				tags, attributeValue, startPrice, endPrice, isMarketable,
				isList, isTop, isGift, isOutOfStock, isStockAlert, orderType,
				count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(ProductCategory productCategory,
			Date beginDate, Date endDate, Integer first, Integer count) {
		return this.productDao.findList(productCategory, beginDate, endDate,
				first, count);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Pageable pageable) {
		return this.productDao.findPage(productCategory, brand, promotion,
				tags, attributeValue, startPrice, endPrice, isMarketable,
				isList, isTop, isGift, isOutOfStock, isStockAlert, orderType,
				pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(Member member, Pageable pageable) {
		return this.productDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Object> findSalesPage(Date beginDate, Date endDate,
			Pageable pageable) {
		return this.productDao.findSalesPage(beginDate, endDate, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member favoriteMember, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isGift,
			Boolean isOutOfStock, Boolean isStockAlert) {
		return this.productDao.count(favoriteMember, isMarketable, isList,
				isTop, isGift, isOutOfStock, isStockAlert);
	}

	@Transactional(readOnly = true)
	public boolean isPurchased(Member member, Product product) {
		return this.productDao.isPurchased(member, product);
	}

	public long viewHits(Long id) {
		Long hits = (Long) CacheUtils.get(HITS_CACHE_NAME, id.toString());
		if (hits == null) {
			Product product = (Product) this.productDao.find(id);
			if (product == null)
				return 0L;
			hits = product.getHits();
		}
		Long returnHits = Long.valueOf(hits.longValue() + 1L);
		CacheUtils.put(HITS_CACHE_NAME, id.toString(), returnHits);
		long l = System.currentTimeMillis();
		if (l > this.systemTime + HITS_CACHE_INTERVAL) {
			this.systemTime = l;
			destroyCache();
			CacheUtils.removeAll(HITS_CACHE_NAME);
		}
		return returnHits.longValue();
	}

	public void destroy() {
		destroyCache();
	}

	private void destroyCache() {
		@SuppressWarnings("unchecked")
		List<Long> localList = (List<Long>) CacheUtils.getKeys(HITS_CACHE_NAME);
		Iterator<Long> localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			Long id = (Long) localIterator.next();
			Product localProduct = (Product) this.productDao.find(id);
			if (localProduct == null)
				continue;
			this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
			Long l1 = (Long) CacheUtils.get(HITS_CACHE_NAME, id.toString());
			long l2 = l1 - localProduct.getHits().longValue();
			Calendar localCalendar1 = Calendar.getInstance();
			Calendar localCalendar2 = Calendar.getInstance();
			localCalendar2.setTime(localProduct.getWeekHitsDate());
			Calendar localCalendar3 = Calendar.getInstance();
			localCalendar3.setTime(localProduct.getMonthHitsDate());

			if ((localCalendar1.get(1) != localCalendar2.get(1))
					|| (localCalendar1.get(3) > localCalendar2.get(3)))
				localProduct.setWeekHits(Long.valueOf(l2));
			else
				localProduct.setWeekHits(Long.valueOf(localProduct
						.getWeekHits().longValue() + l2));
			if ((localCalendar1.get(1) != localCalendar3.get(1))
					|| (localCalendar1.get(2) > localCalendar3.get(2)))
				localProduct.setMonthHits(Long.valueOf(l2));
			else
				localProduct.setMonthHits(Long.valueOf(localProduct
						.getMonthHits().longValue() + l2));
			localProduct.setHits(Long.valueOf(l1));
			localProduct.setWeekHitsDate(new Date());
			localProduct.setMonthHitsDate(new Date());
			this.productDao.merge(localProduct);
		}
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void save(Product product) {
		Assert.notNull(product);
		this.productDao.persist(product);
		this.productDao.flush();
		build(product);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Product update(Product product) {
		Assert.notNull(product);
		Product localProduct = (Product) this.productDao.merge(product);
		this.productDao.flush();
		build(localProduct);
		return localProduct;
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Product update(Product product, String[] ignoreProperties) {
		return (Product) this.productDao.update(product, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long id) {
		this.productDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.productDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Product product) {
		if (product != null)
			deleteStaticProduct(product);
//			this.staticService.delete(product);
		this.productDao.delete(product);
	}

	@Transactional(readOnly = true)
	public int build(Product product) {
		Assert.notNull(product);
		deleteStaticProduct(product);
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("productContent");
		int i = 0;
		if (product.getIsMarketable().booleanValue()) {
			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("product", product);
			i += build(localTemplate.getTemplatePath(), product.getPath(),
					localHashMap);
		}
		return i;
	}

	@Transactional(readOnly = true)
	private int build(String templatePath, String staticPath,
			Map<String, Object> model) {
		Assert.hasText(templatePath);
		Assert.hasText(staticPath);
		FileOutputStream localFileOutputStream = null;
		OutputStreamWriter localOutputStreamWriter = null;
		BufferedWriter localBufferedWriter = null;
		try {
			freemarker.template.Template localTemplate = this.freeMarkerConfigurer
					.getConfiguration().getTemplate(templatePath);
			File localFile1 = new File(
					this.servletContext.getRealPath(staticPath));
			File localFile2 = localFile1.getParentFile();
			if (!localFile2.exists())
				localFile2.mkdirs();
			localFileOutputStream = new FileOutputStream(localFile1);
			localOutputStreamWriter = new OutputStreamWriter(
					localFileOutputStream, "UTF-8");
			localBufferedWriter = new BufferedWriter(localOutputStreamWriter);
			localTemplate.process(model, localBufferedWriter);
			localBufferedWriter.flush();
			return 1;
		} catch (Exception localException1) {
			localException1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(localBufferedWriter);
			IOUtils.closeQuietly(localOutputStreamWriter);
			IOUtils.closeQuietly(localFileOutputStream);
		}
		return 0;
	}

	@Transactional(readOnly = true)
	public int deleteStaticProduct(Product product) {
		Assert.notNull(product);
		return delete(product.getPath());
	}

	@Transactional(readOnly = true)
	private int delete(String staticPath) {
		Assert.hasText(staticPath);
		File localFile = new File(this.servletContext.getRealPath(staticPath));
		if (localFile.exists()) {
			localFile.delete();
			return 1;
		}
		return 0;
	}
}