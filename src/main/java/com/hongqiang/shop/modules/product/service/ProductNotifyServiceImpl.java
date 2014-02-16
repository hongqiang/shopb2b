package com.hongqiang.shop.modules.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductNotify;
import com.hongqiang.shop.modules.product.dao.ProductNotifyDao;
import com.hongqiang.shop.website.service.MailService;

@Service
public class ProductNotifyServiceImpl extends BaseService implements
		ProductNotifyService {

	@Autowired
	ProductNotifyDao productNotifyDao;

	@Autowired
	MailService mailService;

	@Transactional
	public ProductNotify find(Long id) {
		return this.productNotifyDao.findById(id);
	}

	@Transactional
	public void save(ProductNotify productNotify) {
		this.productNotifyDao.persist(productNotify);
	}

	@Transactional
	public ProductNotify update(ProductNotify productNotify) {
		return (ProductNotify) this.productNotifyDao.merge(productNotify);
	}

	public ProductNotify update(ProductNotify productNotify,String[] ignoreProperties) {
		return (ProductNotify) this.productNotifyDao.update(productNotify,
				ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {
		this.productNotifyDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.productNotifyDao.delete(localSerializable);
	}

	@Transactional
	public void delete(ProductNotify productNotify) {
		this.productNotifyDao.delete(productNotify);
	}

	@Transactional(readOnly = true)
	public boolean exists(Product product, String email) {
		return this.productNotifyDao.exists(product, email);
	}

	@Transactional(readOnly = true)
	public Page<ProductNotify> findPage(Member member, Boolean isMarketable,
			Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
		return this.productNotifyDao.findPage(member, isMarketable,
				isOutOfStock, hasSent, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Boolean isMarketable,
			Boolean isOutOfStock, Boolean hasSent) {
		return this.productNotifyDao.count(member, isMarketable, isOutOfStock,
				hasSent);
	}

	public int send(Long[] ids) {
		int sendCount = 0;
		if (ids != null) {
			for (Long id : ids) {
				ProductNotify localProductNotify = this.find(id);
				if (localProductNotify == null)
					continue;
				sendCount++;
				this.mailService.sendProductNotifyMail(localProductNotify);
				localProductNotify.setHasSent(Boolean.valueOf(true));
				this.productNotifyDao.merge(localProductNotify);
			}
		}
		return sendCount;
	}
}