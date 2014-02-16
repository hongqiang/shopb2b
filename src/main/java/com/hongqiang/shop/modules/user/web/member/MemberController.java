package com.hongqiang.shop.modules.user.web.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CouponCodeService;
import com.hongqiang.shop.modules.content.service.ConsultationService;
import com.hongqiang.shop.modules.content.service.MessageService;
import com.hongqiang.shop.modules.content.service.ReviewService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.product.service.ProductNotifyService;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.shipping.service.OrderService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberController")
@RequestMapping({"${memberPath}"})
public class MemberController extends BaseController
{
  private static final int ORDER_INDEX = 6;

  @Autowired
  private MemberService memberService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private CouponCodeService couponCodeService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductNotifyService productNotifyService;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ConsultationService consultationService;

  @RequestMapping(value={"/index"}, method=RequestMethod.GET)
  public String index(Integer pageNumber, ModelMap model)
  {
	  System.out.println("we are in member index");
    Member localMember = this.memberService.getCurrent();
    model.addAttribute("waitingPaymentOrderCount", this.orderService.waitingPaymentCount(localMember));
    model.addAttribute("waitingShippingOrderCount", this.orderService.waitingShippingCount(localMember));
    model.addAttribute("messageCount", this.messageService.count(localMember, Boolean.valueOf(false)));
    model.addAttribute("couponCodeCount", this.couponCodeService.count(null, localMember, null, Boolean.valueOf(false), Boolean.valueOf(false)));
    model.addAttribute("favoriteCount", this.productService.count(localMember, null, null, null, null, null, null));
    model.addAttribute("productNotifyCount", this.productNotifyService.count(localMember, null, null, null));
    model.addAttribute("reviewCount", this.reviewService.count(localMember, null, null, null));
    model.addAttribute("consultationCount", this.consultationService.count(localMember, null, null));
    model.addAttribute("newOrders", this.orderService.findList(localMember, Integer.valueOf(ORDER_INDEX), null, null));
    return "shop/member/index";
  }
}