package com.hongqiang.shop.modules.product.web.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.ResourceNotFoundException;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.product.service.BrandService;

@Controller("shopBrandController")
@RequestMapping({"${frontPath}/brand"})
public class BrandController extends BaseController
{
  private static final int PAGE_NUMBER = 40;

  @Autowired
  private BrandService brandService;

  @RequestMapping(value={"/list/{pageNumber}"}, method=RequestMethod.GET)
  public String list(@PathVariable Integer pageNumber, ModelMap model)
  {
	Pageable pageBrand= new Pageable(pageNumber,Integer.valueOf(PAGE_NUMBER));
    model.addAttribute("page", this.brandService.findPage(pageBrand));
    return "/shop/brand/list";
  }

  @RequestMapping(value={"/content/{id}"}, method=RequestMethod.GET)
  public String content(@PathVariable Long id, ModelMap model)
  {
    Brand localBrand = (Brand)this.brandService.find(id);
    if (localBrand == null)
      throw new ResourceNotFoundException();
    model.addAttribute("brand", localBrand);
    return "/shop/brand/content";
  }
}