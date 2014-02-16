package com.hongqiang.shop.modules.content.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Consultation;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;

public interface ConsultationDao extends ConsultationDaoCustom, CrudRepository<Consultation, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ConsultationDaoCustom extends BaseDao<Consultation,Long> {

  public List<Consultation> findList(Member paramMember, Product paramProduct, Boolean paramBoolean, Integer paramInteger, List<Filter> paramList, List<Order> paramList1);

  public Page<Consultation> findPage(Member paramMember, Product paramProduct, Boolean paramBoolean, Pageable paramPageable);

  public Long count(Member paramMember, Product paramProduct, Boolean paramBoolean);
  
  public List<Consultation> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders); 
  
  public Page<Consultation> findPage(Pageable pageable);
}
