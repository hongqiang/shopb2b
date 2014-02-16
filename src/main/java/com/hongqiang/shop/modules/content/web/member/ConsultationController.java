package com.hongqiang.shop.modules.content.web.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.ConsultationService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberConsultationController")
@RequestMapping({"${memberPath}/consultation"})
public class ConsultationController extends BaseController
{
  private static final int PAGE_SIZE = 10;

  @Autowired
  private MemberService memberService;

  @Autowired
  private ConsultationService consultationService;

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Integer pageNumber, ModelMap model)
  {
    Member localMember = this.memberService.getCurrent();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_SIZE));
    model.addAttribute("page", this.consultationService.findPage(localMember, null, null, localPageable));
    return "shop/member/consultation/list";
  }
}