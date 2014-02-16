package com.hongqiang.shop.modules.product.web.admin;

import java.util.Iterator;

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
import com.hongqiang.shop.modules.entity.Specification;
import com.hongqiang.shop.modules.entity.SpecificationValue;
import com.hongqiang.shop.modules.product.service.SpecificationService;

@Controller("adminSpecificationController")
@RequestMapping({"${adminPath}/specification"})
public class SpecificationController extends BaseController
{

  @Autowired
  private SpecificationService specificationService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("types", Specification.Type.values());
    return "/admin/specification/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Specification specification, RedirectAttributes redirectAttributes)
  {
    Iterator<SpecificationValue> localIterator = specification.getSpecificationValues().iterator();
    while (localIterator.hasNext())
    {
      SpecificationValue localSpecificationValue = (SpecificationValue)localIterator.next();
      if ((localSpecificationValue == null) || (localSpecificationValue.getName() == null))
      {
        localIterator.remove();
      }
      else
      {
        if (specification.getType() == Specification.Type.text)
          localSpecificationValue.setImage(null);
        localSpecificationValue.setSpecification(specification);
      }
    }
    if (!beanValidator(redirectAttributes,specification, new Class[0]))
      return ERROR_PAGE;
    specification.setProducts(null);
    this.specificationService.save(specification);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("types", Specification.Type.values());
    model.addAttribute("specification", this.specificationService.find(id));
    return "/admin/specification/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Specification specification, RedirectAttributes redirectAttributes)
  {
    Iterator<SpecificationValue> localIterator = specification.getSpecificationValues().iterator();
    while (localIterator.hasNext())
    {
      SpecificationValue localSpecificationValue = (SpecificationValue)localIterator.next();
      if ((localSpecificationValue == null) || (localSpecificationValue.getName() == null))
      {
        localIterator.remove();
      }
      else
      {
        if (specification.getType() == Specification.Type.text)
          localSpecificationValue.setImage(null);
        localSpecificationValue.setSpecification(specification);
      }
    }
    if (!beanValidator(redirectAttributes,specification, new Class[0]))
      return ERROR_PAGE;
    this.specificationService.update(specification);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.specificationService.findPage(pageable));
    return "/admin/specification/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids != null)
    {
      for (Long localLong : ids)
      {
        Specification localSpecification = (Specification)this.specificationService.find(localLong);
        if ((localSpecification != null) && (localSpecification.getProducts() != null) && (!localSpecification.getProducts().isEmpty()))
          return Message.error("admin.specification.deleteExistProductNotAllowed", new Object[] { localSpecification.getName() });
      }
      this.specificationService.delete(ids);
    }
    return ADMIN_SUCCESS;
  }
}