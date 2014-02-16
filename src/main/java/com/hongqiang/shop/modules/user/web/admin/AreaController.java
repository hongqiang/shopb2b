package com.hongqiang.shop.modules.user.web.admin;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.user.service.AreaService;

@Controller("adminAreaController")
@RequestMapping({"${adminPath}/area"})
public class AreaController extends BaseController
{

  @Autowired
  private AreaService areaService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(Long parentId, ModelMap model)
  {
    model.addAttribute("parent", this.areaService.find(parentId));
    return "/admin/area/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Area area, Long parentId, RedirectAttributes redirectAttributes)
  {
    area.setParent((Area)this.areaService.find(parentId));
    if (!beanValidator(redirectAttributes,area, new Class[0]))
      return ERROR_PAGE;
    area.setFullName(null);
    area.setTreePath(null);
    area.setChildren(null);
    area.setMembers(null);
    area.setReceivers(null);
    area.setOrders(null);
    area.setDeliveryCenters(null);
    this.areaService.save(area);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("area", this.areaService.find(id));
    return "/admin/area/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Area area, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,area, new Class[0]))
      return ERROR_PAGE;
    this.areaService.update(area, new String[] { "fullName", "treePath", "parent", "children", "members", "receivers", "orders", "deliveryCenters" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Long parentId, ModelMap model)
  {
    Area localArea = (Area)this.areaService.find(parentId);
    if (localArea != null)
    {
      model.addAttribute("parent", localArea);
      model.addAttribute("areas", new ArrayList<Area>(localArea.getChildren()));
    }
    else
    {
      model.addAttribute("areas", this.areaService.findRoots());
    }
    return "/admin/area/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long id)
  {
    this.areaService.delete(id);
    return ADMIN_SUCCESS;
  }
}