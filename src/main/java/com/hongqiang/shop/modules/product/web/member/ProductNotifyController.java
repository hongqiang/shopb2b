package com.hongqiang.shop.modules.product.web.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.ProductNotify;
import com.hongqiang.shop.modules.product.service.ProductNotifyService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberProductNotifyController")
@RequestMapping({"${memberPath}/product_notify"})
public class ProductNotifyController extends BaseController
{
  private static final int PAGE_SIZE = 10;

  @Autowired
  ProductNotifyService productNotifyService;

  @Autowired
  MemberService memberService;

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Integer pageNumber, Model model)
  {
    Member localMember = this.memberService.getCurrent();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_SIZE));
    model.addAttribute("page", this.productNotifyService.findPage(localMember, null, null, null, localPageable));
    return "/shop/member/product_notify/list";
  }


  @RequestMapping({"delete"})
  @ResponseBody
  public Message delete(Long id)
  {
    ProductNotify localProductNotify = (ProductNotify)this.productNotifyService.find(id);
    if (localProductNotify == null)
      return SHOP_ERROR;
    Member localMember = this.memberService.getCurrent();
    if (!localMember.getProductNotifies().contains(localProductNotify))
      return SHOP_ERROR;
    this.productNotifyService.delete(localProductNotify);
    return SHOP_SUCCESS;
  }
}