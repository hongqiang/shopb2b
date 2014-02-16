package com.hongqiang.shop.modules.content.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.modules.entity.ArticleCategory;

@Repository
public class ArticleCategoryDaoImpl extends BaseDaoImpl<ArticleCategory,Long>
  implements ArticleCategoryDaoCustom
{
  public List<ArticleCategory> findRoots(Integer count)
  {
    String str = "select articleCategory from ArticleCategory articleCategory where articleCategory.parent is null order by articleCategory.order asc";
    TypedQuery<ArticleCategory> localTypedQuery = this.getEntityManager().createQuery(str, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
    if (count != null)
      localTypedQuery.setMaxResults(count.intValue());
    return localTypedQuery.getResultList();
  }

  public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count)
  {
    if ((articleCategory == null) || (articleCategory.getParent() == null))
      return Collections.emptyList();
    String str = "select articleCategory from ArticleCategory articleCategory where articleCategory.id in (:ids) order by articleCategory.grade asc";
    TypedQuery<ArticleCategory> localTypedQuery = this.getEntityManager().createQuery(str, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", articleCategory.getTreePaths());
    if (count != null)
      localTypedQuery.setMaxResults(count.intValue());
    return localTypedQuery.getResultList();
  }

  public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count)
  {
    String str;
    TypedQuery<ArticleCategory> localTypedQuery;
    if (articleCategory != null)
    {
      str = "select articleCategory from ArticleCategory articleCategory where articleCategory.treePath like :treePath order by articleCategory.order asc";
      localTypedQuery = this.getEntityManager().createQuery(str, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%," + articleCategory.getId() + "," + "%");
    }
    else
    {
      str = "select articleCategory from ArticleCategory articleCategory order by articleCategory.order asc";
      localTypedQuery = this.getEntityManager().createQuery(str, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
    }
    if (count != null)
      localTypedQuery.setMaxResults(count.intValue());
    return packTheChildren(localTypedQuery.getResultList(), articleCategory);
  }

  public void persist(ArticleCategory articleCategory)
  {
    setTreepathAndGrade(articleCategory);
    super.persist(articleCategory);
  }

  public ArticleCategory merge(ArticleCategory articleCategory)
  {
    setTreepathAndGrade(articleCategory);
    Iterator<ArticleCategory> localIterator = findChildren(articleCategory, null).iterator();
    while (localIterator.hasNext())
    {
      ArticleCategory localArticleCategory = (ArticleCategory)localIterator.next();
      setTreepathAndGrade(localArticleCategory);
    }
    return (ArticleCategory)super.merge(articleCategory);
  }

  private List<ArticleCategory> packTheChildren(List<ArticleCategory> paramList, ArticleCategory paramArticleCategory)
  {
    ArrayList<ArticleCategory> localArrayList = new ArrayList<ArticleCategory>();
    if (paramList != null)
    {
      Iterator<ArticleCategory> localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        ArticleCategory localArticleCategory = (ArticleCategory)localIterator.next();
        if (localArticleCategory.getParent() != paramArticleCategory)
          continue;
        localArrayList.add(localArticleCategory);
        localArrayList.addAll(packTheChildren(paramList, localArticleCategory));
      }
    }
    return localArrayList;
  }

  private void setTreepathAndGrade(ArticleCategory paramArticleCategory)
  {
    if (paramArticleCategory == null)
      return;
    ArticleCategory localArticleCategory = paramArticleCategory.getParent();
    if (localArticleCategory != null)
      paramArticleCategory.setTreePath(localArticleCategory.getTreePath() + localArticleCategory.getId() + ",");
    else
      paramArticleCategory.setTreePath(",");
    paramArticleCategory.setGrade(Integer.valueOf(paramArticleCategory.getTreePaths().size()));
  }
}