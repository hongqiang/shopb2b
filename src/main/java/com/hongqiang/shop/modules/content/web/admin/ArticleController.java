package com.hongqiang.shop.modules.content.web.admin;

import java.util.HashSet;

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
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.content.service.ArticleService;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.entity.Tag;
import com.hongqiang.shop.modules.product.service.TagService;

@Controller("adminArticleController")
@RequestMapping({"${adminPath}/article"})
public class ArticleController extends BaseController
{

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleCategoryService articleCategoryService;

  @Autowired
  private TagService tagService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    model.addAttribute("tags", this.tagService.findList(Tag.Type.article));
    return "/admin/article/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Article article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes)
  {
    article.setArticleCategory((ArticleCategory)this.articleCategoryService.find(articleCategoryId));
    article.setTags(new HashSet<Tag>(this.tagService.findList(tagIds)));
    if (!beanValidator(redirectAttributes,article, new Class[0]))
      return ERROR_PAGE;
    article.setHits(Long.valueOf(0L));
    article.setPageNumber(null);
    this.articleService.save(article);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    model.addAttribute("tags", this.tagService.findList(Tag.Type.article));
    model.addAttribute("article", this.articleService.find(id));
    return "/admin/article/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Article article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes)
  {
    article.setArticleCategory((ArticleCategory)this.articleCategoryService.find(articleCategoryId));
    article.setTags(new HashSet<Tag>(this.tagService.findList(tagIds)));
    if (!beanValidator(redirectAttributes,article, new Class[0]))
      return ERROR_PAGE;
    this.articleService.update(article, new String[] { "hits", "pageNumber" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.articleService.findPage(pageable));
    return "/admin/article/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.articleService.delete(ids);
    return ADMIN_SUCCESS;
  }
}