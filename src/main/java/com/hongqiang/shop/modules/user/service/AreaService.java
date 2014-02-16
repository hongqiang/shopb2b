package com.hongqiang.shop.modules.user.service;

import java.util.List;

import com.hongqiang.shop.modules.entity.Area;

public interface AreaService {
	public List<Area> findRoots();

	public List<Area> findRoots(Integer paramInteger);

	public Area find(Long id);

	public void save(Area area);

	public Area update(Area area);

	public Area update(Area area, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Area area);
}