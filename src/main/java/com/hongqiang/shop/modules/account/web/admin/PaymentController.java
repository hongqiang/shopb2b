package com.hongqiang.shop.modules.account.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PaymentService;
import com.hongqiang.shop.modules.entity.Payment;

@Controller("adminPaymentController")
@RequestMapping({"${adminPath}/payment"})
public class PaymentController extends BaseController
{

  @Autowired
  private PaymentService paymentService;

  @RequestMapping(value={"/view"}, method=RequestMethod.GET)
  public String view(Long id, ModelMap model)
  {
    model.addAttribute("payment", this.paymentService.find(id));
    return "/admin/payment/view";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.paymentService.findPage(pageable));
    return "/admin/payment/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids != null)
    {
      for (Long localLong : ids)
      {
        Payment localPayment = (Payment)this.paymentService.find(localLong);
        if ((localPayment != null) && (localPayment.getExpire() != null) && (!localPayment.hasExpired()))
          return Message.error("admin.payment.deleteUnexpiredNotAllowed", new Object[0]);
      }
      this.paymentService.delete(ids);
    }
    return ADMIN_SUCCESS;
  }
}