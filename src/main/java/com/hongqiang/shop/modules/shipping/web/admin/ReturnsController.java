package com.hongqiang.shop.modules.shipping.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.shipping.service.ReturnsService;

@Controller("adminReturnsController")
@RequestMapping({"${adminPath}/returns"})
public class ReturnsController extends BaseController
{

 @Autowired
  private ReturnsService returnsService;

  @RequestMapping(value={"/view"}, method=RequestMethod.GET)
  public String view(Long id, ModelMap model)
  {
    model.addAttribute("returns", this.returnsService.find(id));
    return "/admin/returns/view";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.returnsService.findPage(pageable));
    return "/admin/returns/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.returnsService.delete(ids);
    return ADMIN_SUCCESS;
  }
}