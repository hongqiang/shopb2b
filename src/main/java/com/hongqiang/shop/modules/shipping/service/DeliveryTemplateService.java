package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.DeliveryTemplate;

public interface DeliveryTemplateService {

	public DeliveryTemplate findDefault();

	public DeliveryTemplate find(Long id);

	public List<DeliveryTemplate> findAll();

	public Page<DeliveryTemplate> findPage(Pageable pageable);

	public void save(DeliveryTemplate deliveryTemplate);

	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate);

	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate,
			String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(DeliveryTemplate deliveryTemplate);
}