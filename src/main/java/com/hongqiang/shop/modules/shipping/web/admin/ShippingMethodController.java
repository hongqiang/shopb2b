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
import com.hongqiang.shop.modules.entity.ShippingMethod;
import com.hongqiang.shop.modules.shipping.service.DeliveryCorpService;
import com.hongqiang.shop.modules.shipping.service.ShippingMethodService;

@Controller("adminShippingMethodController")
@RequestMapping({"${adminPath}/shipping_method"})
public class ShippingMethodController extends BaseController
{

  @Autowired
  private ShippingMethodService shippingMethodService;

  @Autowired
  private DeliveryCorpService deliveryCorpService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("deliveryCorps", this.deliveryCorpService.findAll());
    return "/admin/shipping_method/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(ShippingMethod shippingMethod, Long defaultDeliveryCorpId, RedirectAttributes redirectAttributes)
  {
    shippingMethod.setDefaultDeliveryCorp((DeliveryCorp)this.deliveryCorpService.find(defaultDeliveryCorpId));
    if (!beanValidator(redirectAttributes,shippingMethod, new Class[0]))
      return ERROR_PAGE;
    shippingMethod.setPaymentMethods(null);
    shippingMethod.setOrders(null);
    this.shippingMethodService.save(shippingMethod);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("deliveryCorps", this.deliveryCorpService.findAll());
    model.addAttribute("shippingMethod", this.shippingMethodService.find(id));
    return "/admin/shipping_method/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(ShippingMethod shippingMethod, Long defaultDeliveryCorpId, RedirectAttributes redirectAttributes)
  {
    shippingMethod.setDefaultDeliveryCorp((DeliveryCorp)this.deliveryCorpService.find(defaultDeliveryCorpId));
    if (!beanValidator(redirectAttributes,shippingMethod, new Class[0]))
      return ERROR_PAGE;
    this.shippingMethodService.update(shippingMethod, new String[] { "paymentMethods", "orders" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.shippingMethodService.findPage(pageable));
    return "/admin/shipping_method/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids.length >= this.shippingMethodService.count())
      return Message.error("admin.common.deleteAllNotAllowed", new Object[0]);
    this.shippingMethodService.delete(ids);
    return ADMIN_SUCCESS;
  }
}