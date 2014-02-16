package com.hongqiang.shop.modules.util.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.content.dao.ArticleDao;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.product.dao.ProductDao;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {
	private static final float WEIGHT = 0.5F;
	private static final int COUNT_NUMBER = 20;

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private ProductDao productDao;

	public void index() {
		index(Article.class);
		index(Product.class);
	}

	public void index(Class<?> type) {
		FullTextEntityManager localFullTextEntityManager = Search
				.getFullTextEntityManager(this.entityManager);
		int i;
		if (type == Article.class)
			for (i = 0; i < this.articleDao.count(new Filter[0]); i += COUNT_NUMBER) {
				List<Article> localList = this.articleDao.findList(
						Integer.valueOf(i), Integer.valueOf(COUNT_NUMBER),
						null, null);
				Iterator<Article> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					Article localObject = (Article) localIterator.next();
					localFullTextEntityManager.index(localObject);
				}
				localFullTextEntityManager.flushToIndexes();
				localFullTextEntityManager.clear();
				this.articleDao.clear();
			}
		else if (type == Product.class)
			for (i = 0; i < this.productDao.count(new Filter[0]); i += COUNT_NUMBER) {
				List<Product> localList = this.productDao.findList(
						Integer.valueOf(i), Integer.valueOf(COUNT_NUMBER),
						null, null);
				Iterator<Product> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					Product localObject = (Product) localIterator.next();
					localFullTextEntityManager.index(localObject);
				}
				localFullTextEntityManager.flushToIndexes();
				localFullTextEntityManager.clear();
				this.productDao.clear();
			}
	}

	public void index(Article article) {
		if (article != null) {
			FullTextEntityManager localFullTextEntityManager = Search
					.getFullTextEntityManager(this.entityManager);
			localFullTextEntityManager.index(article);
		}
	}

	public void index(Product product) {
		if (product != null) {
			FullTextEntityManager localFullTextEntityManager = Search
					.getFullTextEntityManager(this.entityManager);
			localFullTextEntityManager.index(product);
		}
	}

	public void purge() {
		purge(Article.class);
		purge(Product.class);
	}

	public void purge(Class<?> type) {
		FullTextEntityManager localFullTextEntityManager = Search
				.getFullTextEntityManager(this.entityManager);
		if (type == Article.class)
			localFullTextEntityManager.purgeAll(Article.class);
		else if (type == Product.class)
			localFullTextEntityManager.purgeAll(Product.class);
	}

	public void purge(Article article) {
		if (article != null) {
			FullTextEntityManager localFullTextEntityManager = Search
					.getFullTextEntityManager(this.entityManager);
			localFullTextEntityManager.purge(Article.class, article.getId());
		}
	}

	public void purge(Product product) {
		if (product != null) {
			FullTextEntityManager localFullTextEntityManager = Search
					.getFullTextEntityManager(this.entityManager);
			localFullTextEntityManager.purge(Product.class, product.getId());
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Article> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword))
			return new Page<Article>();
		if (pageable == null)
			pageable = new Pageable();
		try {
			String str = QueryParser.escape(keyword);
			QueryParser localQueryParser = new QueryParser(Version.LUCENE_35,
					"title", new IKAnalyzer());
			localQueryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query localQuery = localQueryParser.parse(str);
			FuzzyQuery localFuzzyQuery = new FuzzyQuery(new Term("title", str),
					WEIGHT);
			TermQuery localTermQuery1 = new TermQuery(new Term("content", str));
			TermQuery localTermQuery2 = new TermQuery(new Term("isPublication",
					"true"));
			BooleanQuery localBooleanQuery1 = new BooleanQuery();
			BooleanQuery localBooleanQuery2 = new BooleanQuery();
			localBooleanQuery1.add(localQuery, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localFuzzyQuery, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localTermQuery1, BooleanClause.Occur.SHOULD);
			localBooleanQuery2.add(localTermQuery2, BooleanClause.Occur.MUST);
			localBooleanQuery2
					.add(localBooleanQuery1, BooleanClause.Occur.MUST);
			FullTextEntityManager localFullTextEntityManager = Search
					.getFullTextEntityManager(this.entityManager);
			FullTextQuery localFullTextQuery = localFullTextEntityManager
					.createFullTextQuery(localBooleanQuery2,
							new Class[] { Article.class });
			localFullTextQuery.setSort(new Sort(new SortField[] {
					new SortField("isTop", 3, true), new SortField(null, 0),
					new SortField("createDate", 6, true) }));
			localFullTextQuery.setFirstResult((pageable.getPageNumber() - 1)
					* pageable.getPageSize());
			localFullTextQuery.setMaxResults(pageable.getPageSize());
			return new Page<Article>(localFullTextQuery.getResultList(),
					localFullTextQuery.getResultSize(), pageable);
		} catch (ParseException localParseException1) {
			localParseException1.printStackTrace();
		}
		return new Page<Article>();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Product> search(String keyword, BigDecimal startPrice,
			BigDecimal endPrice, Product.OrderType orderType, Pageable pageable) {
		if (StringUtils.isEmpty(keyword))
			return new Page<Product>();
		if (pageable == null)
			pageable = new Pageable();
		try {
			String str = QueryParser.escape(keyword);
			TermQuery localTermQuery1 = new TermQuery(new Term("sn", str));
			Query localQuery1 = new QueryParser(Version.LUCENE_35, "keyword",
					new IKAnalyzer()).parse(str);
			QueryParser localQueryParser = new QueryParser(Version.LUCENE_35,
					"name", new IKAnalyzer());
			localQueryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query localQuery2 = localQueryParser.parse(str);
			FuzzyQuery localFuzzyQuery = new FuzzyQuery(new Term("name", str),
					WEIGHT);
			TermQuery localTermQuery2 = new TermQuery(new Term("introduction",
					str));
			TermQuery localTermQuery3 = new TermQuery(new Term("isMarketable",
					"true"));
			TermQuery localTermQuery4 = new TermQuery(
					new Term("isList", "true"));
			TermQuery localTermQuery5 = new TermQuery(new Term("isGift",
					"false"));
			BooleanQuery localBooleanQuery1 = new BooleanQuery();
			BooleanQuery localBooleanQuery2 = new BooleanQuery();
			localBooleanQuery1.add(localTermQuery1, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localQuery1, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localQuery2, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localFuzzyQuery, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localTermQuery2, BooleanClause.Occur.SHOULD);
			localBooleanQuery2.add(localTermQuery3, BooleanClause.Occur.MUST);
			localBooleanQuery2.add(localTermQuery4, BooleanClause.Occur.MUST);
			localBooleanQuery2.add(localTermQuery5, BooleanClause.Occur.MUST);
			localBooleanQuery2
					.add(localBooleanQuery1, BooleanClause.Occur.MUST);
			if ((startPrice != null) && (endPrice != null)
					&& (startPrice.compareTo(endPrice) > 0)) {
				BigDecimal localObject = startPrice;
				startPrice = endPrice;
				endPrice = (BigDecimal) localObject;
			}
			if ((startPrice != null)
					&& (startPrice.compareTo(new BigDecimal(0)) >= 0)
					&& (endPrice != null)
					&& (endPrice.compareTo(new BigDecimal(0)) >= 0)) {
				Query localObject = NumericRangeQuery.newDoubleRange("price",
						Double.valueOf(startPrice.doubleValue()),
						Double.valueOf(endPrice.doubleValue()), true, true);
				localBooleanQuery2.add((Query) localObject,
						BooleanClause.Occur.MUST);
			} else if ((startPrice != null)
					&& (startPrice.compareTo(new BigDecimal(0)) >= 0)) {
				Query localObject = NumericRangeQuery.newDoubleRange("price",
						Double.valueOf(startPrice.doubleValue()), null, true,
						false);
				localBooleanQuery2.add((Query) localObject,
						BooleanClause.Occur.MUST);
			} else if ((endPrice != null)
					&& (endPrice.compareTo(new BigDecimal(0)) >= 0)) {
				Query localObject = NumericRangeQuery.newDoubleRange("price",
						null, Double.valueOf(endPrice.doubleValue()), false,
						true);
				localBooleanQuery2.add((Query) localObject,
						BooleanClause.Occur.MUST);
			}
			Object localObject = Search
					.getFullTextEntityManager(this.entityManager);
			FullTextQuery localFullTextQuery = ((FullTextEntityManager) localObject)
					.createFullTextQuery(localBooleanQuery2,
							new Class[] { Product.class });
			SortField[] arrayOfSortField = null;
			if (orderType == Product.OrderType.priceAsc)
				arrayOfSortField = new SortField[] {
						new SortField("price", 7, false),
						new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.priceDesc)
				arrayOfSortField = new SortField[] {
						new SortField("price", 7, true),
						new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.salesDesc)
				arrayOfSortField = new SortField[] {
						new SortField("sales", 4, true),
						new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.scoreDesc)
				arrayOfSortField = new SortField[] {
						new SortField("score", 4, true),
						new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.dateDesc)
				arrayOfSortField = new SortField[] { new SortField(
						"createDate", 6, true) };
			else
				arrayOfSortField = new SortField[] {
						new SortField("isTop", 3, true),
						new SortField(null, 0),
						new SortField("updateDate", 6, true) };
			localFullTextQuery.setSort(new Sort(arrayOfSortField));
			localFullTextQuery.setFirstResult((pageable.getPageNumber() - 1)
					* pageable.getPageSize());
			localFullTextQuery.setMaxResults(pageable.getPageSize());
			return new Page<Product>(localFullTextQuery.getResultList(),
					localFullTextQuery.getResultSize(), pageable);
		} catch (Exception localException1) {
			localException1.printStackTrace();
		}
		return new Page<Product>();
	}
}