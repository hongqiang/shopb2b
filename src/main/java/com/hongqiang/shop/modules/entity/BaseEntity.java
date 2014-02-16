package com.hongqiang.shop.modules.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.groups.Default;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 数据Entity类
 * 
 * @author Jack
 * 
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@MappedSuperclass
@EntityListeners(EntityListeners.class)
public abstract class BaseEntity implements Serializable {

	public abstract interface Save extends Default {
	}

	public abstract interface Update extends Default {
	}

	private static final long serialVersionUID = 6L;
	public static final String ID_PROPERTY_NAME = "id";
	public static final String CREATE_DATE_PROPERTY_NAME = "createDate";
	public static final String MODIFY_DATE_PROPERTY_NAME = "updateDate";
	public static final String fileSuffix = ".jhtml";
	// public static final String fileSuffix = ".html";
	// 显示/隐藏
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	// 是/否
	public static final String YES = "1";
	public static final String NO = "0";

	// 删除标记（0：正常；1：删除；2：审核；）
	public static final String DEL_FLAG = "delFlag";
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";

	private Long id;// id主键
	private String remarks; // 备注
	// private User createBy; // 创建者
	private Date createDate;// 创建日期
	// private User updateBy; // 更新者
	private Date updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）

	public BaseEntity() {
		this.delFlag = DEL_FLAG_NORMAL;
	}

	@PrePersist
	public void prePersist() {
		// this.updateBy = UserUtils.getUser();
		this.updateDate = new Date();
		// this.createBy = this.updateBy;
		this.createDate = this.updateDate;
	}

	@PreUpdate
	public void preUpdate() {
		// this.updateBy = UserUtils.getUser();
		this.updateDate = new Date();
	}

	@JsonProperty
	@DocumentId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min = 0, max = 255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	// @JsonIgnore
	// @ManyToOne(fetch=FetchType.LAZY)
	// @NotFound(action = NotFoundAction.IGNORE)
	// public User getCreateBy() {
	// return createBy;
	// }
	//
	// public void setCreateBy(User createBy) {
	// this.createBy = createBy;
	// }

	@JsonProperty
	@Field(store = Store.YES, index = Index.YES)
	@DateBridge(resolution = Resolution.SECOND)
	@Column(nullable = false, updatable = false)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	// @JsonIgnore
	// @ManyToOne(fetch=FetchType.LAZY)
	// @NotFound(action = NotFoundAction.IGNORE)
	// public User getUpdateBy() {
	// return updateBy;
	// }
	//
	// public void setUpdateBy(User updateBy) {
	// this.updateBy = updateBy;
	// }

	@JsonProperty
	@Field(store = Store.YES, index = Index.YES)
	@DateBridge(resolution = Resolution.SECOND)
	@Column(nullable = false)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Length(min = 1, max = 1)
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!BaseEntity.class.isAssignableFrom(obj.getClass()))
			return false;
		BaseEntity localBaseEntity = (BaseEntity) obj;
		return getId() != null ? getId().equals(localBaseEntity.getId())
				: false;
	}

	public int hashCode() {
		int i = 17;
		i += (getId() == null ? 0 : getId().hashCode() * 31);
		return i;
	}
}