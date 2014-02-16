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
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.DeliveryCenter;
import com.hongqiang.shop.modules.shipping.service.DeliveryCenterService;
import com.hongqiang.shop.modules.user.service.AreaService;

@Controller("deliveryCenterController")
@RequestMapping({"${adminPath}/delivery_center"})
public class DeliveryCenterController extends BaseController
{

  @Autowired
  private DeliveryCenterService deliveryCenterService;

  @Autowired
  private AreaService areaService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add()
  {
    return "/admin/delivery_center/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(DeliveryCenter deliveryCenter, Long areaId, Model model, RedirectAttributes redirectAttributes)
  {
    deliveryCenter.setArea((Area)this.areaService.find(areaId));
    if (!beanValidator(redirectAttributes,deliveryCenter, new Class[0]))
      return ERROR_PAGE;
    deliveryCenter.setAreaName(null);
    this.deliveryCenterService.save(deliveryCenter);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, Model model)
  {
    model.addAttribute("deliveryCenter", this.deliveryCenterService.find(id));
    return "/admin/delivery_center/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(DeliveryCenter deliveryCenter, Long areaId, RedirectAttributes redirectAttributes)
  {
    deliveryCenter.setArea((Area)this.areaService.find(areaId));
    if (!beanValidator(redirectAttributes,deliveryCenter, new Class[0]))
      return ERROR_PAGE;
    this.deliveryCenterService.update(deliveryCenter, new String[] { "areaName" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Model model, Pageable pageable)
  {
    model.addAttribute("page", this.deliveryCenterService.findPage(pageable));
    return "/admin/delivery_center/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.deliveryCenterService.delete(ids);
    return ADMIN_SUCCESS;
  }
}