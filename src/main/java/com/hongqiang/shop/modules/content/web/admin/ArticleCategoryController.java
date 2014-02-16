package com.hongqiang.shop.modules.content.web.admin;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;

@Controller("adminArticleCategoryController")
@RequestMapping({"${adminPath}/article_category"})
public class ArticleCategoryController extends BaseController
{

  @Autowired
  private ArticleCategoryService articleCategoryService;

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    return "/admin/article_category/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(ArticleCategory articleCategory, Long parentId, RedirectAttributes redirectAttributes)
  {
    articleCategory.setParent((ArticleCategory)this.articleCategoryService.find(parentId));
    if (!beanValidator(redirectAttributes,articleCategory, new Class[0]))
      return ERROR_PAGE;
    articleCategory.setTreePath(null);
    articleCategory.setGrade(null);
    articleCategory.setChildren(null);
    articleCategory.setArticles(null);
    this.articleCategoryService.save(articleCategory);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    ArticleCategory localArticleCategory = (ArticleCategory)this.articleCategoryService.find(id);
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    model.addAttribute("articleCategory", localArticleCategory);
    model.addAttribute("children", this.articleCategoryService.findChildren(localArticleCategory));
    return "/admin/article_category/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(ArticleCategory articleCategory, Long parentId, RedirectAttributes redirectAttributes)
  {
    articleCategory.setParent((ArticleCategory)this.articleCategoryService.find(parentId));
    if (!beanValidator(redirectAttributes,articleCategory, new Class[0]))
      return ERROR_PAGE;
    if (articleCategory.getParent() != null)
    {
      ArticleCategory localArticleCategory = articleCategory.getParent();
      if (localArticleCategory.equals(articleCategory))
        return ERROR_PAGE;
      List<ArticleCategory> localList = this.articleCategoryService.findChildren(localArticleCategory);
      if ((localList != null) && (localList.contains(localArticleCategory)))
        return ERROR_PAGE;
    }
    this.articleCategoryService.update(articleCategory, new String[] { "treePath", "grade", "children", "articles" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(ModelMap model)
  {
    model.addAttribute("articleCategoryTree", this.articleCategoryService.findTree());
    return "/admin/article_category/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long id)
  {
    ArticleCategory localArticleCategory = (ArticleCategory)this.articleCategoryService.find(id);
    if (localArticleCategory == null)
      return ADMIN_ERROR;
    Set<ArticleCategory> localSet1 = localArticleCategory.getChildren();
    if ((localSet1 != null) && (!localSet1.isEmpty()))
      return Message.error("admin.articleCategory.deleteExistChildrenNotAllowed", new Object[0]);
    Set<Article> localSet2 = localArticleCategory.getArticles();
    if ((localSet2 != null) && (!localSet2.isEmpty()))
      return Message.error("admin.articleCategory.deleteExistArticleNotAllowed", new Object[0]);
    this.articleCategoryService.delete(id);
    return ADMIN_SUCCESS;
  }
}