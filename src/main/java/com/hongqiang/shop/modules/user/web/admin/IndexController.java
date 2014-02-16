package com.hongqiang.shop.modules.user.web.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.ArticleService;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.util.service.SearchService;

@Controller("adminIndexController")
@RequestMapping({ "${adminPath}/index" })
public class IndexController extends BaseController {
	public enum BuildType {
		article, product;
	}

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ProductService productService;

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = { "/build" }, method = RequestMethod.GET)
	public String build(ModelMap model) {
		model.addAttribute("buildTypes", BuildType.values());
		return "/admin/index/build";
	}

	@RequestMapping(value = { "/build" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> build(BuildType buildType, Boolean isPurge,
			Integer first, Integer count) {
		long beginTime = System.currentTimeMillis();
		if ((first == null) || (first.intValue() < 0))
			first = Integer.valueOf(0);
		if ((count == null) || (count.intValue() <= 0))
			count = Integer.valueOf(50);
		int i = 0;
		boolean bool = true;
		if (buildType == BuildType.article) {
			if ((first.intValue() == 0) && (isPurge != null)
					&& (isPurge.booleanValue()))
				this.searchService.purge(Article.class);
			List<Article> localList = this.articleService.findList(null, null,
					null, first, count);
			Iterator<Article> articleIterator = localList.iterator();
			while (articleIterator.hasNext()) {
				Article article = (Article) articleIterator.next();
				this.searchService.index(article);
				i++;
			}
			first = Integer.valueOf(first.intValue() + localList.size());
			if (localList.size() == count.intValue())
				bool = false;
		} else if (buildType == BuildType.product) {
			if ((first.intValue() == 0) && (isPurge != null)
					&& (isPurge.booleanValue()))
				this.searchService.purge(Product.class);
			List<Product> localList = this.productService.findList(null, null,
					null, first, count);
			Iterator<Product> productIterator = localList.iterator();
			while (productIterator.hasNext()) {
				Product product = (Product) productIterator.next();
				this.searchService.index(product);
				i++;
			}
			first = Integer.valueOf(first.intValue() + localList.size());
			if (localList.size() == count.intValue())
				bool = false;
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