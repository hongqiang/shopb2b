package com.hongqiang.shop.modules.shipping.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Receiver;

@Repository
public class ReceiverDaoImpl extends BaseDaoImpl<Receiver,Long> implements
		ReceiverDaoCustom {
	public Receiver findDefault(Member member) {
		if (member == null)
			return null;
		try {
			String str1 = "select receiver from Receiver receiver where receiver.member = :member and receiver.isDefault = true";
			return (Receiver) this.getEntityManager()
					.createQuery(str1, Receiver.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member).getSingleResult();
		} catch (NoResultException localNoResultException2) {
			try {
				String str2 = "select receiver from Receiver receiver where receiver.member = :member order by receiver.updateDate desc";
				return (Receiver) this.getEntityManager()
						.createQuery(str2, Receiver.class)
						.setFlushMode(FlushModeType.COMMIT)
						.setParameter("member", member).setMaxResults(1)
						.getSingleResult();
			} catch (NoResultException localNoResultException3) {
			}
		}
		return null;
	}

	public Page<Receiver> findPage(Member member, Pageable pageable) {
		String sqlString = "select receiver from Receiver receiver where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (member != null) {
			sqlString += " and receiver.member = ? ";
			params.add(member);
		}
		return super.findPage(sqlString, params, pageable);
	}

	public void persist(Receiver receiver) {
		if (receiver == null || receiver.getMember() == null) {
			return;
		}
		if (receiver.getIsDefault().booleanValue()) {
			String str = "update Receiver receiver set receiver.isDefault = false "+
								"where receiver.member = :member and receiver.isDefault = true";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", receiver.getMember())
					.executeUpdate();
		}
		super.persist(receiver);
	}

	public Receiver merge(Receiver receiver) {
		if (receiver == null || receiver.getMember() == null) {
			return null;
		}
		if (receiver.getIsDefault().booleanValue()) {
			String str = "update Receiver receiver set receiver.isDefault = false "+
								"where receiver.member = :member and receiver.isDefault = true and receiver != :receiver";
			this.getEntityManager().createQuery(str)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", receiver.getMember())
					.setParameter("receiver", receiver).executeUpdate();
		}
		return (Receiver) super.merge(receiver);
	}
}