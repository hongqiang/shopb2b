package com.hongqiang.shop.modules.account.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.CartItem;

public interface CartItemService {

	public CartItem find(Long id);

	public Page<CartItem> findPage(Pageable pageable);

	public void save(CartItem cartItem);

	public CartItem update(CartItem cartItem);

	public CartItem update(CartItem cartItem, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(CartItem cartItem);
}
