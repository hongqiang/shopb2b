package com.hongqiang.shop.website.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.Seo;
import com.hongqiang.shop.website.service.SeoService;

@Controller("adminSeoController")
@RequestMapping({"${adminPath}/seo"})
public class SeoController extends BaseController
{

  @Autowired
  private SeoService seoService;

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("seo", this.seoService.find(id));
    return "/admin/seo/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Seo seo, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,seo, new Class[0]))
      return ERROR_PAGE;
    this.seoService.update(seo, new String[] { "type" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.seoService.findPage(pageable));
    return "/admin/seo/list";
  }
}