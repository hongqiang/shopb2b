package com.hongqiang.shop.modules.content.web.admin;

import javax.servlet.http.HttpServletRequest;

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
import com.hongqiang.shop.modules.content.service.ConsultationService;
import com.hongqiang.shop.modules.entity.Consultation;

@Controller("adminConsultationController")
@RequestMapping({"${adminPath}/consultation"})
public class ConsultationController extends BaseController
{

  @Autowired
  private ConsultationService consultationService;

  @RequestMapping(value={"/reply"}, method=RequestMethod.GET)
  public String reply(Long id, ModelMap model)
  {
    model.addAttribute("consultation", this.consultationService.find(id));
    return "/admin/consultation/reply";
  }

  @RequestMapping(value={"/reply"}, method=RequestMethod.POST)
  public String reply(Long id, String content, HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(Consultation.class, "content", content, new Class[0]))
      return ERROR_PAGE;
    Consultation localConsultation1 = (Consultation)this.consultationService.find(id);
    if (localConsultation1 == null)
      return ERROR_PAGE;
    Consultation localConsultation2 = new Consultation();
    localConsultation2.setContent(content);
    localConsultation2.setIp(request.getRemoteAddr());
    this.consultationService.reply(localConsultation1, localConsultation2);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:reply.jhtml?id=" + id;
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("consultation", this.consultationService.find(id));
    return "/admin/consultation/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Long id, @RequestParam(defaultValue="false") Boolean isShow, RedirectAttributes redirectAttributes)
  {
    Consultation localConsultation = (Consultation)this.consultationService.find(id);
    if (localConsultation == null)
      return ERROR_PAGE;
    if (isShow != localConsultation.getIsShow())
    {
      localConsultation.setIsShow(isShow);
      this.consultationService.update(localConsultation);
    }
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.consultationService.findPage(null, null, null, pageable));
    return "/admin/consultation/list";
  }

  @RequestMapping(value={"/delete_reply"}, method=RequestMethod.POST)
  @ResponseBody
  public Message deleteReply(Long id)
  {
    Consultation localConsultation = (Consultation)this.consultationService.find(id);
    if ((localConsultation == null) || (localConsultation.getForConsultation() == null))
      return ADMIN_ERROR;
    this.consultationService.delete(localConsultation);
    return ADMIN_SUCCESS;
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids != null)
      this.consultationService.delete(ids);
    return ADMIN_SUCCESS;
  }
}