package com.hongqiang.shop.modules.user.web.member;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberPasswordController")
@RequestMapping({"${memberPath}/password"})
public class PasswordController extends BaseController
{

  @Autowired
  private MemberService memberService;

  @RequestMapping(value={"/check_current_password"}, method=RequestMethod.GET)
  @ResponseBody
  public boolean checkCurrentPassword(String currentPassword)
  {
    if (StringUtils.isEmpty(currentPassword))
      return false;
    Member localMember = this.memberService.getCurrent();
    return StringUtils.equals(DigestUtils.md5Hex(currentPassword), localMember.getPassword());
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit()
  {
    return "shop/member/password/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(String currentPassword, String password, HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
    if ((StringUtils.isEmpty(password)) || (StringUtils.isEmpty(currentPassword)))
      return SHOP_ERROR_PAGE;
    if (!beanValidator(Member.class, "password", password, new Class[0]))
      return SHOP_ERROR_PAGE;
    Setting localSetting = SettingUtils.get();
    if ((password.length() < localSetting.getPasswordMinLength().intValue()) || (password.length() > localSetting.getPasswordMaxLength().intValue()))
      return SHOP_ERROR_PAGE;
    Member localMember = this.memberService.getCurrent();
    if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), localMember.getPassword()))
      return SHOP_ERROR_PAGE;
    localMember.setPassword(DigestUtils.md5Hex(password));
    this.memberService.update(localMember);
    addMessage(redirectAttributes, SHOP_SUCCESS);
    return "redirect:edit.jhtml";
  }
}