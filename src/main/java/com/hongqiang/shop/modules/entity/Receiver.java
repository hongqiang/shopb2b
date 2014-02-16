package com.hongqiang.shop.modules.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

//收货地址
@Entity
@Table(name = "hq_receiver")
public class Receiver extends BaseEntity {
	private static final long serialVersionUID = 2673602067029665976L;
	public static final Integer MAX_RECEIVER_COUNT = Integer.valueOf(8);// 会员收货地址最大保存数，为null则无限制
	private String consignee;// 收货人姓名
	private String areaName;// 地区名称
	private String address;// 地址
	private String zipCode;// 邮编
	private String phone;// 电话
	private Boolean isDefault;// 是否默认
	private Area area;// 地区
	private Member member;// 会员

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@JsonProperty
	@Column(nullable = false)
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonProperty
	@NotNull
	@Column(nullable = false)
	public Boolean getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		if (getArea() != null)
			setAreaName(getArea().getFullName());
	}

	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
		if (getArea() != null)
			setAreaName(getArea().getFullName());
	}
}