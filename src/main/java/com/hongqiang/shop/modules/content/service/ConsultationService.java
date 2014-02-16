package com.hongqiang.shop.modules.content.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Consultation;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;

public interface ConsultationService {
	public List<Consultation> findList(Member paramMember,
			Product paramProduct, Boolean paramBoolean, Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1);

	public List<Consultation> findList(Member paramMember,
			Product paramProduct, Boolean paramBoolean, Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1, String paramString);

	public List<Consultation> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public Page<Consultation> findPage(Pageable pageable);

	public Consultation find(Long id);

	public Page<Consultation> findPage(Member paramMember,
			Product paramProduct, Boolean paramBoolean, Pageable paramPageable);

	public Long count(Member paramMember, Product paramProduct,
			Boolean paramBoolean);

	public void reply(Consultation paramConsultation1,
			Consultation paramConsultation2);

	public void save(Consultation consultation);

	public Consultation update(Consultation consultation);

	public Consultation update(Consultation consultation,
			String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Consultation consultation);
}