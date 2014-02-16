package com.hongqiang.shop.modules.util.service;

import java.security.interfaces.RSAPublicKey;
import javax.servlet.http.HttpServletRequest;

public interface RSAService
{
  public RSAPublicKey generateKey(HttpServletRequest paramHttpServletRequest);

  public void removePrivateKey(HttpServletRequest paramHttpServletRequest);

  public String decryptParameter(String paramString, HttpServletRequest paramHttpServletRequest);
}