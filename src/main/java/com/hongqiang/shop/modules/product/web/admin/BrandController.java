package com.hongqiang.shop.modules.product.web.admin;

import org.apache.commons.lang.StringUtils;
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
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.product.service.BrandService;

@Controller("adminBrandController")
@RequestMapping({"${adminPath}/brand"})
public class BrandController extends BaseController
{

  @Autowired
  private BrandService brandService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("types", Brand.Type.values());
    return "/admin/brand/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Brand brand, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,brand, new Class[0]))
      return ERROR_PAGE;
    if (brand.getType() == Brand.Type.text)
      brand.setLogo(null);
    else if (StringUtils.isEmpty(brand.getLogo()))
      return ERROR_PAGE;
    brand.setProducts(null);
    brand.setProductCategories(null);
    brand.setPromotions(null);
    this.brandService.save(brand);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("types", Brand.Type.values());
    model.addAttribute("brand", this.brandService.find(id));
    return "/admin/brand/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Brand brand, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,brand, new Class[0]))
      return ERROR_PAGE;
    if (brand.getType() == Brand.Type.text)
      brand.setLogo(null);
    else if (StringUtils.isEmpty(brand.getLogo()))
      return ERROR_PAGE;
    this.brandService.update(brand, new String[] { "products", "productCategories", "promotions" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.brandService.findPage(pageable));
    return "/admin/brand/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.brandService.delete(ids);
    return ADMIN_SUCCESS;
  }

  	@RequestMapping(value={"/do"},method=RequestMethod.GET)
  	public void doit(){
//  		Long idLong=1L;
//  		Brand brand=this.brandService.find(idLong);
//  		System.out.println(brand.getName()+","+brand.getIntroduction());
//  		
//  	  	Brand aBrand =new Brand();
//  	  	aBrand.setName("神一样的存在");
//  	  	aBrand.setType(Brand.Type.text);
//  		this.brandService.save(aBrand);
//  		
//  		aBrand.setName("sit"); 	    
//  	    this.brandService.update(aBrand);
//  	  
//  	    this.brandService.delete(aBrand);
//  	  System.out.println("delete success.");
//  	  
//  	    String temp="hello";
//  	  for (int i = 0; i < 10; i++) {
//		Brand aBrand2 = new Brand();
//		temp+=i;
//		aBrand2.setName(temp);
//		aBrand2.setType(Brand.Type.text);
//		this.brandService.save(aBrand2);
//		System.out.println("save success.");
//		this.brandService.delete(aBrand2.getId());
//	}
//  		Pageable pageable = new Pageable(1,40);
//  		Page<Brand> page=this.brandService.findPage(pageable);
//  		for (Brand o : page.getList()) {
//			System.out.print(o.getName()+", "+o.getIntroduction()+"\n");
//		}

  	}
}