package com.hongqiang.shop.modules.content.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.ReviewService;
import com.hongqiang.shop.modules.entity.Review;

@Controller("adminReviewController")
@RequestMapping({"${adminPath}/review"})
public class ReviewController extends BaseController
{

  @Autowired
  private ReviewService reviewService;

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("review", this.reviewService.find(id));
    return "/admin/review/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Long id, @RequestParam(defaultValue="false") Boolean isShow, RedirectAttributes redirectAttributes)
  {
    Review localReview = (Review)this.reviewService.find(id);
    if (localReview == null)
      return ERROR_PAGE;
    localReview.setIsShow(isShow);
    this.reviewService.update(localReview);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Review.Type type, Pageable pageable, ModelMap model)
  {
    model.addAttribute("type", type);
    model.addAttribute("types", Review.Type.values());
    model.addAttribute("page", this.reviewService.findPage(null, null, type, null, pageable));
    return "/admin/review/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.reviewService.delete(ids);
    return ADMIN_SUCCESS;
  }
}