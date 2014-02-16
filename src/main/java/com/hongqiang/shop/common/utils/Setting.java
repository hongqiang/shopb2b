package com.hongqiang.shop.common.utils;

import java.io.Serializable;
import java.math.BigDecimal;
//import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class Setting implements Serializable {
	private static final long serialVersionUID = -1478999889661796840L;
	public static final String CACHE_NAME = "setting";
	public static final String CACHE_KEY = "0";
	private static final String SEPARATOR = ",";
	private String siteName;
	private String siteUrl;
	private String logo;
	private String hotSearch;
	private String address;
	private String phone;
	private String zipCode;
	private String email;
	private String certtext;
	private Boolean isSiteEnabled;
	private String siteCloseMessage;
	private Integer largeProductImageWidth;
	private Integer largeProductImageHeight;
	private Integer mediumProductImageWidth;
	private Integer mediumProductImageHeight;
	private Integer thumbnailProductImageWidth;
	private Integer thumbnailProductImageHeight;
	private String defaultLargeProductImage;
	private String defaultMediumProductImage;
	private String defaultThumbnailProductImage;
	private Integer watermarkAlpha;
	private String watermarkImage;
	private WatermarkPosition watermarkPosition;
	private Integer priceScale;
	private RoundType priceRoundType;
	private Boolean isShowMarketPrice;
	private Double defaultMarketPriceScale;
	private Boolean isRegisterEnabled;
	private Boolean isDuplicateEmail;
	private String disabledUsername;
	private Integer usernameMinLength;
	private Integer usernameMaxLength;
	private Integer passwordMinLength;
	private Integer passwordMaxLength;
	private Long registerPoint;
	private String registerAgreement;
	private Boolean isEmailLogin;
	private CaptchaType[] captchaTypes;
	private AccountLockType[] accountLockTypes;
	private Integer accountLockCount;
	private Integer accountLockTime;
	private Integer safeKeyExpiryTime;
	private Integer uploadMaxSize;
	private String uploadImageExtension;
	private String uploadFlashExtension;
	private String uploadMediaExtension;
	private String uploadFileExtension;
	private String imageUploadPath;
	private String flashUploadPath;
	private String mediaUploadPath;
	private String fileUploadPath;
	private String smtpFromMail;
	private String smtpHost;
	private Integer smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private String currencySign;
	private String currencyUnit;
	private Integer stockAlertCount;
	private StockAllocationTime stockAllocationTime;
	private Double defaultPointScale;
	private Boolean isDevelopmentEnabled;
	private Boolean isReviewEnabled;
	private Boolean isReviewCheck;
	private ReviewAuthority reviewAuthority;
	private Boolean isConsultationEnabled;
	private Boolean isConsultationCheck;
	private ConsultationAuthority consultationAuthority;
	private Boolean isInvoiceEnabled;
	private Boolean isTaxPriceEnabled;
	private Double taxRate;
	private String cookiePath;
	private String cookieDomain;
	private String kuaidi100Key;
	private Boolean isCnzzEnabled;
	private String cnzzSiteId;
	private String cnzzPassword;

	@NotEmpty
	@Length(max = 200)
	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSiteUrl() {
		return this.siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
	}

	@NotEmpty
	@Length(max = 200)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Length(max = 200)
	public String getHotSearch() {
		return this.hotSearch;
	}

	public void setHotSearch(String hotSearch) {
		if (hotSearch != null) {
			hotSearch = hotSearch.replaceAll("[,\\s]*,[,\\s]*", SEPARATOR)
					.replaceAll("^,|,$", "");
		}
		this.hotSearch = hotSearch;
	}

	@Length(max = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(max = 200)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(max = 200)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Email
	@Length(max = 200)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(max = 200)
	public String getCerttext() {
		return this.certtext;
	}

	public void setCerttext(String certtext) {
		this.certtext = certtext;
	}

	@NotNull
	public Boolean getIsSiteEnabled() {
		return this.isSiteEnabled;
	}

	public void setIsSiteEnabled(Boolean isSiteEnabled) {
		this.isSiteEnabled = isSiteEnabled;
	}

	@NotEmpty
	public String getSiteCloseMessage() {
		return this.siteCloseMessage;
	}

	public void setSiteCloseMessage(String siteCloseMessage) {
		this.siteCloseMessage = siteCloseMessage;
	}

	@NotNull
	@Min(1L)
	public Integer getLargeProductImageWidth() {
		return this.largeProductImageWidth;
	}

	public void setLargeProductImageWidth(Integer largeProductImageWidth) {
		this.largeProductImageWidth = largeProductImageWidth;
	}

	@NotNull
	@Min(1L)
	public Integer getLargeProductImageHeight() {
		return this.largeProductImageHeight;
	}

	public void setLargeProductImageHeight(Integer largeProductImageHeight) {
		this.largeProductImageHeight = largeProductImageHeight;
	}

	@NotNull
	@Min(1L)
	public Integer getMediumProductImageWidth() {
		return this.mediumProductImageWidth;
	}

	public void setMediumProductImageWidth(Integer mediumProductImageWidth) {
		this.mediumProductImageWidth = mediumProductImageWidth;
	}

	@NotNull
	@Min(1L)
	public Integer getMediumProductImageHeight() {
		return this.mediumProductImageHeight;
	}

	public void setMediumProductImageHeight(Integer mediumProductImageHeight) {
		this.mediumProductImageHeight = mediumProductImageHeight;
	}

	@NotNull
	@Min(1L)
	public Integer getThumbnailProductImageWidth() {
		return this.thumbnailProductImageWidth;
	}

	public void setThumbnailProductImageWidth(Integer thumbnailProductImageWidth) {
		this.thumbnailProductImageWidth = thumbnailProductImageWidth;
	}

	@NotNull
	@Min(1L)
	public Integer getThumbnailProductImageHeight() {
		return this.thumbnailProductImageHeight;
	}

	public void setThumbnailProductImageHeight(
			Integer thumbnailProductImageHeight) {
		this.thumbnailProductImageHeight = thumbnailProductImageHeight;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultLargeProductImage() {
		return this.defaultLargeProductImage;
	}

	public void setDefaultLargeProductImage(String defaultLargeProductImage) {
		this.defaultLargeProductImage = defaultLargeProductImage;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultMediumProductImage() {
		return this.defaultMediumProductImage;
	}

	public void setDefaultMediumProductImage(String defaultMediumProductImage) {
		this.defaultMediumProductImage = defaultMediumProductImage;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultThumbnailProductImage() {
		return this.defaultThumbnailProductImage;
	}

	public void setDefaultThumbnailProductImage(
			String defaultThumbnailProductImage) {
		this.defaultThumbnailProductImage = defaultThumbnailProductImage;
	}

	@NotNull
	@Min(0L)
	@Max(100L)
	public Integer getWatermarkAlpha() {
		return this.watermarkAlpha;
	}

	public void setWatermarkAlpha(Integer watermarkAlpha) {
		this.watermarkAlpha = watermarkAlpha;
	}

	public String getWatermarkImage() {
		return this.watermarkImage;
	}

	public void setWatermarkImage(String watermarkImage) {
		this.watermarkImage = watermarkImage;
	}

	@NotNull
	public WatermarkPosition getWatermarkPosition() {
		return this.watermarkPosition;
	}

	public void setWatermarkPosition(WatermarkPosition watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}

	@NotNull
	@Min(0L)
	@Max(3L)
	public Integer getPriceScale() {
		return this.priceScale;
	}

	public void setPriceScale(Integer priceScale) {
		this.priceScale = priceScale;
	}

	@NotNull
	public RoundType getPriceRoundType() {
		return this.priceRoundType;
	}

	public void setPriceRoundType(RoundType priceRoundType) {
		this.priceRoundType = priceRoundType;
	}

	@NotNull
	public Boolean getIsShowMarketPrice() {
		return this.isShowMarketPrice;
	}

	public void setIsShowMarketPrice(Boolean isShowMarketPrice) {
		this.isShowMarketPrice = isShowMarketPrice;
	}

	@NotNull
	@Min(0L)
//	@Digits(integer = 3, fraction = 3)
	public Double getDefaultMarketPriceScale() {
		return this.defaultMarketPriceScale;
	}

	public void setDefaultMarketPriceScale(Double defaultMarketPriceScale) {
		this.defaultMarketPriceScale = defaultMarketPriceScale;
	}

	@NotNull
	public Boolean getIsRegisterEnabled() {
		return this.isRegisterEnabled;
	}

	public void setIsRegisterEnabled(Boolean isRegisterEnabled) {
		this.isRegisterEnabled = isRegisterEnabled;
	}

	@NotNull
	public Boolean getIsDuplicateEmail() {
		return this.isDuplicateEmail;
	}

	public void setIsDuplicateEmail(Boolean isDuplicateEmail) {
		this.isDuplicateEmail = isDuplicateEmail;
	}

	@Length(max = 200)
	public String getDisabledUsername() {
		return this.disabledUsername;
	}

	public void setDisabledUsername(String disabledUsername) {
		if (disabledUsername != null) {
			disabledUsername = disabledUsername.replaceAll("[,\\s]*,[,\\s]*",
					SEPARATOR).replaceAll("^,|,$", "");
		}
		this.disabledUsername = disabledUsername;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getUsernameMinLength() {
		return this.usernameMinLength;
	}

	public void setUsernameMinLength(Integer usernameMinLength) {
		this.usernameMinLength = usernameMinLength;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getUsernameMaxLength() {
		return this.usernameMaxLength;
	}

	public void setUsernameMaxLength(Integer usernameMaxLength) {
		this.usernameMaxLength = usernameMaxLength;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getPasswordMinLength() {
		return this.passwordMinLength;
	}

	public void setPasswordMinLength(Integer passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getPasswordMaxLength() {
		return this.passwordMaxLength;
	}

	public void setPasswordMaxLength(Integer passwordMaxLength) {
		this.passwordMaxLength = passwordMaxLength;
	}

	@NotNull
	@Min(0L)
	public Long getRegisterPoint() {
		return this.registerPoint;
	}

	public void setRegisterPoint(Long registerPoint) {
		this.registerPoint = registerPoint;
	}

	@NotEmpty
	public String getRegisterAgreement() {
		return this.registerAgreement;
	}

	public void setRegisterAgreement(String registerAgreement) {
		this.registerAgreement = registerAgreement;
	}

	@NotNull
	public Boolean getIsEmailLogin() {
		return this.isEmailLogin;
	}

	public void setIsEmailLogin(Boolean isEmailLogin) {
		this.isEmailLogin = isEmailLogin;
	}

	public CaptchaType[] getCaptchaTypes() {
		return this.captchaTypes;
	}

	public void setCaptchaTypes(CaptchaType[] captchaTypes) {
		this.captchaTypes = captchaTypes;
	}

	public AccountLockType[] getAccountLockTypes() {
		return this.accountLockTypes;
	}

	public void setAccountLockTypes(AccountLockType[] accountLockTypes) {
		this.accountLockTypes = accountLockTypes;
	}

	@NotNull
	@Min(1L)
	public Integer getAccountLockCount() {
		return this.accountLockCount;
	}

	public void setAccountLockCount(Integer accountLockCount) {
		this.accountLockCount = accountLockCount;
	}

	@NotNull
	@Min(0L)
	public Integer getAccountLockTime() {
		return this.accountLockTime;
	}

	public void setAccountLockTime(Integer accountLockTime) {
		this.accountLockTime = accountLockTime;
	}

	@NotNull
	@Min(0L)
	public Integer getSafeKeyExpiryTime() {
		return this.safeKeyExpiryTime;
	}

	public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime) {
		this.safeKeyExpiryTime = safeKeyExpiryTime;
	}

	@NotNull
	@Min(0L)
	public Integer getUploadMaxSize() {
		return this.uploadMaxSize;
	}

	public void setUploadMaxSize(Integer uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	@Length(max = 200)
	public String getUploadImageExtension() {
		return this.uploadImageExtension;
	}

	public void setUploadImageExtension(String uploadImageExtension) {
		if (uploadImageExtension != null) {
			uploadImageExtension = uploadImageExtension
					.replaceAll("[,\\s]*,[,\\s]*", SEPARATOR)
					.replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadImageExtension = uploadImageExtension;
	}

	@Length(max = 200)
	public String getUploadFlashExtension() {
		return this.uploadFlashExtension;
	}

	public void setUploadFlashExtension(String uploadFlashExtension) {
		if (uploadFlashExtension != null) {
			uploadFlashExtension = uploadFlashExtension
					.replaceAll("[,\\s]*,[,\\s]*", SEPARATOR)
					.replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadFlashExtension = uploadFlashExtension;
	}

	@Length(max = 200)
	public String getUploadMediaExtension() {
		return this.uploadMediaExtension;
	}

	public void setUploadMediaExtension(String uploadMediaExtension) {
		if (uploadMediaExtension != null) {
			uploadMediaExtension = uploadMediaExtension
					.replaceAll("[,\\s]*,[,\\s]*", SEPARATOR)
					.replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadMediaExtension = uploadMediaExtension;
	}

	@Length(max = 200)
	public String getUploadFileExtension() {
		return this.uploadFileExtension;
	}

	public void setUploadFileExtension(String uploadFileExtension) {
		if (uploadFileExtension != null) {
			uploadFileExtension = uploadFileExtension
					.replaceAll("[,\\s]*,[,\\s]*", SEPARATOR)
					.replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadFileExtension = uploadFileExtension;
	}

	@NotEmpty
	@Length(max = 200)
	public String getImageUploadPath() {
		return this.imageUploadPath;
	}

	public void setImageUploadPath(String imageUploadPath) {
		if (imageUploadPath != null) {
			if (!imageUploadPath.startsWith("/")) {
				imageUploadPath = "/" + imageUploadPath;
			}
			if (!imageUploadPath.endsWith("/")) {
				imageUploadPath = imageUploadPath + "/";
			}
		}
		this.imageUploadPath = imageUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getFlashUploadPath() {
		return this.flashUploadPath;
	}

	public void setFlashUploadPath(String flashUploadPath) {
		if (flashUploadPath != null) {
			if (!flashUploadPath.startsWith("/")) {
				flashUploadPath = "/" + flashUploadPath;
			}
			if (!flashUploadPath.endsWith("/")) {
				flashUploadPath = flashUploadPath + "/";
			}
		}
		this.flashUploadPath = flashUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getMediaUploadPath() {
		return this.mediaUploadPath;
	}

	public void setMediaUploadPath(String mediaUploadPath) {
		if (mediaUploadPath != null) {
			if (!mediaUploadPath.startsWith("/")) {
				mediaUploadPath = "/" + mediaUploadPath;
			}
			if (!mediaUploadPath.endsWith("/")) {
				mediaUploadPath = mediaUploadPath + "/";
			}
		}
		this.mediaUploadPath = mediaUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getFileUploadPath() {
		return this.fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		if (fileUploadPath != null) {
			if (!fileUploadPath.startsWith("/")) {
				fileUploadPath = "/" + fileUploadPath;
			}
			if (!fileUploadPath.endsWith("/")) {
				fileUploadPath = fileUploadPath + "/";
			}
		}
		this.fileUploadPath = fileUploadPath;
	}

	@NotEmpty
	@Email
	@Length(max = 200)
	public String getSmtpFromMail() {
		return this.smtpFromMail;
	}

	public void setSmtpFromMail(String smtpFromMail) {
		this.smtpFromMail = smtpFromMail;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSmtpHost() {
		return this.smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@NotNull
	@Min(0L)
	public Integer getSmtpPort() {
		return this.smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSmtpUsername() {
		return this.smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	@Length(max = 200)
	public String getSmtpPassword() {
		return this.smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCurrencySign() {
		return this.currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCurrencyUnit() {
		return this.currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	@NotNull
	@Min(0L)
	public Integer getStockAlertCount() {
		return this.stockAlertCount;
	}

	public void setStockAlertCount(Integer stockAlertCount) {
		this.stockAlertCount = stockAlertCount;
	}

	@NotNull
	public StockAllocationTime getStockAllocationTime() {
		return this.stockAllocationTime;
	}

	public void setStockAllocationTime(StockAllocationTime stockAllocationTime) {
		this.stockAllocationTime = stockAllocationTime;
	}

	@NotNull
	@Min(0L)
//	@Digits(integer = 3, fraction = 3)
	public Double getDefaultPointScale() {
		return this.defaultPointScale;
	}

	public void setDefaultPointScale(Double defaultPointScale) {
		this.defaultPointScale = defaultPointScale;
	}

	@NotNull
	public Boolean getIsDevelopmentEnabled() {
		return this.isDevelopmentEnabled;
	}

	public void setIsDevelopmentEnabled(Boolean isDevelopmentEnabled) {
		this.isDevelopmentEnabled = isDevelopmentEnabled;
	}

	@NotNull
	public Boolean getIsReviewEnabled() {
		return this.isReviewEnabled;
	}

	public void setIsReviewEnabled(Boolean isReviewEnabled) {
		this.isReviewEnabled = isReviewEnabled;
	}

	@NotNull
	public Boolean getIsReviewCheck() {
		return this.isReviewCheck;
	}

	public void setIsReviewCheck(Boolean isReviewCheck) {
		this.isReviewCheck = isReviewCheck;
	}

	@NotNull
	public ReviewAuthority getReviewAuthority() {
		return this.reviewAuthority;
	}

	public void setReviewAuthority(ReviewAuthority reviewAuthority) {
		this.reviewAuthority = reviewAuthority;
	}

	@NotNull
	public Boolean getIsConsultationEnabled() {
		return this.isConsultationEnabled;
	}

	public void setIsConsultationEnabled(Boolean isConsultationEnabled) {
		this.isConsultationEnabled = isConsultationEnabled;
	}

	@NotNull
	public Boolean getIsConsultationCheck() {
		return this.isConsultationCheck;
	}

	public void setIsConsultationCheck(Boolean isConsultationCheck) {
		this.isConsultationCheck = isConsultationCheck;
	}

	@NotNull
	public ConsultationAuthority getConsultationAuthority() {
		return this.consultationAuthority;
	}

	public void setConsultationAuthority(
			ConsultationAuthority consultationAuthority) {
		this.consultationAuthority = consultationAuthority;
	}

	@NotNull
	public Boolean getIsInvoiceEnabled() {
		return this.isInvoiceEnabled;
	}

	public void setIsInvoiceEnabled(Boolean isInvoiceEnabled) {
		this.isInvoiceEnabled = isInvoiceEnabled;
	}

	@NotNull
	public Boolean getIsTaxPriceEnabled() {
		return this.isTaxPriceEnabled;
	}

	public void setIsTaxPriceEnabled(Boolean isTaxPriceEnabled) {
		this.isTaxPriceEnabled = isTaxPriceEnabled;
	}

	@NotNull
	@Min(0L)
//	@Digits(integer = 3, fraction = 3)
	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCookiePath() {
		return this.cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		if ((cookiePath != null) && (!cookiePath.endsWith("/"))) {
			cookiePath = cookiePath + "/";
		}
		this.cookiePath = cookiePath;
	}

	@Length(max = 200)
	public String getCookieDomain() {
		return this.cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	@Length(max = 200)
	public String getKuaidi100Key() {
		return this.kuaidi100Key;
	}

	public void setKuaidi100Key(String kuaidi100Key) {
		this.kuaidi100Key = kuaidi100Key;
	}

	public Boolean getIsCnzzEnabled() {
		return this.isCnzzEnabled;
	}

	public void setIsCnzzEnabled(Boolean isCnzzEnabled) {
		this.isCnzzEnabled = isCnzzEnabled;
	}

	public String getCnzzSiteId() {
		return this.cnzzSiteId;
	}

	public void setCnzzSiteId(String cnzzSiteId) {
		this.cnzzSiteId = cnzzSiteId;
	}

	public String getCnzzPassword() {
		return this.cnzzPassword;
	}

	public void setCnzzPassword(String cnzzPassword) {
		this.cnzzPassword = cnzzPassword;
	}

	public String[] getHotSearches() {
		return StringUtils.split(this.hotSearch, SEPARATOR);
	}

	public String[] getDisabledUsernames() {
		return StringUtils.split(this.disabledUsername, SEPARATOR);
	}

	public String[] getUploadImageExtensions() {
		return StringUtils.split(this.uploadImageExtension, SEPARATOR);
	}

	public String[] getUploadFlashExtensions() {
		return StringUtils.split(this.uploadFlashExtension, SEPARATOR);
	}

	public String[] getUploadMediaExtensions() {
		return StringUtils.split(this.uploadMediaExtension, SEPARATOR);
	}

	public String[] getUploadFileExtensions() {
		return StringUtils.split(this.uploadFileExtension, SEPARATOR);
	}

	//设置小数的精度
	public BigDecimal setScale(BigDecimal amount) {
		if (amount == null)
			return null;
		int roundingMode = 0;
		if (getPriceRoundType() == RoundType.roundUp) {
			roundingMode = 0;
		} else {
			if (getPriceRoundType() == RoundType.roundDown)
				roundingMode = 1;
			else
				roundingMode = 4;
		}
		return amount.setScale(getPriceScale().intValue(), roundingMode);
	}

	public static enum AccountLockType {
		member,

		admin;
	}

	public static enum CaptchaType {
		memberLogin,

		memberRegister,

		adminLogin,

		review,

		consultation,

		findPassword,

		resetPassword,

		other;
	}

	public static enum ConsultationAuthority {
		anyone,

		member;
	}

	public static enum ReviewAuthority {
		anyone,

		member,

		purchased;
	}

	public static enum RoundType {
		roundHalfUp,

		roundUp,

		roundDown;
	}

	public static enum StockAllocationTime {
		order,

		payment,

		ship;
	}

	public static enum WatermarkPosition {
		no,

		topLeft,

		topRight,

		center,

		bottomLeft,

		bottomRight;
	}
}