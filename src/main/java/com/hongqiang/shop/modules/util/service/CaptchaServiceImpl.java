package com.hongqiang.shop.modules.util.service;

import java.awt.image.BufferedImage;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;

@Service("captchaServiceImpl")
public class CaptchaServiceImpl
  implements CaptchaService
{

  @Autowired
  private com.octo.captcha.service.CaptchaService captchaService;

  public BufferedImage buildImage(String captchaId)
  {
    return (BufferedImage)this.captchaService.getChallengeForID(captchaId);
  }

  public boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha)
  {
    Setting localSetting = SettingUtils.get();
    if ((captchaType == null) || (ArrayUtils.contains(localSetting.getCaptchaTypes(), captchaType)))
    {
      if ((StringUtils.isNotEmpty(captchaId)) && (StringUtils.isNotEmpty(captcha)))
        try
        {
          return this.captchaService.validateResponseForID(captchaId, captcha.toUpperCase()).booleanValue();
        }
        catch (Exception localException)
        {
          return false;
        }
      return false;
    }
    return true;
  }
}