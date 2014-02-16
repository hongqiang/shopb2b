package com.hongqiang.shop.modules.user.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.MemberAttribute;

public interface MemberAttributeService {

	public Integer findUnusedPropertyIndex();

	public List<MemberAttribute> findList();

	public List<MemberAttribute> findList(String paramString);

	public List<MemberAttribute> findAll();

	public MemberAttribute find(Long id);

	public Page<MemberAttribute> findPage(Pageable pageable);

	public long count();

	public void save(MemberAttribute memberAttribute);

	public MemberAttribute update(MemberAttribute memberAttribute);

	public MemberAttribute update(MemberAttribute brand,
			String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(MemberAttribute memberAttribute);

}