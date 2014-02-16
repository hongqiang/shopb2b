package com.hongqiang.shop.modules.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.io.ClassPathResource;
import org.wltea.analyzer.lucene.IKAnalyzer;
import com.hongqiang.shop.common.utils.model.CommonAttributes;
import com.hongqiang.shop.common.utils.BigDecimalNumericFieldBridge;
import com.hongqiang.shop.common.utils.FreeMarkers;


@Indexed
@Entity
@Table(name = "hq_product")
public class Product extends BaseEntity {
	public enum OrderType {
		topDesc, priceAsc, priceDesc, salesDesc, scoreDesc, dateDesc;
	}

	private static final long serialVersionUID = 2167830430439593293L;
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";
	public static final String FULL_NAME_SPECIFICATION_PREFIX = "[";
	public static final String FULL_NAME_SPECIFICATION_SUFFIX = "]";
	public static final String FULL_NAME_SPECIFICATION_SEPARATOR = " ";
	private static String staticPath;
	private String sn;
	private String name;
	private String fullName;
	private BigDecimal price;
	private BigDecimal cost;
	private BigDecimal marketPrice;
	private String image;
	private String unit;
	private Integer weight;
	private Integer stock;
	private Integer allocatedStock;
	private String stockMemo;
	private Long point;
	private Boolean isMarketable;
	private Boolean isList;
	private Boolean isTop;
	private Boolean isGift;
	private String introduction;
	private String memo;
	private String keyword;
	private String seoTitle;
	private String seoKeywords;
	private String seoDescription;
	private Float score;
	private Long totalScore;
	private Long scoreCount;
	private Long hits;
	private Long weekHits;
	private Long monthHits;
	private Long sales;
	private Long weekSales;
	private Long monthSales;
	private Date weekHitsDate;
	private Date monthHitsDate;
	private Date weekSalesDate;
	private Date monthSalesDate;
	private String attributeValue0;
	private String attributeValue1;
	private String attributeValue2;
	private String attributeValue3;
	private String attributeValue4;
	private String attributeValue5;
	private String attributeValue6;
	private String attributeValue7;
	private String attributeValue8;
	private String attributeValue9;
	private String attributeValue10;
	private String attributeValue11;
	private String attributeValue12;
	private String attributeValue13;
	private String attributeValue14;
	private String attributeValue15;
	private String attributeValue16;
	private String attributeValue17;
	private String attributeValue18;
	private String attributeValue19;
	private ProductCategory productCategory;
	private Goods goods;
	private Brand brand;
	private List<ProductImage> productImages = new ArrayList<ProductImage>();
	private Set<Review> reviews = new HashSet<Review>();
	private Set<Consultation> consultations = new HashSet<Consultation>();
	private Set<Tag> tags = new HashSet<Tag>();
	private Set<Member> favoriteMembers = new HashSet<Member>();
	private Set<Specification> specifications = new HashSet<Specification>();
	private Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();
	private Set<Promotion> promotions = new HashSet<Promotion>();
	private Set<CartItem> cartItems = new HashSet<CartItem>();
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	private Set<GiftItem> giftItems = new HashSet<GiftItem>();
	private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();
	private Map<MemberRank, BigDecimal> memberPrice = new HashMap<MemberRank, BigDecimal>();
	private Map<Parameter, String> parameterValue = new HashMap<Parameter, String>();

	static {
		try {
			File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
			Document localDocument = new SAXReader().read(localFile);
			Element localElement = (Element) localDocument
					.selectSingleNode("/shophq/template[@id='productContent']");
			staticPath = localElement.attributeValue("staticPath");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.YES)
	@Pattern(regexp = "^[0-9a-zA-Z_-]*$")
	@Length(max = 200)
	@Column(nullable = false, unique = true)
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.YES)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@NotNull
	@Min(0L)
//	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Min(0L)
//	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Min(0L)
//	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getMarketPrice() {
		return this.marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Min(0L)
	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Min(0L)
	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Integer getAllocatedStock() {
		return this.allocatedStock;
	}

	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	@Length(max = 200)
	public String getStockMemo() {
		return this.stockMemo;
	}

	public void setStockMemo(String stockMemo) {
		this.stockMemo = stockMemo;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Min(0L)
	@Column(nullable = false)
	public Long getPoint() {
		return this.point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	@Field(store = Store.YES, index = Index.YES)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsMarketable() {
		return this.isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	@Field(store = Store.YES, index = Index.YES)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsList() {
		return this.isList;
	}

	public void setIsList(Boolean isList) {
		this.isList = isList;
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

	@JsonProperty
	@Field(store = Store.YES, index = Index.YES)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsGift() {
		return this.isGift;
	}

	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}

	@Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Length(max = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Length(max = 200)
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		if (keyword != null)
			keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll(
					"^,|,$", "");
		this.keyword = keyword;
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
	@NumericField
	@Column(nullable = false, precision = 12, scale = 6)
	public Float getScore() {
		return this.score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Column(nullable = false)
	public Long getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	@Field(store = Store.YES, index = Index.YES)
	@Column(nullable = false)
	public Long getScoreCount() {
		return this.scoreCount;
	}

	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}

	@Field(store = Store.YES, index = Index.YES)
	@Column(nullable = false)
	public Long getHits() {
		return this.hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getWeekHits() {
		return this.weekHits;
	}

	public void setWeekHits(Long weekHits) {
		this.weekHits = weekHits;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getMonthHits() {
		return this.monthHits;
	}

	public void setMonthHits(Long monthHits) {
		this.monthHits = monthHits;
	}

	@Field(store = Store.YES, index = Index.YES)
	@Column(nullable = false)
	public Long getSales() {
		return this.sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getWeekSales() {
		return this.weekSales;
	}

	public void setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getMonthSales() {
		return this.monthSales;
	}

	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	@Column(nullable = false)
	public Date getWeekHitsDate() {
		return this.weekHitsDate;
	}

	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	@Column(nullable = false)
	public Date getMonthHitsDate() {
		return this.monthHitsDate;
	}

	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	@Column(nullable = false)
	public Date getWeekSalesDate() {
		return this.weekSalesDate;
	}

	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	@Column(nullable = false)
	public Date getMonthSalesDate() {
		return this.monthSalesDate;
	}

	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}

	@Length(max = 200)
	public String getAttributeValue0() {
		return this.attributeValue0;
	}

	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	@Length(max = 200)
	public String getAttributeValue1() {
		return this.attributeValue1;
	}

	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	@Length(max = 200)
	public String getAttributeValue2() {
		return this.attributeValue2;
	}

	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	@Length(max = 200)
	public String getAttributeValue3() {
		return this.attributeValue3;
	}

	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	@Length(max = 200)
	public String getAttributeValue4() {
		return this.attributeValue4;
	}

	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	@Length(max = 200)
	public String getAttributeValue5() {
		return this.attributeValue5;
	}

	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	@Length(max = 200)
	public String getAttributeValue6() {
		return this.attributeValue6;
	}

	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	@Length(max = 200)
	public String getAttributeValue7() {
		return this.attributeValue7;
	}

	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	@Length(max = 200)
	public String getAttributeValue8() {
		return this.attributeValue8;
	}

	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	@Length(max = 200)
	public String getAttributeValue9() {
		return this.attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	@Length(max = 200)
	public String getAttributeValue10() {
		return this.attributeValue10;
	}

	public void setAttributeValue10(String attributeValue10) {
		this.attributeValue10 = attributeValue10;
	}

	@Length(max = 200)
	public String getAttributeValue11() {
		return this.attributeValue11;
	}

	public void setAttributeValue11(String attributeValue11) {
		this.attributeValue11 = attributeValue11;
	}

	@Length(max = 200)
	public String getAttributeValue12() {
		return this.attributeValue12;
	}

	public void setAttributeValue12(String attributeValue12) {
		this.attributeValue12 = attributeValue12;
	}

	@Length(max = 200)
	public String getAttributeValue13() {
		return this.attributeValue13;
	}

	public void setAttributeValue13(String attributeValue13) {
		this.attributeValue13 = attributeValue13;
	}

	@Length(max = 200)
	public String getAttributeValue14() {
		return this.attributeValue14;
	}

	public void setAttributeValue14(String attributeValue14) {
		this.attributeValue14 = attributeValue14;
	}

	@Length(max = 200)
	public String getAttributeValue15() {
		return this.attributeValue15;
	}

	public void setAttributeValue15(String attributeValue15) {
		this.attributeValue15 = attributeValue15;
	}

	@Length(max = 200)
	public String getAttributeValue16() {
		return this.attributeValue16;
	}

	public void setAttributeValue16(String attributeValue16) {
		this.attributeValue16 = attributeValue16;
	}

	@Length(max = 200)
	public String getAttributeValue17() {
		return this.attributeValue17;
	}

	public void setAttributeValue17(String attributeValue17) {
		this.attributeValue17 = attributeValue17;
	}

	@Length(max = 200)
	public String getAttributeValue18() {
		return this.attributeValue18;
	}

	public void setAttributeValue18(String attributeValue18) {
		this.attributeValue18 = attributeValue18;
	}

	@Length(max = 200)
	public String getAttributeValue19() {
		return this.attributeValue19;
	}

	public void setAttributeValue19(String attributeValue19) {
		this.attributeValue19 = attributeValue19;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public ProductCategory getProductCategory() {
		return this.productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Goods getGoods() {
		return this.goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Valid
	@ElementCollection
	@CollectionTable(name = "hq_product_product_image")
	public List<ProductImage> getProductImages() {
		return this.productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<Consultation> getConsultations() {
		return this.consultations;
	}

	public void setConsultations(Set<Consultation> consultations) {
		this.consultations = consultations;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hq_product_tag")
	@OrderBy("order asc")
	public Set<Tag> getTags() {
		return this.tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@ManyToMany(mappedBy = "favoriteProducts", fetch = FetchType.LAZY)
	public Set<Member> getFavoriteMembers() {
		return this.favoriteMembers;
	}

	public void setFavoriteMembers(Set<Member> favoriteMembers) {
		this.favoriteMembers = favoriteMembers;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hq_product_specification")
	@OrderBy("order asc")
	public Set<Specification> getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hq_product_specification_value")
	@OrderBy("specification asc")
	public Set<SpecificationValue> getSpecificationValues() {
		return this.specificationValues;
	}

	public void setSpecificationValues(
			Set<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	@ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<CartItem> getCartItems() {
		return this.cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	public Set<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "gift", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	public Set<GiftItem> getGiftItems() {
		return this.giftItems;
	}

	public void setGiftItems(Set<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<ProductNotify> getProductNotifies() {
		return this.productNotifies;
	}

	public void setProductNotifies(Set<ProductNotify> productNotifies) {
		this.productNotifies = productNotifies;
	}

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "hq_product_member_price")
	public Map<MemberRank, BigDecimal> getMemberPrice() {
		return this.memberPrice;
	}

	public void setMemberPrice(Map<MemberRank, BigDecimal> memberPrice) {
		this.memberPrice = memberPrice;
	}

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "hq_product_parameter_value")
	public Map<Parameter, String> getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(Map<Parameter, String> parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Transient
	public String getAttributeValue(Attribute attribute) {
		if ((attribute != null) && (attribute.getPropertyIndex() != null))
			try {
				String str = "attributeValue" + attribute.getPropertyIndex();
				return (String) PropertyUtils.getProperty(this, str);
			} catch (IllegalAccessException localIllegalAccessException1) {
				localIllegalAccessException1.printStackTrace();
			} catch (InvocationTargetException localInvocationTargetException1) {
				localInvocationTargetException1.printStackTrace();
			} catch (NoSuchMethodException localNoSuchMethodException1) {
				localNoSuchMethodException1.printStackTrace();
			}
		return null;
	}

	@Transient
	public void setAttributeValue(Attribute attribute, String value) {
		if ((attribute != null) && (attribute.getPropertyIndex() != null)) {
			if (StringUtils.isEmpty(value))
				value = null;
			if ((value == null)
					|| ((attribute.getOptions() != null) && (attribute
							.getOptions().contains(value))))
				try {
					String str = "attributeValue"
							+ attribute.getPropertyIndex();
					PropertyUtils.setProperty(this, str, value);
				} catch (IllegalAccessException localIllegalAccessException1) {
					localIllegalAccessException1.printStackTrace();
				} catch (InvocationTargetException localInvocationTargetException1) {
					localInvocationTargetException1.printStackTrace();
				} catch (NoSuchMethodException localNoSuchMethodException1) {
					localNoSuchMethodException1.printStackTrace();
				}
		}
	}

	@Transient
	public List<Product> getSiblings() {
		ArrayList<Product> localArrayList = new ArrayList<Product>();
		if ((getGoods() != null) && (getGoods().getProducts() != null)) {
			Iterator<Product> localIterator = getGoods().getProducts()
					.iterator();
			while (localIterator.hasNext()) {
				Product localProduct = (Product) localIterator.next();
				if (equals(localProduct))
					continue;
				localArrayList.add(localProduct);
			}
		}
		return localArrayList;
	}

	@JsonProperty
	@Transient
	public String getPath() {
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		localHashMap.put("id", getId());
		localHashMap.put("createDate", getCreateDate());
		localHashMap.put("updateDate", getUpdateDate());
		localHashMap.put("sn", getSn());
		localHashMap.put("name", getName());
		localHashMap.put("fullName", getFullName());
		localHashMap.put("seoTitle", getSeoTitle());
		localHashMap.put("seoKeywords", getSeoKeywords());
		localHashMap.put("seoDescription", getSeoDescription());
		localHashMap.put("productCategory", getProductCategory());
		return FreeMarkers.renderString(staticPath, localHashMap);
	}

	@JsonProperty
	@Transient
	public String getThumbnail() {
		if ((getProductImages() != null) && (!getProductImages().isEmpty()))
			return ((ProductImage) getProductImages().get(0)).getThumbnail();
		return null;
	}

	@Transient
	public Set<Promotion> getValidPromotions() {
		HashSet<Promotion> localHashSet = new HashSet<Promotion>();
		if (getPromotions() != null)
			localHashSet.addAll(getPromotions());
		if ((getProductCategory() != null)
				&& (getProductCategory().getPromotions() != null))
			localHashSet.addAll(getProductCategory().getPromotions());
		if ((getBrand() != null) && (getBrand().getPromotions() != null))
			localHashSet.addAll(getBrand().getPromotions());
		Set<Promotion> localTreeSet = new TreeSet<Promotion>();
		Iterator<Promotion> localIterator = localHashSet.iterator();
		while (localIterator.hasNext()) {
			Promotion localPromotion = (Promotion) localIterator.next();
			if ((localPromotion == null) || (!localPromotion.hasBegun())
					|| (localPromotion.hasEnded()))
				continue;
			localTreeSet.add(localPromotion);
		}
		return localTreeSet;
	}

	@Transient
	public Integer getAvailableStock() {
		Integer localInteger = null;
		if ((getStock() != null) && (getAllocatedStock() != null)) {
			localInteger = Integer.valueOf(getStock().intValue()
					- getAllocatedStock().intValue());
			if (localInteger.intValue() < 0)
				localInteger = Integer.valueOf(0);
		}
		return localInteger;
	}

	@Transient
	public Boolean getIsOutOfStock() {
		if ((getStock() != null) && (getAllocatedStock() != null)
				&& (getAllocatedStock().intValue() >= getStock().intValue()))
			return Boolean.valueOf(true);
		return Boolean.valueOf(false);
	}

	@PreRemove
	public void preRemove() {
		Set<Member> localSet = getFavoriteMembers();
		if (localSet != null) {
			Iterator<Member> localObject2 = localSet.iterator();
			while (localObject2.hasNext()) {
				Member localObject1 = (Member) (localObject2).next();
				localObject1.getFavoriteProducts().remove(this);
			}
		}
		Set<Promotion> localObject1 = getPromotions();
		if (localObject1 != null) {
			Iterator<Promotion> localObject3 = (localObject1).iterator();
			while (localObject3.hasNext()) {
				Promotion localObject2 = (Promotion) (localObject3).next();
				localObject2.getProducts().remove(this);
			}
		}
		Set<OrderItem> localObject2 = getOrderItems();
		if (localObject2 != null) {
			Iterator<OrderItem> localIterator = localObject2.iterator();
			while (localIterator.hasNext()) {
				OrderItem localObject3 = (OrderItem) localIterator.next();
				localObject3.setProduct(null);
			}
		}
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		if (getStock() == null)
			setAllocatedStock(Integer.valueOf(0));
		setScore(Float.valueOf(0.0F));
	}

	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
		if (getStock() == null)
			setAllocatedStock(Integer.valueOf(0));
		if ((getTotalScore() != null) && (getScoreCount() != null)
				&& (getScoreCount().longValue() != 0L))
			setScore(Float.valueOf((float) getTotalScore().longValue()
					/ (float) getScoreCount().longValue()));
		else
			setScore(Float.valueOf(0.0F));
	}

	public String toString() {
		return getFullName();
	}
}