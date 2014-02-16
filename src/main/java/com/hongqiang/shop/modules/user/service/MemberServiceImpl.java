package com.hongqiang.shop.modules.user.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Principal;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.Deposit;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.dao.DepositDao;
import com.hongqiang.shop.modules.user.dao.MemberDao;

@Service
public class MemberServiceImpl extends BaseService implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private DepositDao depositDao;

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return this.memberDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);
		Setting setting = SettingUtils.get();
		if (setting.getDisabledUsernames() != null)
			for (String localUsername : setting.getDisabledUsernames())
				if (StringUtils.containsIgnoreCase(username, localUsername))
					return true;
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return this.memberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail))
			return true;
		return !this.memberDao.emailExists(currentEmail);
	}

	@Transactional
	public void save(Member member, Admin operator) {
		if (member == null) {
			return;
		}
		this.memberDao.persist(member);
		if (member.getBalance().compareTo(new BigDecimal(0)) > 0) {
			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminRecharge : Deposit.Type.memberRecharge);
			deposit.setCredit(member.getBalance());
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			this.depositDao.persist(deposit);
		}
	}

	@Transactional
	public void update(Member member, Integer modifyPoint,
			BigDecimal modifyBalance, String depositMemo, Admin operator) {
		if (member == null) {
			return;
		}
		this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		if ((modifyPoint != null) && (modifyPoint.intValue() != 0)
				&& (member.getPoint().longValue() + modifyPoint.intValue() >= 0L))
			member.setPoint(Long.valueOf(member.getPoint().longValue() + modifyPoint.intValue()));
		if ((modifyBalance != null)
				&& (modifyBalance.compareTo(new BigDecimal(0)) != 0)
				&& (member.getBalance().add(modifyBalance).compareTo(new BigDecimal(0)) >= 0)) {
			member.setBalance(member.getBalance().add(modifyBalance));
			Deposit deposit = new Deposit();
			if (modifyBalance.compareTo(new BigDecimal(0)) > 0) {
				deposit.setType(operator != null ? Deposit.Type.adminRecharge : Deposit.Type.memberRecharge);
				deposit.setCredit(modifyBalance);
				deposit.setDebit(new BigDecimal(0));
			} else {
				deposit.setType(operator != null ? Deposit.Type.adminChargeback : Deposit.Type.memberPayment);
				deposit.setCredit(new BigDecimal(0));
				deposit.setDebit(modifyBalance);
			}
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			this.depositDao.persist(deposit);
		}
		this.memberDao.merge(member);
	}

	@Transactional(readOnly = true)
	public Long count(){
		return this.memberDao.count();
	}
	
	@Transactional(readOnly = true)
	public Member find(Long id) {
		return this.memberDao.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<Member> findPage(Pageable pageable) {
		return this.memberDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return this.memberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<Member> findListByEmail(String email) {
		return this.memberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public Page<Object> findPurchasePage(Date beginDate, Date endDate, Pageable pageable) {
		return this.memberDao.findPurchasePage(beginDate, endDate, pageable);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest localHttpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) localHttpServletRequest.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null)
				return true;
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest localHttpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) localHttpServletRequest.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null)
				return (Member) this.memberDao.findById(principal.getId());
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest localHttpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) localHttpServletRequest.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null)
				return principal.getUsername();
		}
		return null;
	}

	@Transactional
	public void save(Member member) {
		this.memberDao.persist(member);
	}

	@Transactional
	public Member update(Member member) {
		return (Member) this.memberDao.merge(member);
	}

	@Transactional
	public Member update(Member member, String[] ignoreProperties) {
		return (Member) this.memberDao.update(member, ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {
		this.memberDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.memberDao.delete(localSerializable);
	}

	@Transactional
	public void delete(Member member) {
		this.memberDao.delete(member);
	}
}
