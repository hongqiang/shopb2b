package com.hongqiang.shop.modules.product.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductNotify;

public interface ProductNotifyDao extends  ProductNotifyDaoCustom, CrudRepository<ProductNotify, Long> {

	public ProductNotify findById(Long id);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ProductNotifyDaoCustom extends BaseDao<ProductNotify,Long> {

	public boolean exists(Product paramProduct, String paramString);

  public Page<ProductNotify> findPage(Member paramMember, Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3, Pageable paramPageable);

  public Long count(Member paramMember, Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3);

}
  
