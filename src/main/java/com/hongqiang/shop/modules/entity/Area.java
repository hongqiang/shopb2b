package com.hongqiang.shop.modules.entity;

import java.util.HashSet;
import java.util.Iterator;
//import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

//地区
@Entity
@Table(name = "hq_area")
public class Area extends OrderEntity {
	private static final long serialVersionUID = -2158109459123036967L;
	private static final String PATH_SEPARATOR = ",";// 树路径分隔符
	private String name;// 地区名称
	private String fullName;// 全名
	private String treePath;// 树路径
	private Area parent;// 上级地区
	private Set<Area> children = new HashSet<Area>();// 下级地区
	private Set<Member> members = new HashSet<Member>();// 会员
	private Set<Receiver> receivers = new HashSet<Receiver>();// 接收者
	private Set<Order> orders = new HashSet<Order>();// 订单
	private Set<DeliveryCenter> deliveryCenters = new HashSet<DeliveryCenter>();// 配送中心

	@NotEmpty
	@Length(max = 100)
	@Column(nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 500)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(nullable = false, updatable = false)
	public String getTreePath() {
		return this.treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Area getParent() {
		return this.parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE )
	@OrderBy("order asc")
	public Set<Area> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Area> children) {
		this.children = children;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<Member> getMembers() {
		return this.members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<Receiver> getReceivers() {
		return this.receivers;
	}

	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<DeliveryCenter> getDeliveryCenters() {
		return this.deliveryCenters;
	}

	public void setDeliveryCenters(Set<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		Area localArea = getParent();
		if (localArea != null) {
			setFullName(localArea.getFullName() + getName());
			setTreePath(localArea.getTreePath() + localArea.getId()
					+ PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(PATH_SEPARATOR);
		}
	}

	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
		Area localArea = getParent();
		if (localArea != null)
			setFullName(localArea.getFullName() + getName());
		else
			setFullName(getName());
	}

	@PreRemove
	public void preRemove() {
		Set<Member> localSet = getMembers();
		if (localSet != null) {
			Iterator<Member> localObject2 = localSet.iterator();
			while (localObject2.hasNext()) {
				Member localObject1 = (Member) ( localObject2).next();
				localObject1.setArea(null);
			}
		}
		Set<Receiver> localObject1 = getReceivers();
		if (localObject1 != null) {
			Iterator<Receiver> localObject3 =  localObject1.iterator();
			while (localObject3.hasNext()) {
				Receiver localObject2 = (Receiver) (localObject3).next();
				localObject2.setArea(null);
			}
		}
		Set<Order> localObject2 = getOrders();
		if (localObject2 != null) {
			Iterator<Order> localObject4 = (localObject2).iterator();
			while (localObject4.hasNext()) {
				Order localObject3 =(localObject4).next();
				localObject3.setArea(null);
			}
		}
		Set<DeliveryCenter> localObject3 = getDeliveryCenters();
		if (localObject3 != null) {
			Iterator<DeliveryCenter> localIterator = (localObject3).iterator();
			while (localIterator.hasNext()) {
				DeliveryCenter localObject4 = (DeliveryCenter) localIterator.next();
				localObject4.setArea(null);
			}
		}
	}

	public String toString() {
		return getFullName();
	}
}