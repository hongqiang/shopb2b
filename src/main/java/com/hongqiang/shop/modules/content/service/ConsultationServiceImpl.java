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
import com.hongqiang.shop.modules.content.dao.ConsultationDao;
import com.hongqiang.shop.modules.entity.Consultation;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.util.service.TemplateService;

@Service
public class ConsultationServiceImpl extends BaseService implements
		ConsultationService, ServletContextAware {

	private ServletContext servletContext;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private ConsultationDao consultationDao;

	// @Autowired
	// private StaticService staticService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Transactional(readOnly = true)
	public List<Consultation> findList(Member member, Product product,
			Boolean isShow, Integer count, List<Filter> filters,
			List<Order> orders) {
		return this.consultationDao.findList(member, product, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "consultation" })
	public List<Consultation> findList(Member member, Product product,
			Boolean isShow, Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion) {
		return this.consultationDao.findList(member, product, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "consultation" })
	public List<Consultation> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		return this.consultationDao.findList(first, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "consultation" })
	public Page<Consultation> findPage(Pageable pageable) {
		return this.consultationDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "consultation" })
	public Consultation find(Long id) {
		return this.consultationDao.find(id);
	}

	@Transactional(readOnly = true)
	public Page<Consultation> findPage(Member member, Product product,
			Boolean isShow, Pageable pageable) {
		return this.consultationDao.findPage(member, product, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Product product, Boolean isShow) {
		return this.consultationDao.count(member, product, isShow);
	}

	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void reply(Consultation consultation, Consultation replyConsultation) {
		if ((consultation == null) || (replyConsultation == null))
			return;
		consultation.setIsShow(Boolean.valueOf(true));
		this.consultationDao.merge(consultation);
		replyConsultation.setIsShow(Boolean.valueOf(true));
		replyConsultation.setProduct(consultation.getProduct());
		replyConsultation.setForConsultation(consultation);
		this.consultationDao.persist(replyConsultation);
		Product localProduct = consultation.getProduct();
		if (localProduct != null) {
			this.consultationDao.flush();
			// this.staticService.build(localProduct);
			build(localProduct);
		}
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void save(Consultation consultation) {
		this.consultationDao.persist(consultation);
		Product localProduct = consultation.getProduct();
		if (localProduct != null) {
			this.consultationDao.flush();
			// this.staticService.build(localProduct);
			build(localProduct);
		}
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Consultation update(Consultation consultation) {
		Consultation localConsultation = (Consultation) this.consultationDao
				.merge(consultation);
		Product localProduct = localConsultation.getProduct();
		if (localProduct != null) {
			this.consultationDao.flush();
			// this.staticService.build(localProduct);
			build(localProduct);
		}
		return localConsultation;
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Consultation update(Consultation consultation,
			String[] ignoreProperties) {
		return (Consultation) this.consultationDao.update(consultation,
				ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long id) {
		this.consultationDao.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.consultationDao.delete(localSerializable);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Consultation consultation) {
		if (consultation != null) {
			this.consultationDao.delete(consultation);
			Product localProduct = consultation.getProduct();
			if (localProduct != null) {
				this.consultationDao.flush();
				// this.staticService.build(localProduct);
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