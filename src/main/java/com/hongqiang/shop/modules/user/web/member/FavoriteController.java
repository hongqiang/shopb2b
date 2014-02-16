package com.hongqiang.shop.modules.user.web.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberFavoriteController")
@RequestMapping({"${memberPath}/favorite"})
public class FavoriteController extends BaseController
{
  private static final int PAGE_SIZE = 10;

  @Autowired
  private MemberService memberService;

  @Autowired
  private ProductService productService;

  @RequestMapping(value={"/add"}, method=RequestMethod.POST)
  @ResponseBody
  public Message add(Long id)
  {
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      return SHOP_ERROR;
    Member localMember = this.memberService.getCurrent();
    if (localMember.getFavoriteProducts().contains(localProduct))
      return Message.warn("shop.member.favorite.exist", new Object[0]);
    if ((Member.MAX_FAVORITE_COUNT != null) && (localMember.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT.intValue()))
      return Message.warn("shop.member.favorite.addCountNotAllowed", new Object[] { Member.MAX_FAVORITE_COUNT });
    localMember.getFavoriteProducts().add(localProduct);
    this.memberService.update(localMember);
    return Message.success("shop.member.favorite.success", new Object[0]);
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Integer pageNumber, ModelMap model)
  {
    Member localMember = this.memberService.getCurrent();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_SIZE));
    model.addAttribute("page", this.productService.findPage(localMember, localPageable));
    return "shop/member/favorite/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long id)
  {
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      return SHOP_ERROR;
    Member localMember = this.memberService.getCurrent();
    if (!localMember.getFavoriteProducts().contains(localProduct))
      return SHOP_ERROR;
    localMember.getFavoriteProducts().remove(localProduct);
    this.memberService.update(localMember);
    return SHOP_SUCCESS;
  }
}