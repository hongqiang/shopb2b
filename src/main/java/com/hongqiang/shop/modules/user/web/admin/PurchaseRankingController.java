package com.hongqiang.shop.modules.user.web.admin;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("adminPurchaseRankingController")
@RequestMapping({"${adminPath}/purchase_ranking"})
public class PurchaseRankingController extends BaseController
{

  @Resource(name="memberServiceImpl")
  private MemberService memberService;

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Date beginDate, Date endDate, Pageable pageable, Model model)
  {
    model.addAttribute("beginDate", beginDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("page", this.memberService.findPurchasePage(beginDate, endDate, pageable));
    return "/admin/purchase_ranking/list";
  }
}