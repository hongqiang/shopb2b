package com.hongqiang.shop.modules.product.web.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PromotionService;
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Goods;
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.entity.Parameter;
import com.hongqiang.shop.modules.entity.ParameterGroup;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.ProductImage;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.entity.Specification;
import com.hongqiang.shop.modules.entity.SpecificationValue;
import com.hongqiang.shop.modules.entity.Tag;
import com.hongqiang.shop.modules.product.service.BrandService;
import com.hongqiang.shop.modules.product.service.GoodsService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.modules.product.service.ProductImageService;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.product.service.SnService;
import com.hongqiang.shop.modules.product.service.SpecificationService;
import com.hongqiang.shop.modules.product.service.SpecificationValueService;
import com.hongqiang.shop.modules.product.service.TagService;
import com.hongqiang.shop.modules.user.service.MemberRankService;
import com.hongqiang.shop.website.entity.FileInfo;
import com.hongqiang.shop.website.service.FileService;

@Controller("adminProductController")
@RequestMapping({ "${adminPath}/product" })
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private TagService tagService;

	@Autowired
	private MemberRankService memberRankService;

	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private SpecificationService specificationService;

	@Autowired
	private SpecificationValueService specificationValueService;

	@Autowired
	private FileService fileService;
	
	 @Autowired
	  private SnService snService;

	@RequestMapping(value = { "/check_sn" }, method =RequestMethod.GET )
	@ResponseBody
	public boolean checkSn(String previousSn, String sn) {
		if (StringUtils.isEmpty(sn))
			return false;
		return this.productService.snUnique(previousSn, sn);
	}

	@RequestMapping(value = { "/parameter_groups" }, method =RequestMethod.GET )
	@ResponseBody
	public Set<ParameterGroup> parameterGroups(Long id) {
		ProductCategory localProductCategory = (ProductCategory) this.productCategoryService
				.find(id);
		return localProductCategory.getParameterGroups();
	}

	@RequestMapping(value = { "/attributes" }, method =RequestMethod.GET )
	@ResponseBody
	public Set<Attribute> attributes(Long id) {
		ProductCategory localProductCategory = (ProductCategory) this.productCategoryService
				.find(id);
		return localProductCategory.getAttributes();
	}

	@RequestMapping(value = { "/add" }, method =RequestMethod.GET )
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("brands", this.brandService.findAll());
		model.addAttribute("tags", this.tagService.findList(Tag.Type.product));
		model.addAttribute("memberRanks", this.memberRankService.findAll());
		model.addAttribute("specifications",
				this.specificationService.findAll());
		return "/admin/product/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(Product product, Long productCategoryId, Long brandId,
			Long[] tagIds, Long[] specificationIds, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Iterator<ProductImage> productImageIterator = product.getProductImages().iterator();
		while (productImageIterator.hasNext()) {
			ProductImage productImage = (ProductImage) productImageIterator.next();
			if ((productImage == null) || (productImage.isEmpty())) {
				productImageIterator.remove();
			} else {
				if ((productImage.getFile() == null)
						|| ( productImage.getFile().isEmpty())
						|| (this.fileService.isValid(FileInfo.FileType.image, productImage.getFile())))
					continue;
				addMessage(redirectAttributes, Message.error("admin.upload.invalid", new Object[0]));
				return "redirect:add.jhtml";
			}
		}
		product.setProductCategory((ProductCategory) this.productCategoryService.find(productCategoryId));
		product.setBrand((Brand) this.brandService.find(brandId));
		product.setTags(new HashSet<Tag>(this.tagService.findList(tagIds)));
		if (!beanValidator(product, new Class[0]))
			return ERROR_PAGE;
		if ((StringUtils.isNotEmpty(product.getSn())) && (this.productService.snExists(product.getSn())))
			return ERROR_PAGE;
		if (product.getMarketPrice() == null) {
			BigDecimal marketPrice = getDefaultMarketPriceScale(product.getPrice());
			product.setMarketPrice(marketPrice);
		}
		if (product.getPoint() == null) {
			long point = getDefaultPointScale(product.getPrice());
			product.setPoint(Long.valueOf(point));
		}
		product.setFullName(null);
		product.setAllocatedStock(Integer.valueOf(0));
		product.setScore(Float.valueOf(0.0F));
		product.setTotalScore(Long.valueOf(0L));
		product.setScoreCount(Long.valueOf(0L));
		product.setHits(Long.valueOf(0L));
		product.setWeekHits(Long.valueOf(0L));
		product.setMonthHits(Long.valueOf(0L));
		product.setSales(Long.valueOf(0L));
		product.setWeekSales(Long.valueOf(0L));
		product.setMonthSales(Long.valueOf(0L));
		product.setWeekHitsDate(new Date());
		product.setMonthHitsDate(new Date());
		product.setWeekSalesDate(new Date());
		product.setMonthSalesDate(new Date());
		product.setReviews(null);
		product.setConsultations(null);
		product.setFavoriteMembers(null);
		product.setPromotions(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setGiftItems(null);
		product.setProductNotifies(null);
		Iterator<MemberRank> memberRankIterator = this.memberRankService.findAll().iterator();
		while (memberRankIterator.hasNext()) {
			MemberRank memberRank = (MemberRank) memberRankIterator.next();
			String memberPriceId = request.getParameter("memberPrice_" + ((MemberRank) memberRank).getId());
			if ((StringUtils.isNotEmpty(memberPriceId))
					&& (new BigDecimal( memberPriceId).compareTo(new BigDecimal(0)) >= 0))
				product.getMemberPrice().put(memberRank, new BigDecimal(memberPriceId));
			else
				product.getMemberPrice().remove(memberRank);
		}
		Iterator<ProductImage> imageIterator = product.getProductImages().iterator();
		while (imageIterator.hasNext()) {
			ProductImage productImage = (ProductImage)imageIterator.next();
			this.productImageService.build(productImage);
		}
		Collections.sort(product.getProductImages());
		if ((product.getImage() == null) && (product.getThumbnail() != null))
			product.setImage(product.getThumbnail());
		Iterator<ParameterGroup> parameterGroupIterator = product.getProductCategory().getParameterGroups().iterator();
		while (parameterGroupIterator.hasNext()) {
			ParameterGroup parameterGroup = (ParameterGroup) parameterGroupIterator.next();
			Iterator<Parameter> parameterIterator = parameterGroup.getParameters().iterator();
			while (parameterIterator.hasNext()) {
				Parameter parameter = (Parameter) parameterIterator.next();
				String parameterId = request.getParameter("parameter_" + parameter.getId());
				if (StringUtils.isNotEmpty(parameterId))
					product.getParameterValue().put(parameter, parameterId);
				else
					product.getParameterValue().remove(parameter);
			}
		}
		Iterator<Attribute> attributeIterator = product.getProductCategory().getAttributes().iterator();
		while (attributeIterator.hasNext()) {
			Attribute attribute = (Attribute) attributeIterator.next();
			String attributeId = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(attributeId))
				product.setAttributeValue(attribute,attributeId);
			else
				product.setAttributeValue(attribute, null);
		}
		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if ((specificationIds != null) && (specificationIds.length > 0)) {
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = (Specification) this.specificationService.find(specificationIds[i]);
				String[] specificationId = request.getParameterValues("specification_" + specification.getId());
				if ((specificationId == null) || (specificationId.length <= 0))
					continue;
				for (int j = 0; j < specificationId.length; j++) {
					if (i == 0)
						if (j == 0) {
							product.setGoods(goods);
							product.setSpecifications(new HashSet<Specification>());
							product.setSpecificationValues(new HashSet<SpecificationValue>());
							products.add(product);
						} else {
							Product localProduct = new Product();
							BeanUtils.copyProperties(product, localProduct);
							localProduct.setId(null);
							localProduct.setCreateDate(null);
							localProduct.setUpdateDate(null);
							localProduct.setSn(null);
							localProduct.setFullName(null);
							localProduct.setAllocatedStock(Integer.valueOf(0));
							localProduct.setIsList(Boolean.valueOf(false));
							localProduct.setScore(Float.valueOf(0.0F));
							localProduct.setTotalScore(Long.valueOf(0L));
							localProduct.setScoreCount(Long.valueOf(0L));
							localProduct.setHits(Long.valueOf(0L));
							localProduct.setWeekHits(Long.valueOf(0L));
							localProduct.setMonthHits(Long.valueOf(0L));
							localProduct.setSales(Long.valueOf(0L));
							localProduct.setWeekSales(Long.valueOf(0L));
							localProduct.setMonthSales(Long.valueOf(0L));
							localProduct.setWeekHitsDate(new Date());
							localProduct.setMonthHitsDate(new Date());
							localProduct.setWeekSalesDate(new Date());
							localProduct.setMonthSalesDate(new Date());
							localProduct.setGoods(goods);
							localProduct.setReviews(null);
							localProduct.setConsultations(null);
							localProduct.setFavoriteMembers(null);
							localProduct.setSpecifications(new HashSet<Specification>());
							localProduct.setSpecificationValues(new HashSet<SpecificationValue>());
							localProduct.setPromotions(null);
							localProduct.setCartItems(null);
							localProduct.setOrderItems(null);
							localProduct.setGiftItems(null);
							localProduct.setProductNotifies(null);
							products.add(localProduct);
						}
					Product localProduct = (Product)  products.get(j);
					SpecificationValue specificationValue = (SpecificationValue) this.specificationValueService
							.find(Long.valueOf(specificationId[j]));
					localProduct.getSpecifications().add(specification);
					localProduct.getSpecificationValues().add(specificationValue);
				}
			}
		} else {
			product.setGoods(goods);
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			products.add(product);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		this.goodsService.save(goods);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return  "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method =RequestMethod.GET )
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("brands", this.brandService.findAll());
		model.addAttribute("tags", this.tagService.findList(Tag.Type.product));
		model.addAttribute("memberRanks", this.memberRankService.findAll());
		model.addAttribute("specifications",
				this.specificationService.findAll());
		model.addAttribute("product", this.productService.find(id));
		return "/admin/product/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(Product product, Long productCategoryId, Long brandId,
			Long[] tagIds, Long[] specificationIds,
			Long[] specificationProductIds, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Iterator<ProductImage> productImageIterator = product.getProductImages().iterator();
		while (productImageIterator.hasNext()) {
			ProductImage productImage = (ProductImage)productImageIterator.next();
			if ((productImage == null)
					|| (productImage.isEmpty())) {
				productImageIterator.remove();
			} else {
				if ((productImage.getFile() == null)
						|| (productImage.getFile().isEmpty())
						|| (this.fileService.isValid(FileInfo.FileType.image,
								productImage.getFile())))
					continue;
				addMessage(redirectAttributes,
						Message.error("admin.upload.invalid", new Object[0]));
				return "redirect:edit.jhtml?id=" + product.getId();
			}
		}
		product.setProductCategory((ProductCategory) this.productCategoryService
				.find(productCategoryId));
		product.setBrand((Brand) this.brandService.find(brandId));
		product.setTags(new HashSet<Tag>(this.tagService.findList(tagIds)));
		if (!beanValidator(product, new Class[0]))
			return ERROR_PAGE;
		Product localProduct = (Product) this.productService.find(product.getId());
		if (localProduct == null)
			return ERROR_PAGE;
		if ((StringUtils.isNotEmpty(product.getSn()))
				&& (!this.productService.snUnique(
						localProduct.getSn(), product.getSn())))
			return ERROR_PAGE;
		if (product.getMarketPrice() == null) {
			BigDecimal marketPrice = getDefaultMarketPriceScale(product.getPrice());
			product.setMarketPrice(marketPrice);
		}
		if (product.getPoint() == null) {
			long l = getDefaultPointScale(product.getPrice());
			product.setPoint(Long.valueOf(l));
		}
		Iterator<MemberRank> memberRankIterator = this.memberRankService.findAll().iterator();
		while (memberRankIterator.hasNext()) {
			MemberRank memberRank = (MemberRank) memberRankIterator.next();
			String memberPriceId = request.getParameter("memberPrice_"
					+ memberRank.getId());
			if ((StringUtils.isNotEmpty(memberPriceId)
					&& (new BigDecimal(memberPriceId)
							.compareTo(new BigDecimal(0)) >= 0)))
				product.getMemberPrice().put(memberRank,
						new BigDecimal(memberPriceId));
			else
				product.getMemberPrice().remove(memberRank);
		}
		Iterator<ProductImage> productImageIterator2 = product.getProductImages().iterator();
		while (productImageIterator2.hasNext()) {
			ProductImage productImage = (ProductImage) productImageIterator2.next();
			this.productImageService.build(productImage);
		}
		Collections.sort(product.getProductImages());
		if ((product.getImage() == null) && (product.getThumbnail() != null))
			product.setImage(product.getThumbnail());
		Iterator<ParameterGroup> parameterGroupIterator = product.getProductCategory().getParameterGroups()
				.iterator();
		while (parameterGroupIterator.hasNext()) {
			ParameterGroup parameterGroup = (ParameterGroup) parameterGroupIterator.next();
			Iterator<Parameter> parameterIterator = parameterGroup.getParameters()
					.iterator();
			while ( parameterIterator.hasNext()) {
				Parameter parameter = (Parameter)parameterIterator.next();
				String patameterId = request.getParameter("parameter_"
						+ parameter.getId());
				if (StringUtils.isNotEmpty(patameterId))
					product.getParameterValue().put(parameter, patameterId);
				else
					product.getParameterValue().remove(parameter);
			}
		}
		Iterator<Attribute> attributeIterator = product.getProductCategory().getAttributes().iterator();
		while (attributeIterator.hasNext()) {
			Attribute attribute = (Attribute) attributeIterator.next();
			String attributeId = request.getParameter("attribute_"
					+ attribute.getId());
			if (StringUtils.isNotEmpty(attributeId))
				product.setAttributeValue(attribute,attributeId);
			else
				product.setAttributeValue(attribute, null);
		}
		Goods goods = localProduct.getGoods();
		List<Product> products = new ArrayList<Product>();
		if ((specificationIds != null) && (specificationIds.length > 0)) {
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = (Specification) this.specificationService
						.find(specificationIds[i]);
				String[] localSpecificationIds = request.getParameterValues("specification_"
						+ specification.getId());
				if ((localSpecificationIds == null) || (localSpecificationIds.length <= 0))
					continue;
				for (int j = 0; j < localSpecificationIds.length; j++) {
					if (i == 0)
						if (j == 0) {
							BeanUtils.copyProperties(product, localProduct,
									new String[] { "id", "createDate",
											"updateDate", "fullName",
											"allocatedStock", "score",
											"totalScore", "scoreCount", "hits",
											"weekHits", "monthHits", "sales",
											"weekSales", "monthSales",
											"weekHitsDate", "monthHitsDate",
											"weekSalesDate", "monthSalesDate",
											"goods", "reviews",
											"consultations", "favoriteMembers",
											"specifications",
											"specificationValues",
											"promotions", "cartItems",
											"orderItems", "giftItems",
											"productNotifies" });
							localProduct
									.setSpecifications(new HashSet<Specification>());
							localProduct
									.setSpecificationValues(new HashSet<SpecificationValue>());
							products.add(localProduct);
						} else if ((specificationProductIds != null)
								&& (j < specificationProductIds.length)) {
							localProduct = (Product) this.productService
									.find(specificationProductIds[j]);
							if (localProduct.getGoods() != goods)
								return ERROR_PAGE;
							localProduct.setSpecifications(new HashSet<Specification>());
							localProduct.setSpecificationValues(new HashSet<SpecificationValue>());
							products.add(localProduct);
						} else {
							localProduct = new Product();
							BeanUtils.copyProperties(product, localProduct);
							localProduct.setId(null);
							localProduct.setCreateDate(null);
							localProduct.setUpdateDate(null);
							localProduct.setSn(null);
							localProduct.setFullName(null);
							localProduct.setAllocatedStock(Integer.valueOf(0));
							localProduct.setIsList(Boolean.valueOf(false));
							localProduct.setScore(Float.valueOf(0.0F));
							localProduct.setTotalScore(Long.valueOf(0L));
							localProduct.setScoreCount(Long.valueOf(0L));
							localProduct.setHits(Long.valueOf(0L));
							localProduct.setWeekHits(Long.valueOf(0L));
							localProduct.setMonthHits(Long.valueOf(0L));
							localProduct.setSales(Long.valueOf(0L));
							localProduct.setWeekSales(Long.valueOf(0L));
							localProduct.setMonthSales(Long.valueOf(0L));
							localProduct.setWeekHitsDate(new Date());
							localProduct.setMonthHitsDate(new Date());
							localProduct.setWeekSalesDate(new Date());
							localProduct.setMonthSalesDate(new Date());
							localProduct.setGoods(goods);
							localProduct.setReviews(null);
							localProduct.setConsultations(null);
							localProduct.setFavoriteMembers(null);
							localProduct.setSpecifications(new HashSet<Specification>());
							localProduct.setSpecificationValues(new HashSet<SpecificationValue>());
							localProduct.setPromotions(null);
							localProduct.setCartItems(null);
							localProduct.setOrderItems(null);
							localProduct.setGiftItems(null);
							localProduct.setProductNotifies(null);
							products.add(localProduct);
						}
					Product tempProduct = (Product) products.get(j);
					SpecificationValue localSpecificationValue = (SpecificationValue) this.specificationValueService
							.find(Long.valueOf(localSpecificationIds[j]));
					tempProduct.getSpecifications().add(specification);
					tempProduct.getSpecificationValues().add(
							localSpecificationValue);
				}
			}
		} else {
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			BeanUtils
					.copyProperties(product, localProduct, new String[] { "id",
							"createDate", "updateDate", "fullName",
							"allocatedStock", "score", "totalScore",
							"scoreCount", "hits", "weekHits", "monthHits",
							"sales", "weekSales", "monthSales", "weekHitsDate",
							"monthHitsDate", "weekSalesDate", "monthSalesDate",
							"goods", "reviews", "consultations",
							"favoriteMembers", "promotions", "cartItems",
							"orderItems", "giftItems", "productNotifies" });
			products.add(localProduct);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		this.goodsService.update(goods);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return  "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method =RequestMethod.GET )
	public String list(Long productCategoryId, Long brandId, Long promotionId,
			Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, ModelMap model) {
		ProductCategory localProductCategory = (ProductCategory) this.productCategoryService
				.find(productCategoryId);
		Brand localBrand = (Brand) this.brandService.find(brandId);
		Promotion localPromotion = (Promotion) this.promotionService
				.find(promotionId);
		List<Tag> localList = this.tagService.findList(new Long[] { tagId });
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("brands", this.brandService.findAll());
		model.addAttribute("promotions", this.promotionService.findAll());
		model.addAttribute("tags", this.tagService.findList(Tag.Type.product));
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", this.productService.findPage(
				localProductCategory, localBrand, localPromotion, localList,
				null, null, null, isMarketable, isList, isTop, isGift,
				isOutOfStock, isStockAlert, Product.OrderType.dateDesc,
				pageable));
		return "/admin/product/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		this.productService.delete(ids);
		return ADMIN_SUCCESS;
	}

	private BigDecimal getDefaultMarketPriceScale(BigDecimal paramBigDecimal) {
		Setting localSetting = SettingUtils.get();
		Double localDouble = localSetting.getDefaultMarketPriceScale();
		return localSetting.setScale(paramBigDecimal.multiply(new BigDecimal(
				localDouble.toString())));
	}

	private long getDefaultPointScale(BigDecimal paramBigDecimal) {
		Setting localSetting = SettingUtils.get();
		Double localDouble = localSetting.getDefaultPointScale();
		return paramBigDecimal.multiply(new BigDecimal(localDouble.toString()))
				.longValue();
	}
}