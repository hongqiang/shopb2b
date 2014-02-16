package com.hongqiang.shop.modules.user.web.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hongqiang.shop.common.utils.DateUtils;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.common.utils.plugin.service.PluginService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PaymentService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Payment;
import com.hongqiang.shop.modules.entity.Sn;
import com.hongqiang.shop.modules.product.service.SnService;
import com.hongqiang.shop.modules.user.service.DepositService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberDepositController")
@RequestMapping({"${memberPath}/deposit"})
public class DepositController extends BaseController
{
  private static final int PAGE_SIZE = 10;

  @Autowired
  private MemberService memberService;

  @Autowired
  private DepositService depositService;

  @Autowired
  private PluginService pluginService;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private SnService snService;

  @RequestMapping(value={"/recharge"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String recharge(ModelMap model)
  {
    List<PaymentPlugin> localList = this.pluginService.getPaymentPlugins(true);
    if (!localList.isEmpty())
    {
      model.addAttribute("defaultPaymentPlugin", localList.get(0));
      model.addAttribute("paymentPlugins", localList);
    }
    return "shop/member/deposit/recharge";
  }

  @RequestMapping(value={"/recharge"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String recharge(BigDecimal amount, String paymentPluginId, HttpServletRequest request, ModelMap model)
  {
    PaymentPlugin localPaymentPlugin = this.pluginService.getPaymentPlugin(paymentPluginId);
    if ((localPaymentPlugin == null) || (!localPaymentPlugin.getIsEnabled()))
      return SHOP_ERROR_PAGE;
    Setting localSetting = SettingUtils.get();
    if ((amount == null) || (amount.compareTo(new BigDecimal(0)) <= 0) || (amount.precision() > 15) || (amount.scale() > localSetting.getPriceScale().intValue()))
      return SHOP_ERROR_PAGE;
    BigDecimal localBigDecimal = localPaymentPlugin.getFee(amount);
    amount = amount.add(localBigDecimal);
    Payment localPayment = new Payment();
    localPayment.setSn(this.snService.generate(Sn.Type.payment));
    localPayment.setType(Payment.Type.online);
    localPayment.setStatus(Payment.Status.wait);
    localPayment.setPaymentMethod(localPaymentPlugin.getPaymentName());
    localPayment.setFee(localBigDecimal);
    localPayment.setAmount(amount);
    localPayment.setPaymentPluginId(paymentPluginId);
    localPayment.setExpire(localPaymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), localPaymentPlugin.getTimeout().intValue()) : null);
    localPayment.setMember(this.memberService.getCurrent());
    this.paymentService.save(localPayment);
    model.addAttribute("url", localPaymentPlugin.getUrl());
    model.addAttribute("method", localPaymentPlugin.getMethod());
    model.addAttribute("parameterMap", localPaymentPlugin.getParameterMap(localPayment.getSn(), amount, addMessage("shop.member.deposit.recharge", new Object[0]), request));
    return "shop/payment/submit";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Integer pageNumber, ModelMap model)
  {
    Member localMember = this.memberService.getCurrent();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_SIZE));
    model.addAttribute("page", this.depositService.findPage(localMember, localPageable));
    return "shop/member/deposit/list";
  }
}