package com.hongqiang.shop.modules.account.dao;

import java.util.Date;

import javax.persistence.FlushModeType;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.modules.entity.Cart;

@Repository
public class CartDaoImpl extends BaseDaoImpl<Cart,Long> implements CartDaoCustom {
	// 7*24*60*60
	private final int COOKIE_TIME = -604800;

	@Override
	public void evictExpired() {
		String str = "delete from Cart cart where cart.updateDate <= :expire";
		this.getEntityManager()
				.createQuery(str)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("expire",
						DateUtils.addSeconds(new Date(), COOKIE_TIME))
				.executeUpdate();
	}
}