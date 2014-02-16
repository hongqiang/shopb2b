package com.hongqiang.shop.modules.product.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductNotify;

public abstract interface ProductNotifyService {

	public ProductNotify find(Long id); 

	public void save(ProductNotify productNotify);

	public ProductNotify update(ProductNotify productNotify);

	 public ProductNotify update(ProductNotify brand, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(ProductNotify productNotify);
	
  public boolean exists(Product product, String email);

  public Page<ProductNotify> findPage(Member paramMember, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable);

  public Long count(Member paramMember, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent);
  
  public int send(Long[] paramArrayOfLong);
}