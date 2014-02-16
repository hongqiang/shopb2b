package com.hongqiang.shop.website.web.admin;

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
import com.hongqiang.shop.website.entity.Ad;
import com.hongqiang.shop.website.entity.AdPosition;
import com.hongqiang.shop.website.service.AdPositionService;
import com.hongqiang.shop.website.service.AdService;

@Controller("adminAdController")
@RequestMapping({"${adminPath}/ad"})
public class AdController extends BaseController
{

  @Autowired
  private AdService adService;

  @Autowired
  private AdPositionService adPositionService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("types", Ad.Type.values());
    model.addAttribute("adPositions", this.adPositionService.findAll());
    return "/admin/ad/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes)
  {
    ad.setAdPosition((AdPosition)this.adPositionService.find(adPositionId));
    if (!beanValidator(redirectAttributes,ad, new Class[0]))
      return ERROR_PAGE;
    if ((ad.getBeginDate() != null) && (ad.getEndDate() != null) && (ad.getBeginDate().after(ad.getEndDate())))
      return ERROR_PAGE;
    if (ad.getType() == Ad.Type.text)
      ad.setPath(null);
    else
      ad.setContent(null);
    this.adService.save(ad);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("types", Ad.Type.values());
    model.addAttribute("ad", this.adService.find(id));
    model.addAttribute("adPositions", this.adPositionService.findAll());
    return "/admin/ad/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes)
  {
    ad.setAdPosition((AdPosition)this.adPositionService.find(adPositionId));
    if (!beanValidator(redirectAttributes,ad, new Class[0]))
      return ERROR_PAGE;
    if ((ad.getBeginDate() != null) && (ad.getEndDate() != null) && (ad.getBeginDate().after(ad.getEndDate())))
      return ERROR_PAGE;
    if (ad.getType() == Ad.Type.text)
      ad.setPath(null);
    else
      ad.setContent(null);
    this.adService.update(ad);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.adService.findPage(pageable));
    return "/admin/ad/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.adService.delete(ids);
    return ADMIN_SUCCESS;
  }
}