package com.hongqiang.shop.website.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.Ad;

public interface AdService {

	public Ad find(Long id);

	public Page<Ad> findPage(Pageable pageable);

	public void save(Ad ad);

	public Ad update(Ad ad);

	public Ad update(Ad ad, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Ad ad);
}