package com.hongqiang.shop.modules.user.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.Member;

public abstract interface MemberService {

	public boolean usernameExists(String paramString);

	public boolean usernameDisabled(String paramString);

	public boolean emailExists(String paramString);

	public boolean emailUnique(String paramString1, String paramString2);

	public void save(Member paramMember, Admin paramAdmin);

	public void update(Member paramMember, Integer paramInteger,
			BigDecimal paramBigDecimal, String paramString, Admin paramAdmin);

	public Long count();
	
	public Member find(Long id);

	public Page<Member> findPage(Pageable pageable);

	public Member findByUsername(String paramString);

	public List<Member> findListByEmail(String paramString);

	public Page<Object> findPurchasePage(Date paramDate1, Date paramDate2,
			Pageable paramPageable);

	public boolean isAuthenticated();

	public Member getCurrent();

	public String getCurrentUsername();

	public void save(Member member);

	public Member update(Member member);

	public Member update(Member member, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Member member);
}