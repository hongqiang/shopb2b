package com.hongqiang.shop.modules.shipping.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.shipping.service.DeliveryTemplateService;
import com.hongqiang.shop.website.entity.DeliveryTemplate;

@Controller("adminDeliveryTemplateController")
@RequestMapping({"${adminPath}/delivery_template"})
public class DeliveryTemplateController extends BaseController
{

  @Autowired
  private DeliveryTemplateService deliveryTemplateService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(Pageable pageable)
  {
    return "/admin/delivery_template/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(DeliveryTemplate deliveryTemplate, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,deliveryTemplate, new Class[0]))
      return ERROR_PAGE;
    this.deliveryTemplateService.save(deliveryTemplate);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String eidt(Long id, Model model)
  {
    model.addAttribute("deliveryTemplate", this.deliveryTemplateService.find(id));
    return "/admin/delivery_template/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String udpate(DeliveryTemplate deliveryTemplate, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,deliveryTemplate, new Class[0]))
      return ERROR_PAGE;
    this.deliveryTemplateService.update(deliveryTemplate);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, Model model)
  {
    model.addAttribute("page", this.deliveryTemplateService.findPage(pageable));
    return "/admin/delivery_template/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.deliveryTemplateService.delete(ids);
    return ADMIN_SUCCESS;
  }
}