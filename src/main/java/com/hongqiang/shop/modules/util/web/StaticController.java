package com.hongqiang.shop.modules.util.web;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.content.service.ArticleService;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.util.service.StaticService;

@Controller("adminStaticController")
@RequestMapping({ "${adminPath}/static" })
public class StaticController extends BaseController {

	public enum BuildType {
		index, article, product, other;
	}

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArticleCategoryService articleCategoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private StaticService staticService;

	@RequestMapping(value = { "/build" }, method = RequestMethod.GET)
	public String build(ModelMap model) {
		model.addAttribute("buildTypes", BuildType.values());
		model.addAttribute("defaultBeginDate",
				DateUtils.addDays(new Date(), -7));
		model.addAttribute("defaultEndDate", new Date());
		model.addAttribute("articleCategoryTree",
				this.articleCategoryService.findChildren(null, null));
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findChildren(null, null));
		return "/admin/static/build";
	}

	@RequestMapping(value = { "/build" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> build(BuildType buildType,
			Long articleCategoryId, Long productCategoryId, Date beginDate,
			Date endDate, Integer first, Integer count) {
		long beginTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		if (beginDate != null) {
			calendar.setTime(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			calendar.setTime(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		if ((first == null) || (first.intValue() < 0))
			first = Integer.valueOf(0);
		if ((count == null) || (count.intValue() <= 0))
			count = Integer.valueOf(50);
		int i = 0;
		boolean bool = true;
		if (buildType == BuildType.index) {
			i = this.staticService.buildIndex();
		} else {
			if (buildType == BuildType.article) {
				ArticleCategory articleCategory = (ArticleCategory) this.articleCategoryService
						.find(articleCategoryId);
				List<Article> localList = this.articleService.findList(
						articleCategory, beginDate, endDate, first, count);
				Iterator<Article> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					Article article = (Article) localIterator.next();
					i += this.staticService.build(article);
				}
				first = Integer.valueOf(first.intValue() + localList.size());
				if (localList.size() == count.intValue())
					bool = false;
			} else if (buildType == BuildType.product) {
				ProductCategory productCategory = (ProductCategory) this.productCategoryService
						.find(productCategoryId);
				List<Product> localList = this.productService.findList(
						productCategory, beginDate, endDate, first, count);
				Iterator<Product> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					Product product = (Product) localIterator.next();
					i += this.staticService.build(product);
				}
				first = Integer.valueOf(first.intValue() + localList.size());
				if (localList.size() == count.intValue())
					bool = false;
			} else if (buildType == BuildType.other) {
				i = this.staticService.buildOther();
			}
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("first", first);
		map.put("buildCount", Integer.valueOf(i));
		map.put("buildTime", Long.valueOf(endTime - beginTime));
		map.put("isCompleted", Boolean.valueOf(bool));
		return map;
	}
}