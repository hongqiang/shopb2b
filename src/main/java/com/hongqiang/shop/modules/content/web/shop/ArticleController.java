package com.hongqiang.shop.modules.content.web.shop;

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
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.content.service.ArticleService;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.util.service.SearchService;

@Controller("shopArticleController")
@RequestMapping({"${frontPath}/article"})
public class ArticleController extends BaseController
{
  private static final int PAGE_NUMBER = 20;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleCategoryService articleCategoryService;

  @Autowired
  private SearchService searchService;

  @RequestMapping(value={"/list/{id}"}, method=RequestMethod.GET)
  public String list(@PathVariable Long id, Integer pageNumber, ModelMap model)
  {
    ArticleCategory localArticleCategory = (ArticleCategory)this.articleCategoryService.find(id);
    if (localArticleCategory == null)
      throw new ResourceNotFoundException();
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_NUMBER));
    model.addAttribute("articleCategory", localArticleCategory);
    model.addAttribute("page", this.articleService.findPage(localArticleCategory, null, localPageable));
    return "/shop/article/list";
  }

  @RequestMapping(value={"/search"}, method=RequestMethod.GET)
  public String search(String keyword, Integer pageNumber, ModelMap model)
  {
	  System.out.println("keyword="+keyword);
    if (StringUtils.isEmpty(keyword))
      return SHOP_ERROR_PAGE;
    Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(PAGE_NUMBER));
    model.addAttribute("articleKeyword", keyword);
    model.addAttribute("page", this.searchService.search(keyword, localPageable));
    return "shop/article/search";
  }

  @RequestMapping(value={"/hits/{id}"}, method=RequestMethod.GET)
  @ResponseBody
  public Long hits(@PathVariable Long id)
  {
    return Long.valueOf(this.articleService.viewHits(id));
  }
}