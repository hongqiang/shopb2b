package com.hongqiang.shop.website.web.admin;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.FriendLink;
import com.hongqiang.shop.website.service.FriendLinkService;

@Controller("adminFriendLinkController")
@RequestMapping({"${adminPath}/friend_link"})
public class FriendLinkController extends BaseController
{

  @Autowired
  private FriendLinkService friendLinkService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("types", FriendLink.Type.values());
    return "/admin/friend_link/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(FriendLink friendLink, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,friendLink, new Class[0]))
      return ERROR_PAGE;
    if (friendLink.getType() == FriendLink.Type.text)
      friendLink.setLogo(null);
    else if (StringUtils.isEmpty(friendLink.getLogo()))
      return ERROR_PAGE;
    this.friendLinkService.save(friendLink);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("types", FriendLink.Type.values());
    model.addAttribute("friendLink", this.friendLinkService.find(id));
    return "/admin/friend_link/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(FriendLink friendLink, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,friendLink, new Class[0]))
      return ERROR_PAGE;
    if (friendLink.getType() == FriendLink.Type.text)
      friendLink.setLogo(null);
    else if (StringUtils.isEmpty(friendLink.getLogo()))
      return ERROR_PAGE;
    this.friendLinkService.update(friendLink);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.friendLinkService.findPage(pageable));
    return "/admin/friend_link/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.friendLinkService.delete(ids);
    return ADMIN_SUCCESS;
  }
}