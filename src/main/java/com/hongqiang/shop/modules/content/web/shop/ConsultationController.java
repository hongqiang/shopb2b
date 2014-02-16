package com.hongqiang.shop.modules.content.web.shop;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.ResourceNotFoundException;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.ConsultationService;
import com.hongqiang.shop.modules.entity.Consultation;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.modules.util.service.CaptchaService;

@Controller("shopConsultationController")
@RequestMapping({"${frontPath}/consultation"})
public class ConsultationController extends BaseController
{
  private static final int PAGE_SIZE = 10;

  @Autowired
  private ConsultationService consultationService;

  @Autowired
  private ProductService productService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private CaptchaService captchaService;

  @RequestMapping(value={"/add/{id}"}, method=RequestMethod.GET)
  public String add(@PathVariable Long id, ModelMap model)
  {
    Setting localSetting = SettingUtils.get();
    if (!localSetting.getIsConsultationEnabled().booleanValue())
      throw new ResourceNotFoundException();
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      throw new ResourceNotFoundException();
    model.addAttribute("product", localProduct);
    model.addAttribute("captchaId", UUID.randomUUID().toString());
    return "/shop/consultation/add";
  }

  @RequestMapping(value={"/content/{id}"}, method=RequestMethod.GET)
  public String content(@PathVariable Long id, Integer pageNumber, ModelMap model)
  {
    Setting localSetting = SettingUtils.get();
    if (!localSetting.getIsConsultationEnabled().booleanValue())
      throw new ResourceNotFoundException();
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      throw new ResourceNotFoundException();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_SIZE));
    model.addAttribute("product", localProduct);
    model.addAttribute("page", this.consultationService.findPage(null, localProduct, Boolean.valueOf(true), localPageable));
    return "/shop/consultation/content";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  @ResponseBody
  public Message save(String captchaId, String captcha, Long id, String content, HttpServletRequest request)
  {
    if (!this.captchaService.isValid(Setting.CaptchaType.consultation, captchaId, captcha))
      return Message.error("shop.captcha.invalid", new Object[0]);
    Setting localSetting = SettingUtils.get();
    if (!localSetting.getIsConsultationEnabled().booleanValue())
      return Message.error("shop.consultation.disabled", new Object[0]);
    if (!beanValidator(Consultation.class, "content", content, new Class[0]))
      return SHOP_ERROR;
    Member localMember = this.memberService.getCurrent();
    if ((localSetting.getConsultationAuthority() != Setting.ConsultationAuthority.anyone) && (localMember == null))
      return Message.error("shop.consultation.accessDenied", new Object[0]);
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      return SHOP_ERROR;
    Consultation localConsultation = new Consultation();
    localConsultation.setContent(content);
    localConsultation.setIp(request.getRemoteAddr());
    localConsultation.setMember(localMember);
    localConsultation.setProduct(localProduct);
    if (localSetting.getIsConsultationCheck().booleanValue())
    {
      localConsultation.setIsShow(Boolean.valueOf(false));
      this.consultationService.save(localConsultation);
      return Message.success("shop.consultation.check", new Object[0]);
    }
    localConsultation.setIsShow(Boolean.valueOf(true));
    this.consultationService.save(localConsultation);
    return Message.success("shop.consultation.success", new Object[0]);
  }
}