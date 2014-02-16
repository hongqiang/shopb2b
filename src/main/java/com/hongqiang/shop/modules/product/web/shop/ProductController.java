package com.hongqiang.shop.modules.product.web.shop;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.ResourceNotFoundException;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PromotionService;
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.entity.Tag;
import com.hongqiang.shop.modules.product.dao.BrandDao;
import com.hongqiang.shop.modules.product.dao.ProductCategoryDao;
import com.hongqiang.shop.modules.product.dao.ProductDao;
import com.hongqiang.shop.modules.product.service.BrandService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.product.service.TagService;
import com.hongqiang.shop.modules.util.service.SearchService;

@Controller("shopProductController")
@RequestMapping(value = "${frontPath}/product")
public class ProductController extends BaseController {
	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private TagService tagService;

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = { "/history" }, method = RequestMethod.GET)
	@ResponseBody
	public List<Product> history(Long[] ids) {
		return this.productService.findList(ids);
	}

	@RequestMapping(value = { "/list/{productCategoryId}" }, method = RequestMethod.GET)
	public String list(@PathVariable Long productCategoryId, Long brandId,
			Long promotionId, Long[] tagIds, BigDecimal startPrice,
			BigDecimal endPrice, Product.OrderType orderType,
			Integer pageNumber, Integer pageSize, HttpServletRequest request,
			ModelMap model) {
		ProductCategory localProductCategory = (ProductCategory) this.productCategoryService
				.find(productCategoryId);
		if (localProductCategory == null)
			throw new ResourceNotFoundException();
		Brand localBrand = (Brand) this.brandService.find(brandId);
		Promotion localPromotion = (Promotion) this.promotionService
				.find(promotionId);
		List<Tag> tags = this.tagService.findList(tagIds);
		HashMap<Attribute, String> localHashMap = new HashMap<Attribute, String>();
		if (localProductCategory != null) {
			Set<Attribute> attributes = localProductCategory.getAttributes();
			Iterator<Attribute> localIterator = attributes.iterator();
			while (localIterator.hasNext()) {
				Attribute localAttribute = (Attribute) localIterator.next();
				String str = request.getParameter("attribute_"+ localAttribute.getId());
				if (!StringUtils.isNotEmpty(str))
					continue;
				localHashMap.put(localAttribute, str);
			}
		}
		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", Product.OrderType.values());// 布局类型，从前端页面传过来
		model.addAttribute("productCategory", localProductCategory);
		model.addAttribute("brand", localBrand);
		model.addAttribute("promotion", localPromotion);
		model.addAttribute("tags", tags);
		model.addAttribute("attributeValue", localHashMap);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNumber", pageable.getPageNumber());
		model.addAttribute("pageSize", pageable.getPageSize());
		model.addAttribute("page", this.productService.findPage(
				localProductCategory, localBrand, localPromotion, tags,
				localHashMap, startPrice, endPrice, Boolean.valueOf(true),
				Boolean.valueOf(true), null, Boolean.valueOf(false), null,
				null, orderType, pageable));
		return "/shop/product/list";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Long brandId, Long promotionId, Long[] tagIds,
			BigDecimal startPrice, BigDecimal endPrice,
			Product.OrderType orderType, Integer pageNumber, Integer pageSize,
			HttpServletRequest request, ModelMap model) {
		System.out.println("promotionId="+promotionId);
		Brand localBrand = (Brand) this.brandService.find(brandId);
		Promotion localPromotion = (Promotion) this.promotionService
				.find(promotionId);
		List<Tag> tags = this.tagService.findList(tagIds);
		Pageable localPageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", Product.OrderType.values());
		model.addAttribute("brand", localBrand);
		model.addAttribute("promotion", localPromotion);
		model.addAttribute("tags", tags);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("page", this.productService.findPage(null,
				localBrand, localPromotion, tags, null, startPrice, endPrice,
				Boolean.valueOf(true), Boolean.valueOf(true), null,
				Boolean.valueOf(false), null, null, orderType, localPageable));
		return "/shop/product/list";
	}

	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(String keyword, BigDecimal startPrice,
			BigDecimal endPrice, Product.OrderType orderType,
			Integer pageNumber, Integer pageSize, ModelMap model) throws UnsupportedEncodingException {
		System.out.println("keywords="+keyword);
		System.out.println(URLDecoder.decode(keyword,"UTF-8"));
		if (StringUtils.isEmpty(keyword))
			return SHOP_ERROR_PAGE;
		Pageable localPageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", Product.OrderType.values());
		model.addAttribute("productKeyword", keyword);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("page", this.searchService.search(keyword,
				startPrice, endPrice, orderType, localPageable));
		return "shop/product/search";
	}

	@RequestMapping(value = { "/hits/{id}" }, method = RequestMethod.GET)
	@ResponseBody
	public Long hits(@PathVariable Long id) {
		return Long.valueOf(this.productService.viewHits(id));
	}

	// @RequestMapping(value={"/do"},method=RequestMethod.GET)
	// public void test() {
	// List<Long> li = new ArrayList<Long>();
	// for (Long i = 1L; i <= 300L; i++) {
	// li.add(i);
	// }
	// Long[] array = new Long[li.size()];
	// for (int i = 0; i < li.size(); i++) {
	// array[i] = li.get(i);
	// }
	// System.out.println("hello");
	// List<Product> products = this.productService.findList(array);
	// for (Product o : products) {
	// System.out.print(o.getName() + ", " + o.getPrice() + "\n");
	// }
	//
	// boolean bl= this.productService.snExists("2013041");
	// System.out.println("bl="+bl);
	// Product product = this.productService.findBySn("2013041");
	// System.out.println(product.getName() + ", " + product.getPrice());
	//
	// bl= this.productService.snUnique("2013041","2013041");
	// System.out.println("bl="+bl);
	//
	// bl= this.productService.snUnique("2013041","2013042");
	// System.out.println("bl="+bl);
	//
	// bl= this.productService.snUnique("2013041","20130422222");
	// System.out.println("bl="+bl);
	//
	// String keyword="Max";
	// Boolean isGift=true;
	// Integer count=null;
	// List<Product> products1=this.productService.search(keyword, isGift,
	// count);
	// System.out.println("products1.length="+products1.size());
	// for (Product o : products1) {
	// System.out.print(o.getName() + ", " + o.getPrice() + "\n");
	// }
	//
	// isGift=false;
	// count=null;
	// products1=this.productService.search(keyword, isGift, count);
	// System.out.println("products2.length="+products1.size());
	// for (Product o : products1) {
	// System.out.print(o.getName() + ", " + o.getPrice() + "\n");
	// }
	//
	// ProductCategory p = this.productCategoryDao.findById(11L);
	// Brand brand = this.brandDao.findById(1L);
	// List<Product> products = this.productService.findList(p, brand, null,
	// null,
	// null, null, null, null, null, null, null, null, null, null,
	// null, null, null);
	// for (Product o : products) {
	// System.out.print(o.getName() + ", " + o.getPrice() + "\n");
	// }
	// System.out.println("===============================================");
	// products = this.productService.findList(p,null,null,null,null);
	// for (Product o : products) {
	// System.out.print(o.getName() + ", " + o.getPrice() + "\n");
	// }
	// System.out.println("===============================================");
	// Pageable pageable = new Pageable();
	// pageable.setPageNumber(1);
	// pageable.setPageSize(40);
	// Page<Product> pag=this.productService.findPage( p,brand, null, null,
	// null, null,
	// null, null,
	// null,null,
	// null, null,
	// null, Product.OrderType.topDesc,
	// pageable);
	// for (Product o : pag.getList()) {
	// System.out.print(o.getName() + ", " + o.getPrice() + "\n");
	// }
	//
	// Product product = new Product();
	// product.setName("First");
	// BigDecimal decima = new BigDecimal(100);
	// product.setPrice(decima);
	// product.setIsGift(false);
	// product.setIsTop(false);
	// product.setIsList(true);
	// product.setIsMarketable(false);
	// product.setProductCategory(p);
	// product.setCreateDate(new Date());
	// product.setUpdateDate(new Date());
	// this.productService.save(product);
	//
	// product.setName("hello");
	// this.productService.update(product);
	//
	// this.productService.delete(product);
	// }

}