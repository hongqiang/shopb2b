package com.hongqiang.shop.modules.shipping.web.admin;

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
import com.hongqiang.shop.modules.entity.DeliveryCorp;
import com.hongqiang.shop.modules.shipping.service.DeliveryCorpService;

@Controller("adminDeliveryCorpController")
@RequestMapping({"${adminPath}/delivery_corp"})
public class DeliveryCorpController extends BaseController
{

  @Autowired
  private DeliveryCorpService deliveryCorpService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add()
  {
    return "/admin/delivery_corp/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(DeliveryCorp deliveryCorp, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,deliveryCorp, new Class[0]))
      return ERROR_PAGE;
    deliveryCorp.setShippingMethods(null);
    this.deliveryCorpService.save(deliveryCorp);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("deliveryCorp", this.deliveryCorpService.find(id));
    return "/admin/delivery_corp/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(DeliveryCorp deliveryCorp, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,deliveryCorp, new Class[0]))
      return ERROR_PAGE;
    this.deliveryCorpService.update(deliveryCorp, new String[] { "shippingMethods" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.deliveryCorpService.findPage(pageable));
    return "/admin/delivery_corp/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.deliveryCorpService.delete(ids);
    return ADMIN_SUCCESS;
  }
}