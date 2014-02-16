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
import com.hongqiang.shop.modules.shipping.service.RefundsService;

@Controller("adminRefundsController")
@RequestMapping({"${adminPath}/refunds"})
public class RefundsController extends BaseController
{

  @Autowired
  private RefundsService refundsService;

  @RequestMapping(value={"/view"}, method=RequestMethod.GET)
  public String view(Long id, ModelMap model)
  {
    model.addAttribute("refunds", this.refundsService.find(id));
    return "/admin/refunds/view";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.refundsService.findPage(pageable));
    return "/admin/refunds/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.refundsService.delete(ids);
    return ADMIN_SUCCESS;
  }
}