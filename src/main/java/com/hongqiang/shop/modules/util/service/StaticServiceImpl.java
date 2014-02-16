package com.hongqiang.shop.modules.util.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.account.dao.PromotionDao;
import com.hongqiang.shop.modules.content.dao.ArticleDao;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.product.dao.BrandDao;
import com.hongqiang.shop.modules.product.dao.ProductDao;

@Lazy(true)
@Service
public class StaticServiceImpl implements StaticService, ServletContextAware {
	private static final Integer STATIC_SIZE = Integer.valueOf(40000);
	private ServletContext servletContext;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private PromotionDao promotionDao;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Transactional(readOnly = true)
	public int build(String templatePath, String staticPath,
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
	public int build(String templatePath, String staticPath) {
		return build(templatePath, staticPath, null);
	}

	@Transactional(readOnly = true)
	public int build(Article article) {
		Assert.notNull(article);
		delete(article);
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("articleContent");
		int i = 0;
		if (article.getIsPublication().booleanValue()) {
			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("article", article);
			for (int j = 1; j <= article.getTotalPages(); j++) {
				article.setPageNumber(Integer.valueOf(j));
				i += build(localTemplate.getTemplatePath(), article.getPath(),
						localHashMap);
			}
			article.setPageNumber(null);
		}
		return i;
	}

	@Transactional(readOnly = true)
	public int build(Product product) {
		Assert.notNull(product);
		delete(product);
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
	public int buildIndex() {
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("index");
		return build(localTemplate.getTemplatePath(),
				localTemplate.getStaticPath());
	}

	@Transactional(readOnly = true)
	public int buildSitemap() {
		int i = 0;
		int baseIndex = 0;
		final int ADDITIONAL = 10000;
		com.hongqiang.shop.modules.utils.Template localTemplate1 = this.templateService
				.get("sitemapIndex");
		com.hongqiang.shop.modules.utils.Template localTemplate2 = this.templateService
				.get("sitemap");
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		ArrayList<String> localArrayList = new ArrayList<String>();
		int indexArticle = baseIndex*ADDITIONAL;
		int firstArticleCount = 0;
		int count = STATIC_SIZE.intValue();
		String templatePath = localTemplate2.getTemplatePath();
		String staticPath;
		while (true) {
			try {
				localHashMap.put("index", Integer.valueOf(indexArticle));
				staticPath = FreeMarkers.renderString(
						localTemplate2.getStaticPath(), localHashMap);
				List<Article> articles = this.articleDao.findList(
						Integer.valueOf(firstArticleCount),
						Integer.valueOf(count), null, null);
				localHashMap.put("articles", articles);
				i += build(templatePath, staticPath, localHashMap);
				this.articleDao.clear();
				this.articleDao.flush();
				localArrayList.add(staticPath);
				localHashMap.clear();
				if (articles.size() < count) {
					break;
				}
				firstArticleCount += articles.size();
				indexArticle++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int indexProduct = (++baseIndex)*ADDITIONAL;
		int firstProductCount = 0;
		while (true) {
			try {
				localHashMap.put("index", Integer.valueOf(indexProduct));
				staticPath = FreeMarkers.renderString(
						localTemplate2.getStaticPath(), localHashMap);
				List<Product> products = this.productDao.findList(
						Integer.valueOf(firstProductCount),
						Integer.valueOf(count), null, null);
				localHashMap.put("products", products);
				i += build(templatePath, staticPath, localHashMap);
				this.productDao.clear();
				this.productDao.flush();
				localArrayList.add(staticPath);
				localHashMap.clear();
				if (products.size() < count) {
					break;
				}
				firstProductCount += products.size();
				indexProduct++;
				}catch (Exception e) {
				e.printStackTrace();
				}
		}
		int indexBrand = (++baseIndex)*ADDITIONAL;
		int firstBrandCount = 0;
		while (true) {
			try {
				localHashMap.put("index", Integer.valueOf(indexBrand));
				staticPath = FreeMarkers.renderString(
						localTemplate2.getStaticPath(), localHashMap);
				List<Brand> brands = this.brandDao.findList(
						Integer.valueOf(firstBrandCount),
						Integer.valueOf(count), null, null);
				localHashMap.put("brands", brands);
				i += build(templatePath, staticPath, localHashMap);
				this.brandDao.clear();
				this.brandDao.flush();
				localArrayList.add(staticPath);
				localHashMap.clear();
				if (brands.size() < count) {
					break;
				}
				firstBrandCount += brands.size();
				indexBrand++;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		int indexPromotion = (++baseIndex)*ADDITIONAL;
		int firstPromotionCount = 0;
		while (true) {
			try {
				localHashMap.put("index", Integer.valueOf(indexPromotion));
				staticPath = FreeMarkers.renderString(
						localTemplate2.getStaticPath(), localHashMap);
				List<Promotion> promotions = this.promotionDao.findList(
						Integer.valueOf(firstPromotionCount),
						Integer.valueOf(count), null, null);
				localHashMap.put("promotions", promotions);
				i += build(templatePath, staticPath, localHashMap);
				this.promotionDao.clear();
				this.promotionDao.flush();
				localArrayList.add(staticPath);
				localHashMap.clear();
				if (promotions.size() < count) {
					break;
				}
				firstPromotionCount += promotions.size();
				indexPromotion++;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		localHashMap.put("staticPaths", localArrayList);
		i += build(localTemplate1.getTemplatePath(),
				localTemplate1.getStaticPath(), localHashMap);
		localHashMap.clear();
		return i;
	}

	@Transactional(readOnly = true)
	public int buildOther() {
		int i = 0;
		com.hongqiang.shop.modules.utils.Template localTemplate1 = this.templateService
				.get("shopCommonJs");
		com.hongqiang.shop.modules.utils.Template localTemplate2 = this.templateService
				.get("adminCommonJs");
		i += build(localTemplate1.getTemplatePath(),
				localTemplate1.getStaticPath());
		i += build(localTemplate2.getTemplatePath(),
				localTemplate2.getStaticPath());
		return i;
	}

	@Transactional(readOnly = true)
	public int buildAll() {
		int pageCount = 20;
		int i = 0;
		for (int j = 0; j < this.articleDao.count(new Filter[0]); j += pageCount) {
			List<Article> articles = this.articleDao.findList(
					Integer.valueOf(j), Integer.valueOf(pageCount), null, null);
			Iterator<Article> articleIterator = articles.iterator();
			while (articleIterator.hasNext()) {
				Article article = (Article) articleIterator.next();
				i += build(article);
			}
			this.articleDao.clear();
		}
		for (int j = 0; j < this.productDao.count(new Filter[0]); j += pageCount) {
			List<Product> products = this.productDao.findList(
					Integer.valueOf(j), Integer.valueOf(pageCount), null, null);
			Iterator<Product> productIterator = products.iterator();
			while (productIterator.hasNext()) {
				Product product = (Product) productIterator.next();
				i += build(product);
			}
			this.productDao.clear();
		}
		buildIndex();
		buildSitemap();
		buildOther();
		return i;
	}

	@Transactional(readOnly = true)
	public int delete(String staticPath) {
		Assert.hasText(staticPath);
		File localFile = new File(this.servletContext.getRealPath(staticPath));
		if (localFile.exists()) {
			localFile.delete();
			return 1;
		}
		return 0;
	}

	@Transactional(readOnly = true)
	public int delete(Article article) {
		Assert.notNull(article);
		int i = 0;
		for (int j = 1; j <= article.getTotalPages() + 1000; j++) {
			article.setPageNumber(Integer.valueOf(j));
			int k = delete(article.getPath());
			if (k < 1)
				break;
			i += k;
		}
		article.setPageNumber(null);
		return i;
	}

	@Transactional(readOnly = true)
	public int delete(Product product) {
		Assert.notNull(product);
		return delete(product.getPath());
	}

	@Transactional(readOnly = true)
	public int deleteIndex() {
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("index");
		return delete(localTemplate.getStaticPath());
	}

	@Transactional(readOnly = true)
	public int deleteOther() {
		int i = 0;
		com.hongqiang.shop.modules.utils.Template localTemplate1 = this.templateService
				.get("shopCommonJs");
		com.hongqiang.shop.modules.utils.Template localTemplate2 = this.templateService
				.get("adminCommonJs");
		i += delete(localTemplate1.getStaticPath());
		i += delete(localTemplate2.getStaticPath());
		return i;
	}
}