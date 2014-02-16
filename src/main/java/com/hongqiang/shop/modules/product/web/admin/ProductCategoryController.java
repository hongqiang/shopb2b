package com.hongqiang.shop.modules.product.web.admin;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.ProductImage;
import com.hongqiang.shop.modules.product.service.BrandService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.modules.product.service.ProductImageService;

@Controller("adminProductCategoryController")
@RequestMapping({"${adminPath}/product_category"})
public class ProductCategoryController extends BaseController
{

	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
  private ProductCategoryService productCategoryService;

	@Autowired
  private BrandService brandService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    model.addAttribute("brands", this.brandService.findAll());
    return "/admin/product_category/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(ProductCategory productCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes)
  {
    productCategory.setParent((ProductCategory)this.productCategoryService.find(parentId));
    productCategory.setBrands(new HashSet<Brand>(this.brandService.findList(brandIds)));
    if (!beanValidator(redirectAttributes,productCategory, new Class[0]))
      return ERROR_PAGE;
    productCategory.setTreePath(null);
    productCategory.setGrade(null);
    productCategory.setChildren(null);
    productCategory.setProducts(null);
    productCategory.setParameterGroups(null);
    productCategory.setAttributes(null);
    productCategory.setPromotions(null);
    this.productCategoryService.save(productCategory);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    ProductCategory localProductCategory = (ProductCategory)this.productCategoryService.find(id);
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    model.addAttribute("brands", this.brandService.findAll());
    model.addAttribute("productCategory", localProductCategory);
    model.addAttribute("children", this.productCategoryService.findChildren(localProductCategory));
    return "/admin/product_category/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(ProductCategory productCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes)
  {
    productCategory.setParent((ProductCategory)this.productCategoryService.find(parentId));
    productCategory.setBrands(new HashSet<Brand>(this.brandService.findList(brandIds)));
    if (!beanValidator(redirectAttributes,productCategory, new Class[0]))
      return ERROR_PAGE;
	//非顶级分类,异常设置情况跳转到错误
    if (productCategory.getParent() != null)
    {
      ProductCategory localProductCategory = productCategory.getParent();
      if (localProductCategory.equals(productCategory))
        return ERROR_PAGE;
      List<ProductCategory> localList = this.productCategoryService.findChildren(localProductCategory);
      if ((localList != null) && (localList.contains(localProductCategory)))
        return ERROR_PAGE;
    }
	//顶级分类和正确的非顶级分类
    this.productCategoryService.update(productCategory, 
    		new String[] { "treePath", "grade", "children", "products", "parameterGroups", "attributes", "promotions" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(ModelMap model)
  {
    model.addAttribute("productCategoryTree", this.productCategoryService.findTree());
    return "/admin/product_category/list";
  }

  @RequestMapping(value={"/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message delete(Long id)
  {
    ProductCategory localProductCategory = (ProductCategory)this.productCategoryService.find(id);
	//应该是已经删除之类的字符串
    if (localProductCategory == null)
      return ADMIN_ERROR;
	//得到该分类的所有子级分类，存在子级分类返回不能删除的消息
    Set<ProductCategory> localSet1 = localProductCategory.getChildren();
    if ((localSet1 != null) && (!localSet1.isEmpty()))
      return Message.error("admin.productCategory.deleteExistChildrenNotAllowed", new Object[0]);
	//得到该分类的所有商品，存在商品返回不能删除的消息
    Set<Product> localSet2 = localProductCategory.getProducts();
    if ((localSet2 != null) && (!localSet2.isEmpty()))
      return Message.error("admin.productCategory.deleteExistProductNotAllowed", new Object[0]);
    this.productCategoryService.delete(id);
    return ADMIN_SUCCESS;
  }
  
//  @RequiresPermissions("sys:area:edit")
  @RequestMapping(value = "/form",method = RequestMethod.POST)
	public String handleFormUpload(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) {
	    System.out.println("name="+name);
		System.out.println("get here.");
		try {
			if (!file.isEmpty()) {
				System.out.println("and get here");
				byte[] bytes = file.getBytes();
				System.out.println("len= " + bytes.length);
				ProductImage productImage = new ProductImage();
				productImage.setFile(file);
				productImage.setTitle("hehe");
				productImage.setOrder(1);
				System.out.println("productImage.Title"+productImage.getTitle());
				System.out.println("productImage.Order"+productImage.getOrder());
				this.productImageService.build(productImage);
				System.out.println("productImage.Large"+productImage.getLarge());
				System.out.println("productImage.Source"+productImage.getSource());
				System.out.println("productImage.Title"+productImage.getTitle());
				System.out.println("build success");
			} else {
				System.out.println("no file upload");
				return "redirect:uploadFailure";
			}
		} catch (IOException e) {
			System.out.println("nothing");
		}
		System.out.println("over\n");
		return "redirect:list";
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String handleFormUpload() {
		System.out.println("we get here.");
		return "modules/upload/upload";
	}

}