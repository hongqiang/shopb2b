package com.hongqiang.shop.modules.content.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
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
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.content.dao.ReviewDao;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Review;
import com.hongqiang.shop.modules.product.dao.ProductDao;
import com.hongqiang.shop.modules.util.service.TemplateService;

@Service
public class ReviewServiceImpl extends BaseService implements ReviewService, ServletContextAware {

	@Autowired
	private ReviewDao reviewDao;

	private ServletContext servletContext;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private TemplateService templateService;

//	 @Autowired
//	 private StaticService staticService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Transactional(readOnly = true)
	public List<Review> findList(Member member, Long productId,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders) {
		Product product = this.productDao.find(productId);
		return this.reviewDao.findList(member, product, type, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "review" })
	public List<Review> findList(Member member, Long productId,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders, String cacheRegion) {
		Product product = this.productDao.find(productId);
		return this.reviewDao.findList(member, product, type, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Review> findList(Member member, Product product,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders) {
		return this.reviewDao.findList(member, product, type, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "review" })
	public List<Review> findList(Member member, Product product,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders, String cacheRegion) {
		return this.reviewDao.findList(member, product, type, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Review> findPage(Member member, Product product,
			Review.Type type, Boolean isShow, Pageable pageable) {
		return this.reviewDao.findPage(member, product, type, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Product product, Review.Type type,
			Boolean isShow) {
		return this.reviewDao.count(member, product, type, isShow);
	}

	@Transactional(readOnly = true)
	public boolean isReviewed(Member member, Product product) {
		return this.reviewDao.isReviewed(member, product);
	}

	@Transactional(readOnly = true)
	public Review find(Long id) {
		return this.reviewDao.find(id);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void save(Review review) {
		this.reviewDao.persist(review);
		Product localProduct = review.getProduct();
		if (localProduct != null) {
			this.reviewDao.flush();
			long l1 = this.reviewDao.calculateTotalScore(localProduct);
			long l2 = this.reviewDao.calculateScoreCount(localProduct);
			localProduct.setTotalScore(Long.valueOf(l1));
			localProduct.setScoreCount(Long.valueOf(l2));
			this.productDao.merge(localProduct);
			this.reviewDao.flush();
//			 this.staticService.build(localProduct);
			build(localProduct);
		}
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Review update(Review review) {
		Review localReview = (Review) this.reviewDao.merge(review);
		Product localProduct = localReview.getProduct();
		if (localProduct != null) {
			this.reviewDao.flush();
			long l1 = this.reviewDao.calculateTotalScore(localProduct);
			long l2 = this.reviewDao.calculateScoreCount(localProduct);
			localProduct.setTotalScore(Long.valueOf(l1));
			localProduct.setScoreCount(Long.valueOf(l2));
			this.productDao.merge(localProduct);
			this.reviewDao.flush();
//			 this.staticService.build(localProduct);
			 build(localProduct);
		}
		return localReview;
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Review update(Review review, String[] ignoreProperties) {
		return (Review) this.reviewDao.update(review, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long id) {
		this.reviewDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.reviewDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Review review) {
		if (review != null) {
			this.reviewDao.delete(review);
			Product localProduct = review.getProduct();
			if (localProduct != null) {
				this.reviewDao.flush();
				long l1 = this.reviewDao.calculateTotalScore(localProduct);
				long l2 = this.reviewDao.calculateScoreCount(localProduct);
				localProduct.setTotalScore(Long.valueOf(l1));
				localProduct.setScoreCount(Long.valueOf(l2));
				this.productDao.merge(localProduct);
				this.reviewDao.flush();
//				 this.staticService.build(localProduct);
				 build(localProduct);
			}
		}
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