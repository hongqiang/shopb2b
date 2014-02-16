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
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.website.entity.Navigation;
import com.hongqiang.shop.website.service.NavigationService;

@Controller("adminNavigationController")
@RequestMapping({"${adminPath}/navigation"})
public class NavigationController extends BaseController
{

 @Autowired
  private NavigationService navigationService;

  @Autowired
  private ArticleCategoryService articleCategoryService;

  @Autowired
  private ProductCategoryService productCategoryService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("positions", Navigation.Position.values());
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    return "/admin/navigation/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Navigation navigation, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,navigation, new Class[0]))
      return ERROR_PAGE;
    this.navigationService.save(navigation);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("positions", Navigation.Position.values());
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    model.addAttribute("navigation", this.navigationService.find(id));
    return "/admin/navigation/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Navigation navigation, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,navigation, new Class[0]))
      return ERROR_PAGE;
    this.navigationService.update(navigation);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("topNavigations", this.navigationService.findList(Navigation.Position.top));
    model.addAttribute("middleNavigations", this.navigationService.findList(Navigation.Position.middle));
    model.addAttribute("bottomNavigations", this.navigationService.findList(Navigation.Position.bottom));
    return "/admin/navigation/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.navigationService.delete(ids);
    return ADMIN_SUCCESS;
  }
}