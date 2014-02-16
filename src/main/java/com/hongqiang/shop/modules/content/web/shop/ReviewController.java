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
import com.hongqiang.shop.modules.content.service.ReviewService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Review;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.modules.util.service.CaptchaService;

@Controller("shopReviewController")
@RequestMapping({"${frontPath}/review"})
public class ReviewController extends BaseController
{
  private static final int PAGE_SIZE = 10;

  @Autowired
  private ReviewService reviewService;

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
    if (!localSetting.getIsReviewEnabled().booleanValue())
      throw new ResourceNotFoundException();
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      throw new ResourceNotFoundException();
    model.addAttribute("product", localProduct);
    model.addAttribute("captchaId", UUID.randomUUID().toString());
    return "/shop/review/add";
  }

  @RequestMapping(value={"/content/{id}"}, method=RequestMethod.GET)
  public String content(@PathVariable Long id, Integer pageNumber, ModelMap model)
  {
    Setting localSetting = SettingUtils.get();
    if (!localSetting.getIsReviewEnabled().booleanValue())
      throw new ResourceNotFoundException();
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      throw new ResourceNotFoundException();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_SIZE));
    model.addAttribute("product", localProduct);
    model.addAttribute("page", this.reviewService.findPage(null, localProduct, null, Boolean.valueOf(true), localPageable));
    return "/shop/review/content";
  }

  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message save(String captchaId, String captcha, Long id, Integer score, String content, HttpServletRequest request)
  {
    if (!this.captchaService.isValid(Setting.CaptchaType.review, captchaId, captcha))
      return Message.error("shop.captcha.invalid", new Object[0]);
    Setting localSetting = SettingUtils.get();
    if (!localSetting.getIsReviewEnabled().booleanValue())
      return Message.error("shop.review.disabled", new Object[0]);
    if ((!beanValidator(Review.class, "score", score, new Class[0])) || (!beanValidator(Review.class, "content", content, new Class[0])))
      return SHOP_ERROR;
    Product localProduct = (Product)this.productService.find(id);
    if (localProduct == null)
      return SHOP_ERROR;
    Member localMember = this.memberService.getCurrent();
    if ((localSetting.getReviewAuthority() != Setting.ReviewAuthority.anyone) && (localMember == null))
      return Message.error("shop.review.accessDenied", new Object[0]);
    if (localSetting.getReviewAuthority() == Setting.ReviewAuthority.purchased)
    {
      if (!this.productService.isPurchased(localMember, localProduct))
        return Message.error("shop.review.noPurchased", new Object[0]);
      if (this.reviewService.isReviewed(localMember, localProduct))
        return Message.error("shop.review.reviewed", new Object[0]);
    }
    Review localReview = new Review();
    localReview.setScore(score);
    localReview.setContent(content);
    localReview.setIp(request.getRemoteAddr());
    localReview.setMember(localMember);
    localReview.setProduct(localProduct);
    if (localSetting.getIsReviewCheck().booleanValue())
    {
      localReview.setIsShow(Boolean.valueOf(false));
      this.reviewService.save(localReview);
      return Message.success("shop.review.check", new Object[0]);
    }
    localReview.setIsShow(Boolean.valueOf(true));
    this.reviewService.save(localReview);
    return Message.success("shop.review.success", new Object[0]);
  }
}