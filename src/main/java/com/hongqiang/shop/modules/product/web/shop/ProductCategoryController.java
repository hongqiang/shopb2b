package com.hongqiang.shop.modules.product.web.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;

@Controller("shopProductCategoryController")
@RequestMapping(value = "${frontPath}/product_category")
public class ProductCategoryController extends BaseController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("rootProductCategories",this.productCategoryService.findRoots());
//		List <ProductCategory> list = this.productCategoryService.findRoots();
//		for(ProductCategory p:list){
//			System.out.println(p.getName()+","+p.getPath());
//		}
//		return "modules/product/front/productCategory";
		return "/shop/product_category/index";
	}
}