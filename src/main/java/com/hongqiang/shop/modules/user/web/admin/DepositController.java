package com.hongqiang.shop.modules.user.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.DepositService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("adminDepositController")
@RequestMapping({"${adminPath}/deposit"})
public class DepositController extends BaseController
{

  @Autowired
  private DepositService depositService;

  @Autowired
  private MemberService memberService;

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Long memberId, Pageable pageable, ModelMap model)
  {
    Member localMember = (Member)this.memberService.find(memberId);
    if (localMember != null)
    {
      model.addAttribute("member", localMember);
      model.addAttribute("page", this.depositService.findPage(localMember, pageable));
    }
    else
    {
      model.addAttribute("page", this.depositService.findPage(pageable));
    }
    return "/admin/deposit/list";
  }
}