package com.hongqiang.shop.modules.account.service;

import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.Member;

public interface CartService {
	public Cart getCurrent();

	public void merge(Member paramMember, Cart paramCart);

	public void evictExpired();

	public void save(Cart cart);

	public Cart update(Cart cart);

	public Cart update(Cart cart, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Cart cart);
}