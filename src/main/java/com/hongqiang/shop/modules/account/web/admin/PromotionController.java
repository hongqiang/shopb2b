package com.hongqiang.shop.modules.account.web.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.hongqiang.shop.modules.account.service.CouponService;
import com.hongqiang.shop.modules.account.service.PromotionService;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.GiftItem;
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.product.service.BrandService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.user.service.MemberRankService;

@Controller("adminPromotionController")
@RequestMapping({"${adminPath}/promotion"})
public class PromotionController extends BaseController
{
	private static final int PAGE_SIZE = 20;

  @Autowired
  private PromotionService promotionService;

  @Autowired
  private MemberRankService memberRankService;

  @Autowired
  private ProductCategoryService productCategoryService;

  @Autowired
  private ProductService productService;

  @Autowired
  private BrandService brandService;

  @Autowired
  private CouponService couponService;

  @RequestMapping(value={"/product_select"}, method=RequestMethod.GET)
  @ResponseBody
  public List<Map<String, Object>> productSelect(String q)
  {
    List<Map<String, Object>> localArrayList = new ArrayList<Map<String, Object>>();
    if (StringUtils.isNotEmpty(q))
    {
      List<Product> localList = this.productService.search(q, Boolean.valueOf(false), Integer.valueOf(PAGE_SIZE));
      Iterator<Product> localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Product localProduct = (Product)localIterator.next();
        HashMap<String, Object> localHashMap = new HashMap<String, Object>();
        localHashMap.put("id", localProduct.getId());
        localHashMap.put("sn", localProduct.getSn());
        localHashMap.put("fullName", localProduct.getFullName());
        localHashMap.put("path", localProduct.getPath());
        localArrayList.add(localHashMap);
      }
    }
    return localArrayList;
  }

  @RequestMapping(value={"/gift_select"}, method=RequestMethod.GET)
  @ResponseBody
  public List<Map<String, Object>> giftSelect(String q)
  {
    List<Map<String, Object>> localArrayList = new ArrayList<Map<String, Object>>();
    if (StringUtils.isNotEmpty(q))
    {
      List<Product> localList = this.productService.search(q, Boolean.valueOf(true), Integer.valueOf(PAGE_SIZE));
      Iterator<Product> localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Product localProduct = (Product)localIterator.next();
        HashMap<String, Object> localHashMap = new HashMap<String, Object>();
        localHashMap.put("id", localProduct.getId());
        localHashMap.put("sn", localProduct.getSn());
        localHashMap.put("fullName", localProduct.getFullName());
        localHashMap.put("path", localProduct.getPath());
        localArrayList.add(localHashMap);
      }
    }
    return localArrayList;
  }

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("operators", Promotion.Operator.values());
    model.addAttribute("memberRanks", this.memberRankService.findAll());
    model.addAttribute("productCategories", this.productCategoryService.findAll());
    model.addAttribute("brands", this.brandService.findAll());
    model.addAttribute("coupons", this.couponService.findAll());
    return "/admin/promotion/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Promotion promotion, Long[] memberRankIds, Long[] productCategoryIds, Long[] brandIds, Long[] couponIds, Long[] productIds, RedirectAttributes redirectAttributes)
  {
    promotion.setMemberRanks(new HashSet<MemberRank>(this.memberRankService.findList(memberRankIds)));
    promotion.setProductCategories(new HashSet<ProductCategory>(this.productCategoryService.findList(productCategoryIds)));
    promotion.setBrands(new HashSet<Brand>(this.brandService.findList(brandIds)));
    promotion.setCoupons(new HashSet<Coupon>(this.couponService.findList(couponIds)));
    Iterator<Product> localObject2 = this.productService.findList(productIds).iterator();
    while (localObject2.hasNext())
    {
      Product localObject1 = (Product)localObject2.next();
      if (((Product)localObject1).getIsGift().booleanValue())
        continue;
      promotion.getProducts().add(localObject1);
    }
    Iterator<GiftItem> localObject1 = promotion.getGiftItems().iterator();
    while (localObject1.hasNext())
    {
      GiftItem localGiftItem = (GiftItem)localObject1.next();
      if ((localGiftItem == null) || (((GiftItem)localGiftItem).getGift() == null) || (((GiftItem)localGiftItem).getGift().getId() == null))
      {
        localObject1.remove();
      }
      else
      {
        ((GiftItem)localGiftItem).setGift((Product)this.productService.find(((GiftItem)localGiftItem).getGift().getId()));
        ((GiftItem)localGiftItem).setPromotion(promotion);
      }
    }
    if (!beanValidator(redirectAttributes,promotion, new Class[0]))
      return ERROR_PAGE;
    if ((promotion.getBeginDate() != null) && (promotion.getEndDate() != null) && (promotion.getBeginDate().after(promotion.getEndDate())))
      return ERROR_PAGE;
    if ((promotion.getStartPrice() != null) && (promotion.getEndPrice() != null) && (promotion.getStartPrice().compareTo(promotion.getEndPrice()) > 0))
      return ERROR_PAGE;
    if ((promotion.getPriceOperator() == Promotion.Operator.divide) && (promotion.getPriceValue() != null) && (promotion.getPriceValue().compareTo(new BigDecimal(0)) == 0))
      return ERROR_PAGE;
    if ((promotion.getPointOperator() == Promotion.Operator.divide) && (promotion.getPointValue() != null) && (promotion.getPointValue().compareTo(new BigDecimal(0)) == 0))
      return ERROR_PAGE;
    this.promotionService.save(promotion);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("promotion", this.promotionService.find(id));
    model.addAttribute("operators", Promotion.Operator.values());
    model.addAttribute("memberRanks", this.memberRankService.findAll());
    model.addAttribute("productCategories", this.productCategoryService.findAll());
    model.addAttribute("brands", this.brandService.findAll());
    model.addAttribute("coupons", this.couponService.findAll());
    return "/admin/promotion/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Promotion promotion, Long[] memberRankIds, Long[] productCategoryIds, Long[] brandIds, Long[] couponIds, Long[] productIds, RedirectAttributes redirectAttributes)
  {
    promotion.setMemberRanks(new HashSet<MemberRank>(this.memberRankService.findList(memberRankIds)));
    promotion.setProductCategories(new HashSet<ProductCategory>(this.productCategoryService.findList(productCategoryIds)));
    promotion.setBrands(new HashSet<Brand>(this.brandService.findList(brandIds)));
    promotion.setCoupons(new HashSet<Coupon>(this.couponService.findList(couponIds)));
    Iterator<Product> localObject2 = this.productService.findList(productIds).iterator();
    while (localObject2.hasNext())
    {
      Product localObject1 = (Product)localObject2.next();
      if (((Product)localObject1).getIsGift().booleanValue())
        continue;
      promotion.getProducts().add(localObject1);
    }
    Iterator<GiftItem> localObject1 = promotion.getGiftItems().iterator();
    while (localObject1.hasNext())
    {
      GiftItem localGiftItem = (GiftItem)localObject1.next();
      if ((localGiftItem == null) || (((GiftItem)localGiftItem).getGift() == null) || (((GiftItem)localGiftItem).getGift().getId() == null))
      {
        localObject1.remove();
      }
      else
      {
        ((GiftItem)localGiftItem).setGift((Product)this.productService.find(((GiftItem)localGiftItem).getGift().getId()));
        ((GiftItem)localGiftItem).setPromotion(promotion);
      }
    }
    if (!beanValidator(redirectAttributes,promotion, new Class[0]))
      return ERROR_PAGE;
    if ((promotion.getBeginDate() != null) && (promotion.getEndDate() != null) && (promotion.getBeginDate().after(promotion.getEndDate())))
      return ERROR_PAGE;
    if ((promotion.getPriceOperator() == Promotion.Operator.divide) && (promotion.getPriceValue() != null) && (promotion.getPriceValue().compareTo(new BigDecimal(0)) == 0))
      return ERROR_PAGE;
    if ((promotion.getPointOperator() == Promotion.Operator.divide) && (promotion.getPointValue() != null) && (promotion.getPointValue().compareTo(new BigDecimal(0)) == 0))
      return ERROR_PAGE;
    this.promotionService.update(promotion);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.promotionService.findPage(pageable));
    return "/admin/promotion/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.promotionService.delete(ids);
    return ADMIN_SUCCESS;
  }
}