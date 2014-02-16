package com.hongqiang.shop.modules.account.web.admin;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.ExcelView;
import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CouponCodeService;
import com.hongqiang.shop.modules.account.service.CouponService;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.user.service.AdminService;

@Controller("adminCouponController")
@RequestMapping({"${adminPath}/coupon"})
public class CouponController extends BaseController
{

  @Autowired
  private CouponService couponService;

  @Autowired
  private CouponCodeService couponCodeService;

  @Autowired
  private AdminService adminService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("operators", Coupon.Operator.values());
    return "/admin/coupon/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Coupon coupon, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,coupon, new Class[0]))
      return ERROR_PAGE;
    if ((coupon.getBeginDate() != null) && (coupon.getEndDate() != null) && (coupon.getBeginDate().after(coupon.getEndDate())))
      return ERROR_PAGE;
    if ((coupon.getStartPrice() != null) && (coupon.getEndPrice() != null) && (coupon.getStartPrice().compareTo(coupon.getEndPrice()) > 0))
      return ERROR_PAGE;
    if ((coupon.getIsExchange().booleanValue()) && (coupon.getPoint() == null))
      return ERROR_PAGE;
    if ((coupon.getPriceOperator() == Coupon.Operator.divide) && (coupon.getPriceValue() != null) && (coupon.getPriceValue().compareTo(new BigDecimal(0)) == 0))
      return ERROR_PAGE;
    if (!coupon.getIsExchange().booleanValue())
      coupon.setPoint(null);
    coupon.setCouponCodes(null);
    coupon.setPromotions(null);
    coupon.setOrders(null);
    this.couponService.save(coupon);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("coupon", this.couponService.find(id));
    model.addAttribute("operators", Coupon.Operator.values());
    return "/admin/coupon/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Coupon coupon, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,coupon, new Class[0]))
      return ERROR_PAGE;
    if ((coupon.getBeginDate() != null) && (coupon.getEndDate() != null) && (coupon.getBeginDate().after(coupon.getEndDate())))
      return ERROR_PAGE;
    if ((coupon.getStartPrice() != null) && (coupon.getEndPrice() != null) && (coupon.getStartPrice().compareTo(coupon.getEndPrice()) > 0))
      return ERROR_PAGE;
    if ((coupon.getIsExchange().booleanValue()) && (coupon.getPoint() == null))
      return ERROR_PAGE;
    if ((coupon.getPriceOperator() == Coupon.Operator.divide) && (coupon.getPriceValue() != null) && (coupon.getPriceValue().compareTo(new BigDecimal(0)) == 0))
      return ERROR_PAGE;
    if (!coupon.getIsExchange().booleanValue())
      coupon.setPoint(null);
    this.couponService.update(coupon, new String[] { "couponCodes", "promotions", "orders" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.couponService.findPage(pageable));
    return "/admin/coupon/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.couponService.delete(ids);
    return ADMIN_SUCCESS;
  }

  @RequestMapping(value={"/build"}, method=RequestMethod.GET)
  public String build(Long id, ModelMap model)
  {
    Coupon localCoupon = (Coupon)this.couponService.find(id);
    model.addAttribute("coupon", localCoupon);
    model.addAttribute("totalCount", this.couponCodeService.count(localCoupon, null, null, null, null));
    model.addAttribute("usedCount", this.couponCodeService.count(localCoupon, null, null, null, Boolean.valueOf(true)));
    return "/admin/coupon/build";
  }

  @RequestMapping(value={"/download"}, method=RequestMethod.POST)
  public ModelAndView download(Long id, Integer count, ModelMap model)
  {
    if ((count == null) || (count.intValue() <= 0))
      count = Integer.valueOf(50);
    Coupon coupon = (Coupon)this.couponService.find(id);
    List<CouponCode> couponCodes = this.couponCodeService.build(coupon, null, count);
    String str = "coupon_code_" + new SimpleDateFormat("yyyyMM").format(new Date()) + ".xls";
    String[] infos = new String[4];
    infos[0] = (addMessage("admin.coupon.type", new Object[0]) + ": " + coupon.getName());
    infos[1] = (addMessage("admin.coupon.count", new Object[0]) + ": " + count);
    infos[2] = (addMessage("admin.coupon.operator", new Object[0]) + ": " + this.adminService.getCurrentUsername());
    infos[3] = (addMessage("admin.coupon.date", new Object[0]) + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    return new ModelAndView(new ExcelView(str, null, new String[] { "code" }, new String[] { addMessage("admin.coupon.title", new Object[0]) }, null, null, couponCodes, infos), model);
  }
}