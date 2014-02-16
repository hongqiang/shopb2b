package com.hongqiang.shop.modules.user.web.shop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.CookieUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Member;

@Controller("shopLogoutController")
@RequestMapping({ "${frontPath}" })
public class LogoutController extends BaseController
{
	
  @RequestMapping(value={"/logout"}, method=RequestMethod.GET)
  public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session)
  {
    session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
    CookieUtils.removeCookie(request, response, "username");
    return "redirect:/";
  }
}