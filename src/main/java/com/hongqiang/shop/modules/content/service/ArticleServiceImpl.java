package com.hongqiang.shop.modules.content.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
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
import com.hongqiang.shop.modules.content.dao.ArticleDao;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.entity.Tag;
import com.hongqiang.shop.modules.util.service.TemplateService;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService,
		DisposableBean, ServletContextAware {
	private long systemTime = System.currentTimeMillis();
	public static final String HITS_CACHE_NAME = "articleHits";
	public static final int HITS_CACHE_INTERVAL = 600000;
	private ServletContext servletContext;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private ArticleDao articleDao;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Transactional(readOnly = true)
	public List<Article> findList(ArticleCategory articleCategory,
			List<Tag> tags, Integer count, List<Filter> filters,
			List<Order> orders) {
		return this.articleDao.findList(articleCategory, tags, count, filters,
				orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "article" })
	public List<Article> findList(ArticleCategory articleCategory,
			List<Tag> tags, Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion) {
		return this.articleDao.findList(articleCategory, tags, count, filters,
				orders);
	}

	@Transactional(readOnly = true)
	public List<Article> findList(ArticleCategory articleCategory,
			Date beginDate, Date endDate, Integer first, Integer count) {
		return this.articleDao.findList(articleCategory, beginDate, endDate,
				first, count);
	}

	@Transactional(readOnly = true)
	public Page<Article> findPage(ArticleCategory articleCategory,
			List<Tag> tags, Pageable pageable) {
		return this.articleDao.findPage(articleCategory, tags, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Article> findPage(Pageable pageable) {
		return this.articleDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	public Article find(Long id) {
		return this.articleDao.find(id);
	}

	public long viewHits(Long id){
		System.out.println("id="+id);
		System.out.println("idString="+id.toString());
		Long hits = (Long)CacheUtils.get(HITS_CACHE_NAME, id.toString());
		if (hits == null) {
			Article localArticle = (Article) this.articleDao.find(id);
			if (localArticle == null)
				return 0L;
			hits = localArticle.getHits();
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

	private void destroyCache(){
		@SuppressWarnings("unchecked")
		List<Long> localList = (List<Long>) CacheUtils.getKeys(HITS_CACHE_NAME);
		Iterator<Long> localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			Long id = (Long) localIterator.next();
			Article localArticle = (Article) this.articleDao.find(id);
			if (localArticle == null)
				continue;
			Long hits = (Long)CacheUtils.get(HITS_CACHE_NAME, id.toString());
			localArticle.setHits(hits);
			this.articleDao.merge(localArticle);
		}
	}
	
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void save(Article article) {
		Assert.notNull(article);
		this.articleDao.persist(article);
		this.articleDao.flush();
		build(article);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article) {
		Assert.notNull(article);
		Article localArticle = (Article) this.articleDao.merge(article);
		this.articleDao.flush();
		build(localArticle);
		return localArticle;
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article, String[] ignoreProperties) {
		return (Article) this.articleDao.update(article, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long id) {
		this.articleDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.articleDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Article article) {
		if (article != null)
			deleteStaticArticle(article);
		this.articleDao.delete(article);
	}
	
	@Transactional(readOnly=true)
	public int build(Article article) {
		Assert.notNull(article);
		deleteStaticArticle(article);
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
	
	@Transactional(readOnly=true)
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
	
	@Transactional(readOnly=true)
	public int deleteStaticArticle(Article article) {
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
	
	@Transactional(readOnly=true)
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