package com.hongqiang.shop.modules.util.service;

import java.awt.image.BufferedImage;

import com.hongqiang.shop.common.utils.Setting;

public interface CaptchaService
{
  public BufferedImage buildImage(String captchaId);

  public boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha);
}