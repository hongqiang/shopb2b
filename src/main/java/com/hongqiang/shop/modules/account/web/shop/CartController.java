package com.hongqiang.shop.modules.account.web.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.CookieUtils;
import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CartItemService;
import com.hongqiang.shop.modules.account.service.CartService;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.CartItem;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopCartController")
@RequestMapping({ "${frontPath}/cart" })
public class CartController extends BaseController {
	// 7*24*60*60
	private final int COOKIE_TIME = 604800;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	@ResponseBody
	public Message add(Long id, Integer quantity, HttpServletRequest request,
			HttpServletResponse response) {
		if ((quantity == null) || (quantity.intValue() < 1))
			return SHOP_ERROR;
		Product localProduct = (Product) this.productService.find(id);
		if (localProduct == null)
			return Message.warn("shop.cart.productNotExsit", new Object[0]);
		if (!localProduct.getIsMarketable().booleanValue())
			return Message
					.warn("shop.cart.productNotMarketable", new Object[0]);
		if (localProduct.getIsGift().booleanValue())
			return Message.warn("shop.cart.notForSale", new Object[0]);
		Cart localCart = this.cartService.getCurrent();
		Member localMember = this.memberService.getCurrent();
		if (localCart == null) {
			localCart = new Cart();
			localCart
					.setKey(UUID.randomUUID().toString()
							+ DigestUtils.md5Hex(RandomStringUtils
									.randomAlphabetic(30)));
			localCart.setMember(localMember);
			this.cartService.save(localCart);
		}
		if ((Cart.MAX_PRODUCT_COUNT != null)
				&& (localCart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT
						.intValue()))
			return Message.warn("shop.cart.addCountNotAllowed",
					new Object[] { Cart.MAX_PRODUCT_COUNT });
		CartItem localCartItem;
		if (localCart.contains(localProduct)) {
			localCartItem = localCart.getCartItem(localProduct);
			if ((CartItem.MAX_QUANTITY != null)
					&& (localCartItem.getQuantity().intValue()
							+ quantity.intValue() > CartItem.MAX_QUANTITY
								.intValue()))
				return Message.warn("shop.cart.maxCartItemQuantity",
						new Object[] { CartItem.MAX_QUANTITY });
			if ((localProduct.getStock() != null)
					&& (localCartItem.getQuantity().intValue()
							+ quantity.intValue() > localProduct
							.getAvailableStock().intValue()))
				return Message.warn("shop.cart.productLowStock", new Object[0]);
			localCartItem.add(quantity.intValue());
			this.cartItemService.update(localCartItem);
		} else {
			if ((CartItem.MAX_QUANTITY != null)
					&& (quantity.intValue() > CartItem.MAX_QUANTITY.intValue()))
				return Message.warn("shop.cart.maxCartItemQuantity",
						new Object[] { CartItem.MAX_QUANTITY });
			if ((localProduct.getStock() != null)
					&& (quantity.intValue() > localProduct.getAvailableStock()
							.intValue()))
				return Message.warn("shop.cart.productLowStock", new Object[0]);
			localCartItem = new CartItem();
			localCartItem.setQuantity(quantity);
			localCartItem.setProduct(localProduct);
			localCartItem.setCart(localCart);
			this.cartItemService.save(localCartItem);
			localCart.getCartItems().add(localCartItem);
		}
		if (localMember == null) {
			CookieUtils.setCookie(request, response, "cartId", localCart
					.getId().toString(), Integer.valueOf(COOKIE_TIME));
			CookieUtils.setCookie(request, response, "cartKey",
					localCart.getKey(), Integer.valueOf(COOKIE_TIME));
		}
		return Message.success("shop.cart.addSuccess",
				new Object[] { Integer.valueOf(localCart.getQuantity()),
						addMessage(localCart.getAmount(), true, false) });
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("cart", this.cartService.getCurrent());
		return "/shop/cart/list";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> edit(Long id, Integer quantity) {
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		if ((quantity == null) || (quantity.intValue() < 1)) {
			localHashMap.put("message", SHOP_ERROR);
			return localHashMap;
		}
		Cart localCart = this.cartService.getCurrent();
		if ((localCart == null) || (localCart.isEmpty())) {
			localHashMap.put("message",
					Message.error("shop.cart.notEmpty", new Object[0]));
			return localHashMap;
		}
		CartItem localCartItem = (CartItem) this.cartItemService.find(id);
		Set<CartItem> localSet = localCart.getCartItems();
		if ((localCartItem == null) || (localSet == null)
				|| (!localSet.contains(localCartItem))) {
			localHashMap.put("message",
					Message.error("shop.cart.cartItemNotExsit", new Object[0]));
			return localHashMap;
		}
		if ((CartItem.MAX_QUANTITY != null)
				&& (quantity.intValue() > CartItem.MAX_QUANTITY.intValue())) {
			localHashMap.put("message", Message.warn(
					"shop.cart.maxCartItemQuantity",
					new Object[] { CartItem.MAX_QUANTITY }));
			return localHashMap;
		}
		Product localProduct = localCartItem.getProduct();
		if ((localProduct.getStock() != null)
				&& (quantity.intValue() > localProduct.getAvailableStock()
						.intValue())) {
			localHashMap.put("message",
					Message.warn("shop.cart.productLowStock", new Object[0]));
			return localHashMap;
		}
		localCartItem.setQuantity(quantity);
		this.cartItemService.update(localCartItem);
		localHashMap.put("message", SHOP_SUCCESS);
		localHashMap.put("subtotal", localCartItem.getSubtotal());
		localHashMap.put("isLowStock",
				Boolean.valueOf(localCartItem.getIsLowStock()));
		localHashMap.put("quantity", Integer.valueOf(localCart.getQuantity()));
		localHashMap.put("point", Integer.valueOf(localCart.getPoint()));
		localHashMap.put("amount", localCart.getAmount());
		localHashMap.put("promotions", localCart.getPromotions());
		localHashMap.put("giftItems", localCart.getGiftItems());
		return localHashMap;
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(Long id) {
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		Cart localCart = this.cartService.getCurrent();
		if ((localCart == null) || (localCart.isEmpty())) {
			localHashMap.put("message",
					Message.error("shop.cart.notEmpty", new Object[0]));
			return localHashMap;
		}
		CartItem localCartItem = (CartItem) this.cartItemService.find(id);
		Set<CartItem> localSet = localCart.getCartItems();
		if ((localCartItem == null) || (localSet == null)
				|| (!localSet.contains(localCartItem))) {
			localHashMap.put("message",
					Message.error("shop.cart.cartItemNotExsit", new Object[0]));
			return localHashMap;
		}
		localSet.remove(localCartItem);
		this.cartItemService.delete(localCartItem);
		localHashMap.put("message", SHOP_SUCCESS);
		localHashMap.put("quantity", Integer.valueOf(localCart.getQuantity()));
		localHashMap.put("point", Integer.valueOf(localCart.getPoint()));
		localHashMap.put("amount", localCart.getAmount());
		localHashMap.put("promotions", localCart.getPromotions());
		localHashMap.put("isLowStock",
				Boolean.valueOf(localCart.getIsLowStock()));
		return localHashMap;
	}

	@RequestMapping(value = { "/clear" }, method = RequestMethod.POST)
	@ResponseBody
	public Message clear() {
		Cart localCart = this.cartService.getCurrent();
		this.cartService.delete(localCart);
		return SHOP_SUCCESS;
	}
}