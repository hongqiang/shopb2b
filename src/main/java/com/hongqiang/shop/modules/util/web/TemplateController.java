package com.hongqiang.shop.modules.util.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.util.service.TemplateService;
import com.hongqiang.shop.modules.utils.Template;

@Controller("adminTemplateController")
@RequestMapping({"${adminPath}/template"})
public class TemplateController extends BaseController
{

  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;

  @Autowired
  private TemplateService templateService;

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(String id, ModelMap model)
  {
    if (StringUtils.isEmpty(id))
      return ERROR_PAGE;
    model.addAttribute("template", this.templateService.get(id));
    model.addAttribute("content", this.templateService.read(id));
    return "/admin/template/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(String id, String content, RedirectAttributes redirectAttributes)
  {
    if ((StringUtils.isEmpty(id)) || (content == null))
      return ERROR_PAGE;
    this.templateService.write(id, content);
    this.freeMarkerConfigurer.getConfiguration().clearTemplateCache();
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Template.Type type, ModelMap model)
  {
    model.addAttribute("type", type);
    model.addAttribute("types", Template.Type.values());
    model.addAttribute("templates", this.templateService.getList(type));
    return "/admin/template/list";
  }
}