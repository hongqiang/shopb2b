package com.hongqiang.shop.modules.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.core.io.ClassPathResource;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.common.utils.StringUtils;
import com.hongqiang.shop.common.utils.model.CommonAttributes;

//文章
@Indexed
@Entity
@Table(name = "hq_article")
public class Article extends BaseEntity {
	private static final long serialVersionUID = 1475773294701585482L;
	private static final int MAX_PAGE_CONTENT_COUNT = 800;// 内容分页每页最大字数
	private static final String contentBreake = "<hr class=\"pageBreak\" />";
	private static final Pattern pattern = Pattern.compile("[,;\\.!?，；。！？]");
	private static String staticPath;
	private String title;// 标题
	private String author;// 作者
	private String content;// 内容
	private String seoTitle;//
	private String seoKeywords;//
	private String seoDescription;//
	private Boolean isPublication;// 是否发布
	private Boolean isTop;// 是否置顶
	private Long hits;// 点击数
	private Integer pageNumber;// 文章页数
	private ArticleCategory articleCategory;// 文章分类
	private Set<Tag> tags = new HashSet<Tag>();

	static {
		try {
			File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
			Document localDocument = new SAXReader().read(localFile);
			Element localElement = (org.dom4j.Element) localDocument
					.selectSingleNode("/shophq/template[@id='articleContent']");
			staticPath = localElement.attributeValue("staticPath");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	@Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	public String getContent() {
		if (this.pageNumber != null) {
			String[] arrayOfString = getPageContents();
			if (this.pageNumber.intValue() < 1)
				this.pageNumber = Integer.valueOf(1);
			if (this.pageNumber.intValue() > arrayOfString.length)
				this.pageNumber = Integer.valueOf(arrayOfString.length);
			return arrayOfString[(this.pageNumber.intValue() - 1)];
		}
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(max = 200)
	public String getSeoTitle() {
		return this.seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	@Length(max = 200)
	public String getSeoKeywords() {
		return this.seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		if (seoKeywords != null)
			seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",")
					.replaceAll("^,|,$", "");
		this.seoKeywords = seoKeywords;
	}

	@Length(max = 200)
	public String getSeoDescription() {
		return this.seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Field(store = Store.YES, index = Index.YES)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsPublication() {
		return this.isPublication;
	}

	public void setIsPublication(Boolean isPublication) {
		this.isPublication = isPublication;
	}

	@Field(store = Store.YES, index = Index.YES)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsTop() {
		return this.isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	@Column(nullable = false)
	public Long getHits() {
		return this.hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	@Transient
	public Integer getPageNumber() {
		return this.pageNumber;
	}

	@Transient
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public ArticleCategory getArticleCategory() {
		return this.articleCategory;
	}

	public void setArticleCategory(ArticleCategory articleCategory) {
		this.articleCategory = articleCategory;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hq_article_tag")
	@OrderBy("order asc")
	public Set<Tag> getTags() {
		return this.tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Transient
	public String getPath() {
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		localHashMap.put("id", getId());
		localHashMap.put("createDate", getCreateDate());
		localHashMap.put("updateDate", getUpdateDate());
		localHashMap.put("title", getTitle());
		localHashMap.put("seoTitle", getSeoTitle());
		localHashMap.put("seoKeywords", getSeoKeywords());
		localHashMap.put("seoDescription", getSeoDescription());
		localHashMap.put("pageNumber", getPageNumber());
		localHashMap.put("articleCategory", getArticleCategory());
		return FreeMarkers.renderString(staticPath, localHashMap);
	}

	@Transient
	public String getText() {
		if (getContent() != null)
			return Jsoup.parse(getContent()).text();
		return null;
	}

	@Transient
	public String[] getPageContents() {
		if (StringUtils.isEmpty(this.content))
			return new String[] { "" };
		if (this.content.contains(contentBreake))
			return this.content.split(contentBreake);
		ArrayList<String> localArrayList = new ArrayList<String>();
		org.jsoup.nodes.Document localDocument = Jsoup.parse(this.content);
		List<Node> localList = localDocument.body().childNodes();
		if (localList != null) {
			int i = 0;
			StringBuffer localStringBuffer = new StringBuffer();
			Iterator<Node> localIterator = localList.iterator();
			while (localIterator.hasNext()) {
				Node localObject1 = (Node) localIterator.next();
				Object localObject2;
				if ((localObject1 instanceof org.jsoup.nodes.Element)) {
					localObject2 = (org.jsoup.nodes.Element) localObject1;
					localStringBuffer
							.append(((org.jsoup.nodes.Element) localObject2)
									.outerHtml());
					i += ((org.jsoup.nodes.Element) localObject2).text()
							.length();
					if (i < MAX_PAGE_CONTENT_COUNT)
						continue;
					localArrayList.add(localStringBuffer.toString());
					i = 0;
					localStringBuffer.setLength(0);
				} else {
					if (!(localObject1 instanceof TextNode))
						continue;
					localObject2 = (TextNode) localObject1;
					String str1 = ((TextNode) localObject2).text();
					String[] arrayOfString1 = pattern.split(str1);
					Matcher localMatcher = pattern.matcher(str1);
					for (String str2 : arrayOfString1) {
						if (localMatcher.find())
							str2 = str2 + localMatcher.group();
						localStringBuffer.append(str2);
						i += str2.length();
						if (i < MAX_PAGE_CONTENT_COUNT)
							continue;
						localArrayList.add(localStringBuffer.toString());
						i = 0;
						localStringBuffer.setLength(0);
					}
				}
			}
			String localObject1 = localStringBuffer.toString();
			if (StringUtils.isNotEmpty((String) localObject1))
				localArrayList.add(localObject1);
		}
		return (String[]) localArrayList.toArray(new String[localArrayList
				.size()]);
	}

	@Transient
	public int getTotalPages() {
		return getPageContents().length;
	}
}