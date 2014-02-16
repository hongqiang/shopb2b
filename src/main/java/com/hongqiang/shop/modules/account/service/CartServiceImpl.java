package com.hongqiang.shop.modules.account.service;

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.CookieUtils;
import com.hongqiang.shop.common.utils.Principal;
import com.hongqiang.shop.modules.account.dao.CartDao;
import com.hongqiang.shop.modules.account.dao.CartItemDao;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.CartItem;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.user.dao.MemberDao;

@Service
public class CartServiceImpl extends BaseService implements CartService {

	@Autowired
	private CartDao cartDao;

	@Autowired
	private CartItemDao cartItemDao;

	@Autowired
	private MemberDao memberDao;

	public Cart getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) httpServletRequest.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			Member member = principal != null ? (Member) this.memberDao.find(principal.getId()) : null;
			if (member != null) {
				Cart cart = member.getCart();
				if (cart != null) {
					if (!cart.hasExpired()) {
						if (!DateUtils.isSameDay(cart.getUpdateDate(),new Date())) {
							cart.setUpdateDate(new Date());
							this.cartDao.merge(cart);
						}
						return cart;
					}
					this.cartDao.remove(cart);
				}
			} else {
				String cartIdString = CookieUtils.getCookie(httpServletRequest,"cartId");
				String cartKeyString = CookieUtils.getCookie(httpServletRequest,"cartKey");
				if ((StringUtils.isNotEmpty(cartIdString))
						&& (StringUtils.isNumeric(cartIdString))
						&& (StringUtils.isNotEmpty(cartKeyString))) {
					Cart cart = (Cart) this.cartDao.find(Long.valueOf(cartIdString));
					if ((cart != null) && (cart.getMember() == null)
							&& (StringUtils.equals(cart.getKey(), cartKeyString))) {
						if (!cart.hasExpired()) {
							if (!DateUtils.isSameDay(cart.getUpdateDate(),new Date())) {
								cart.setUpdateDate(new Date());
								this.cartDao.merge(cart);
							}
							return cart;
						}
						this.cartDao.remove(cart);
					}
				}
			}
		}
		return null;
	}

	public void merge(Member member, Cart cart) {
		if ((member != null) && (cart != null) && (cart.getMember() == null)) {
			Cart localCart = member.getCart();
			if (localCart != null) {
				Iterator<CartItem> iterator = cart.getCartItems().iterator();
				while (iterator.hasNext()) {
					CartItem cartItem = (CartItem) iterator.next();
					Product product = cartItem.getProduct();
					if (localCart.contains(product)) {
						if ((Cart.MAX_PRODUCT_COUNT != null)
								&& (localCart.getCartItems().size() > Cart.MAX_PRODUCT_COUNT.intValue()))
							continue;
						CartItem localCartItem = localCart.getCartItem(product);
						localCartItem.add(cartItem.getQuantity().intValue());
						this.cartItemDao.merge(localCartItem);
					} else {
						if ((Cart.MAX_PRODUCT_COUNT != null)
								&& (localCart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT.intValue()))
							continue;
						iterator.remove();
						cartItem.setCart(localCart);
						localCart.getCartItems().add(cartItem);
						this.cartItemDao.merge(cartItem);
					}
				}
				this.cartDao.remove(cart);
			} else {
				member.setCart(cart);
				cart.setMember(member);
				this.cartDao.merge(cart);
			}
		}
	}

	public void evictExpired() {
		this.cartDao.evictExpired();
	}

	@Transactional
	public void save(Cart cart) {
		this.cartDao.persist(cart);
	}

	@Transactional
	public Cart update(Cart cart) {
		return (Cart) this.cartDao.merge(cart);
	}

	@Transactional
	public Cart update(Cart cart, String[] ignoreProperties) {
		return (Cart) this.cartDao.update(cart, ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {
		this.cartDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long id : ids)
				this.cartDao.delete(id);
	}

	@Transactional
	public void delete(Cart cart) {
		this.cartDao.delete(cart);
	}
}