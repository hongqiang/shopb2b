package com.hongqiang.shop.modules.shipping.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.modules.entity.Shipping;
import com.hongqiang.shop.modules.shipping.dao.ShippingDao;

@Service
public class ShippingServiceImpl extends BaseService implements ShippingService {

	@Autowired
	private ShippingDao shippingDao;

	@Transactional(readOnly = true)
	public Shipping findBySn(String sn) {
		return this.shippingDao.findBySn(sn);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Cacheable({ "shipping" })
	public Map<String, Object> query(Shipping shipping) {
		Setting localSetting = SettingUtils.get();
		Map<String, Object> localObject = new HashMap<String, Object>();
		if ((shipping != null)
				&& (StringUtils.isNotEmpty(localSetting.getKuaidi100Key()))
				&& (StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()))
				&& (StringUtils.isNotEmpty(shipping.getTrackingNo())))
			try {
				ObjectMapper localObjectMapper = new ObjectMapper();
				URL localURL = new URL("http://api.kuaidi100.com/api?id="
						+ localSetting.getKuaidi100Key() + "&com="
						+ shipping.getDeliveryCorpCode() + "&nu="
						+ shipping.getTrackingNo() + "&show=0&muti=1&order=asc");
				localObject = (Map<String, Object>) localObjectMapper
						.readValue(localURL, Map.class);
			} catch (Exception localException1) {
				localException1.printStackTrace();
			}
		return localObject;
	}

	@Transactional
	public Shipping find(Long id) {
		return this.shippingDao.find(id);
	}

	@Transactional
	public Page<Shipping> findPage(Pageable pageable) {
		return this.shippingDao.findPage(pageable);
	}

	@Transactional
	public void save(Shipping shipping) {
		this.shippingDao.persist(shipping);
	}

	@Transactional
	public Shipping update(Shipping shipping) {
		return (Shipping) this.shippingDao.merge(shipping);
	}

	@Transactional
	public Shipping update(Shipping shipping, String[] ignoreProperties) {
		return (Shipping) this.shippingDao.update(shipping, ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {

		this.shippingDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.shippingDao.delete(localSerializable);
	}

	@Transactional
	public void delete(Shipping shipping) {
		this.shippingDao.delete(shipping);
	}
}