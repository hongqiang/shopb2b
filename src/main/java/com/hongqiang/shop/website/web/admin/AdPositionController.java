package com.hongqiang.shop.website.web.admin;

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
import com.hongqiang.shop.website.entity.AdPosition;
import com.hongqiang.shop.website.service.AdPositionService;

@Controller("adminAdPositionController")
@RequestMapping({"${adminPath}/ad_position"})
public class AdPositionController extends BaseController
{

  @Autowired
  private AdPositionService adPositionService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    return "/admin/ad_position/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(AdPosition adPosition, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,adPosition, new Class[0]))
      return ERROR_PAGE;
    adPosition.setAds(null);
    this.adPositionService.save(adPosition);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("adPosition", this.adPositionService.find(id));
    return "/admin/ad_position/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(AdPosition adPosition, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,adPosition, new Class[0]))
      return ERROR_PAGE;
    this.adPositionService.update(adPosition, new String[] { "ads" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.adPositionService.findPage(pageable));
    return "/admin/ad_position/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.adPositionService.delete(ids);
    return ADMIN_SUCCESS;
  }
}