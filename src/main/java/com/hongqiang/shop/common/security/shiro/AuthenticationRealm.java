package com.hongqiang.shop.common.security.shiro;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.hongqiang.shop.common.utils.AuthenticationToken;
import com.hongqiang.shop.common.utils.Principal;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.SpringContextHolder;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.user.service.AdminService;
import com.hongqiang.shop.modules.util.service.CaptchaService;

@Service
@DependsOn({"AdminDao"})
public class AuthenticationRealm extends AuthorizingRealm
{

  private CaptchaService captchaService;
  private AdminService adminService;

  /**
	 * 认证回调函数, 登录时调用
	 */
	@Override
  protected AuthenticationInfo doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken token) throws AuthenticationException
  {
    AuthenticationToken localAuthenticationToken = (AuthenticationToken)token;
    String str1 = localAuthenticationToken.getUsername();
    String str2 = new String(localAuthenticationToken.getPassword());
    String str3 = localAuthenticationToken.getCaptchaId();
    String str4 = localAuthenticationToken.getCaptcha();
    String str5 = localAuthenticationToken.getHost();
    if (!this.getCaptchaService().isValid(Setting.CaptchaType.adminLogin, str3, str4))
      throw new UnsupportedTokenException();
    if ((str1 != null) && (str2 != null))
    {
      Admin localAdmin = this.getAdminService().findByUsername(str1);
      if (localAdmin == null)
        throw new UnknownAccountException();
      if (!localAdmin.getIsEnabled().booleanValue())
        throw new DisabledAccountException();
      Setting localSetting = SettingUtils.get();
      int i;
      if (localAdmin.getIsLocked().booleanValue())
        if (ArrayUtils.contains(localSetting.getAccountLockTypes(), Setting.AccountLockType.admin))
        {
          i = localSetting.getAccountLockTime().intValue();
          if (i == 0)
            throw new LockedAccountException();
          Date localDate1 = localAdmin.getLockedDate();
          Date localDate2 = DateUtils.addMinutes(localDate1, i);
          if (new Date().after(localDate2))
          {
            localAdmin.setLoginFailureCount(Integer.valueOf(0));
            localAdmin.setIsLocked(Boolean.valueOf(false));
            localAdmin.setLockedDate(null);
            this.getAdminService().update(localAdmin);
          }
          else
          {
            throw new LockedAccountException();
          }
        }
        else
        {
          localAdmin.setLoginFailureCount(Integer.valueOf(0));
          localAdmin.setIsLocked(Boolean.valueOf(false));
          localAdmin.setLockedDate(null);
          this.getAdminService().update(localAdmin);
        }
      if (!DigestUtils.md5Hex(str2).equals(localAdmin.getPassword()))
      {
        i = localAdmin.getLoginFailureCount().intValue() + 1;
        if (i >= localSetting.getAccountLockCount().intValue())
        {
          localAdmin.setIsLocked(Boolean.valueOf(true));
          localAdmin.setLockedDate(new Date());
        }
        localAdmin.setLoginFailureCount(Integer.valueOf(i));
        this.getAdminService().update(localAdmin);
        throw new IncorrectCredentialsException();
      }
      localAdmin.setLoginIp(str5);
      localAdmin.setLoginDate(new Date());
      localAdmin.setLoginFailureCount(Integer.valueOf(0));
      this.getAdminService().update(localAdmin);
      return new SimpleAuthenticationInfo(new Principal(localAdmin.getId(), str1), str2, getName());
    }
    throw new UnknownAccountException();
  }

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
  {
    Principal localPrincipal = (Principal)principals.fromRealm(getName()).iterator().next();
    if (localPrincipal != null)
    {
      List<String> localList = this.getAdminService().findAuthorities(localPrincipal.getId());
      if (localList != null)
      {
        SimpleAuthorizationInfo localSimpleAuthorizationInfo = new SimpleAuthorizationInfo();
        localSimpleAuthorizationInfo.addStringPermissions(localList);
        return localSimpleAuthorizationInfo;
      }
    }
    return null;
  }
	
	/**
	 * 获取管理服务类
	 */
	public AdminService getAdminService() {
		if (adminService == null){
			adminService = SpringContextHolder.getBean(AdminService.class);
		}
		return adminService;
	}
	
	/**
	 * 获取验证码服务类
	 */
	public CaptchaService getCaptchaService() {
		if (captchaService == null){
			captchaService = SpringContextHolder.getBean(CaptchaService.class);
		}
		return captchaService;
	}
}