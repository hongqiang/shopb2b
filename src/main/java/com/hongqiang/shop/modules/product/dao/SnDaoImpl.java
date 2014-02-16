package com.hongqiang.shop.modules.product.dao;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.entity.Sn;

@Repository
public class SnDaoImpl implements SnDao, InitializingBean {

	class HiloOptimizer {
		private Sn.Type type;
		private String prefix;// sn的前缀，如'201312'
		private int maxLo;// 商品数量基数，如外面设置为100，则sn后三位从000到999
		private int middleValue;//
		private long firstValue;//
		private long lastValue;//
		private SnDaoImpl snDaoImpl;

		public HiloOptimizer(SnDaoImpl paramSnDaoImpl, Sn.Type type,
				String prefix, int maxLo) {
			this.type = type;
			this.prefix = (prefix != null ? prefix.replace("{", "${") : "");
			this.maxLo = maxLo;
			this.middleValue = (maxLo + 1);
			this.snDaoImpl = paramSnDaoImpl;
		}

		public synchronized String generate() {
			if (this.middleValue > this.maxLo) {
				this.lastValue = this.snDaoImpl.process(this.type);
				this.middleValue = (this.lastValue == 0L ? 1 : 0);
				this.firstValue = (this.lastValue * (this.maxLo + 1));
			}
			return FreeMarkers.renderString(this.prefix, null)
					+ (this.firstValue + this.middleValue++);
		}
	}

	private HiloOptimizer product;
	private HiloOptimizer order;
	private HiloOptimizer payment;
	private HiloOptimizer refunds;
	private HiloOptimizer shipping;
	private HiloOptimizer returns;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${sn.product.prefix}")
	private String snProductPrefix;

	@Value("${sn.product.maxLo}")
	private int snProductMaxLo;

	@Value("${sn.order.prefix}")
	private String snOrderPrefix;

	@Value("${sn.order.maxLo}")
	private int snOrderMaxLo;

	@Value("${sn.payment.prefix}")
	private String snPaymentPrefix;

	@Value("${sn.payment.maxLo}")
	private int snPaymentMaxLo;

	@Value("${sn.refunds.prefix}")
	private String snRefundsPrefix;

	@Value("${sn.refunds.maxLo}")
	private int snRefundsMaxLo;

	@Value("${sn.shipping.prefix}")
	private String snShippingPrefix;

	@Value("${sn.shipping.maxLo}")
	private int snShippingMaxLo;

	@Value("${sn.returns.prefix}")
	private String snReturnsPrefix;

	@Value("${sn.returns.maxLo}")
	private int snReturnsMaxLo;

	public void afterPropertiesSet() {
		this.product = new HiloOptimizer(this, Sn.Type.product,
				this.snProductPrefix, this.snProductMaxLo);
		this.order = new HiloOptimizer(this, Sn.Type.orders, this.snOrderPrefix,
				this.snOrderMaxLo);
		this.payment = new HiloOptimizer(this, Sn.Type.payment,
				this.snPaymentPrefix, this.snPaymentMaxLo);
		this.refunds = new HiloOptimizer(this, Sn.Type.refunds,
				this.snRefundsPrefix, this.snRefundsMaxLo);
		this.shipping = new HiloOptimizer(this, Sn.Type.shipping,
				this.snShippingPrefix, this.snShippingMaxLo);
		this.returns = new HiloOptimizer(this, Sn.Type.returns,
				this.snReturnsPrefix, this.snReturnsMaxLo);
	}

	public String generate(Sn.Type type) {
		Assert.notNull(type);
		if (type == Sn.Type.product)
			return this.product.generate();
		if (type == Sn.Type.orders)
			return this.order.generate();
		if (type == Sn.Type.payment)
			return this.payment.generate();
		if (type == Sn.Type.refunds)
			return this.refunds.generate();
		if (type == Sn.Type.shipping)
			return this.shipping.generate();
		if (type == Sn.Type.returns)
			return this.returns.generate();
		return null;
	}

	private long process(Sn.Type paramType) {
		String str = "select sn from Sn sn where sn.type = :type";
		try {
			Sn localSn = (Sn) this.entityManager.createQuery(str, Sn.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("type", paramType)
					.setLockMode(LockModeType.PESSIMISTIC_WRITE)
					.getSingleResult();
			// Sn localSn = (Sn) this.entityManager.createQuery(str, Sn.class)
			// .setParameter("type", paramType).getSingleResult();
			long l = localSn.getLastValue().longValue();
			localSn.setLastValue(Long.valueOf(l + 1L));
			this.entityManager.merge(localSn);
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
}