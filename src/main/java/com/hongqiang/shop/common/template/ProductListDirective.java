package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.account.service.PromotionService;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.entity.Promotion;
import com.hongqiang.shop.modules.product.service.AttributeService;
import com.hongqiang.shop.modules.product.service.BrandService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.product.service.TagService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("productListDirective")
public class ProductListDirective extends BaseDirective {
	private static final String PRODUCTCATEGORY_ID = "productCategoryId";
	private static final String BRAND_ID = "brandId";
	private static final String PROMOTION_ID = "promotionId";
	private static final String TAG_IDS = "tagIds";
	private static final String ATTRIBUTEVALUE = "attributeValue";
	private static final String START_PRICE = "startPrice";
	private static final String END_PRICE = "endPrice";
	private static final String ORDER_TYPE = "orderType";
	private static final String PRODUCTS = "products";

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private TagService tagService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Long localLong1 = (Long) FreeMarkers.getParameter(PRODUCTCATEGORY_ID,
				Long.class, params);
		Long localLong2 = (Long) FreeMarkers.getParameter(BRAND_ID, Long.class,
				params);
		Long localLong3 = (Long) FreeMarkers.getParameter(PROMOTION_ID,
				Long.class, params);
		 Long[] arrayOfLong = (Long[])FreeMarkers.getParameter(TAG_IDS,
		 Long[].class, params);
		Map localMap = (Map) FreeMarkers.getParameter(ATTRIBUTEVALUE,
				Map.class, params);
		BigDecimal localBigDecimal1 = (BigDecimal) FreeMarkers.getParameter(
				START_PRICE, BigDecimal.class, params);
		BigDecimal localBigDecimal2 = (BigDecimal) FreeMarkers.getParameter(
				END_PRICE, BigDecimal.class, params);
		Product.OrderType localOrderType = (Product.OrderType) FreeMarkers
				.getParameter(ORDER_TYPE, Product.OrderType.class, params);
		ProductCategory localProductCategory = (ProductCategory) this.productCategoryService
				.find(localLong1);
		Brand localBrand = (Brand) this.brandService.find(localLong2);
		Promotion localPromotion = (Promotion) this.promotionService
				.find(localLong3);
		List localList1 = this.tagService.findList(arrayOfLong);
		HashMap localHashMap = new HashMap();
		Object localObject1;
		Object localObject2;
		if (localMap != null) {
			Iterator localIterator = localMap.entrySet().iterator();
			while (localIterator.hasNext()) {
				localObject1 = (Map.Entry) localIterator.next();
				localObject2 = (Attribute) this.attributeService
						.find((Long) ((Map.Entry) localObject1).getKey());
				if (localObject2 == null)
					continue;
				localHashMap.put(localObject2,
						(String) ((Map.Entry) localObject1).getValue());
			}
		}
		if (((localLong1 != null) && (localProductCategory == null))
				|| ((localLong2 != null) && (localBrand == null))
				|| ((localLong3 != null) && (localPromotion == null))
				|| ((arrayOfLong != null) && (localList1.isEmpty()))
				|| ((localMap != null) && (localHashMap.isEmpty()))) {
			localObject1 = new ArrayList();
		} else {
			boolean bool = setFreemarker(env, params);
			localObject2 = getFreemarkerCacheRegion(env, params);
			Integer localInteger = getFreemarkerCount(params);
			List localList2 = getFreemarkerFilter(params, Article.class,
					new String[0]);
			List localList3 = getFreemarkerOrder(params, new String[0]);
			if (bool)
				localObject1 = this.productService.findList(
						localProductCategory, localBrand, localPromotion,
						localList1, localHashMap, localBigDecimal1,
						localBigDecimal2, Boolean.valueOf(true),
						Boolean.valueOf(true), null, Boolean.valueOf(false),
						null, null, localOrderType, localInteger, localList2,
						localList3, (String) localObject2);
			else
				localObject1 = this.productService.findList(
						localProductCategory, localBrand, localPromotion,
						localList1, localHashMap, localBigDecimal1,
						localBigDecimal2, Boolean.valueOf(true),
						Boolean.valueOf(true), null, Boolean.valueOf(false),
						null, null, localOrderType, localInteger, localList2,
						localList3);
		}
		setFreemarker(PRODUCTS, localObject1, env, body);
	}
}