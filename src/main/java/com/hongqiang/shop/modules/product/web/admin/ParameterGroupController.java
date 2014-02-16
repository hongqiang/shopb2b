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
import com.hongqiang.shop.modules.entity.Parameter;
import com.hongqiang.shop.modules.entity.ParameterGroup;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.product.service.ParameterGroupService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;

@Controller("adminParameterGroupController")
@RequestMapping({"${adminPath}/parameter_group"})
public class ParameterGroupController extends BaseController
{

  @Autowired
  private ParameterGroupService parameterGroupService;

  @Autowired
  private ProductCategoryService productCategoryService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    return "/admin/parameter_group/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(ParameterGroup parameterGroup, Long productCategoryId, RedirectAttributes redirectAttributes)
  {
    Iterator<Parameter> localIterator = parameterGroup.getParameters().iterator();
    while (localIterator.hasNext())
    {
      Parameter localParameter = (Parameter)localIterator.next();
      if ((localParameter == null) || (localParameter.getName() == null))
        localIterator.remove();
      else
        localParameter.setParameterGroup(parameterGroup);
    }
    parameterGroup.setProductCategory((ProductCategory)this.productCategoryService.find(productCategoryId));
    if (!beanValidator(redirectAttributes,parameterGroup, new Class[0]))
      return ERROR_PAGE;
    this.parameterGroupService.save(parameterGroup);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("parameterGroup", this.parameterGroupService.find(id));
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    return "/admin/parameter_group/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(ParameterGroup parameterGroup, Long productCategoryId, RedirectAttributes redirectAttributes)
  {
    Iterator<Parameter> localIterator = parameterGroup.getParameters().iterator();
    while (localIterator.hasNext())
    {
      Parameter localParameter = (Parameter)localIterator.next();
      if ((localParameter == null) || (localParameter.getName() == null))
        localIterator.remove();
      else
        localParameter.setParameterGroup(parameterGroup);
    }
    parameterGroup.setProductCategory((ProductCategory)this.productCategoryService.find(productCategoryId));
    if (!beanValidator(redirectAttributes,parameterGroup, new Class[0]))
      return ERROR_PAGE;
    this.parameterGroupService.update(parameterGroup);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.parameterGroupService.findPage(pageable));
    return "/admin/parameter_group/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.parameterGroupService.delete(ids);
    return ADMIN_SUCCESS;
  }
  
  @RequestMapping(value={"/dopara"},method=RequestMethod.GET)
	public void doit(){
//	  	ParameterGroup parameterGroup = this.parameterGroupService.find(1L);
//	  	System.out.println(parameterGroup.getName()+","+parameterGroup.getProductCategory());
//	  	List<Parameter> params = parameterGroup.getParameters();
//		for (Parameter op : params) {
//		System.out.print(op.getName()+", "+op.getParameterGroup()+"\n");
//	}
//		System.out.println("==============================================");
//		
//		Pageable pageable = new Pageable(1,40);
//		Page<ParameterGroup> page=this.parameterGroupService.findPage(pageable);
//		for (ParameterGroup o : page.getList()) {
//		System.out.print(o.getName()+", "+o.getProductCategory()+"\n");
//	}
//		System.out.println("==============================================");
//		
//		ProductCategory productCategory = new ProductCategory();
//		productCategory.setId(1L);
//		
//
//		
//		ParameterGroup parameterGroup2 = new ParameterGroup();
//		parameterGroup2.setName("hego");
//		parameterGroup2.setProductCategory(productCategory);
//		List<Parameter> lisp = new ArrayList<Parameter>();
//		for(int i=0;i<5;++i){
//			Parameter parameter = new Parameter();
//			parameter.setName("ab"+i);
//			parameter.setParameterGroup(parameterGroup2);
//			lisp.add(parameter);
//		}
//		parameterGroup2.setParameters(lisp);
//		this.parameterGroupService.save(parameterGroup2);
//		
//		parameterGroup.setName("lenght");
//		ParameterGroup aGroup = this.parameterGroupService.update(parameterGroup);
//		System.out.println(aGroup.getName()+","+aGroup.getProductCategory());
  }
}