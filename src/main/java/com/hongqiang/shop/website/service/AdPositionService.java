package com.hongqiang.shop.website.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.AdPosition;

public abstract interface AdPositionService {
	public AdPosition find(Long paramLong);

	public AdPosition find(Long paramLong, String paramString);

	public List<AdPosition> findAll();

	public Page<AdPosition> findPage(Pageable pageable);

	public void save(AdPosition adPosition);

	public AdPosition update(AdPosition adPosition);

	public AdPosition update(AdPosition adPosition, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(AdPosition adPosition);
}