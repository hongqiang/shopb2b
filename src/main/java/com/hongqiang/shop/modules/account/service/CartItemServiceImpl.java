package com.hongqiang.shop.modules.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.account.dao.CartItemDao;
import com.hongqiang.shop.modules.entity.CartItem;

@Service
public class CartItemServiceImpl extends BaseService implements CartItemService {

	@Autowired
	private CartItemDao cartItemDao;

	@Transactional
	public CartItem find(Long id) {
		return this.cartItemDao.find(id);
	}

	@Transactional
	public Page<CartItem> findPage(Pageable pageable) {
		return this.cartItemDao.findPage(pageable);
	}

	@Transactional
	public void save(CartItem cartItem) {
		this.cartItemDao.persist(cartItem);
	}

	@Transactional
	public CartItem update(CartItem cartItem) {
		return (CartItem) this.cartItemDao.merge(cartItem);
	}

	@Transactional
	public CartItem update(CartItem cartItem, String[] ignoreProperties) {
		return (CartItem) this.cartItemDao.update(cartItem, ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {

		this.cartItemDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.cartItemDao.delete(localSerializable);
	}

	@Transactional
	public void delete(CartItem cartItem) {
		this.cartItemDao.delete(cartItem);
	}
}