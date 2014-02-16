package com.hongqiang.shop.modules.util.service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hongqiang.shop.common.utils.RSAUtils;

@Service("rsaServiceImpl")
public class RSAServiceImpl
  implements RSAService
{
  private static final String PRIVATE_KEY = "privateKey";

  @Transactional(readOnly=true)
  public RSAPublicKey generateKey(HttpServletRequest request)
  {
    Assert.notNull(request);
    KeyPair localKeyPair = RSAUtils.generateKeyPair();
    RSAPublicKey localRSAPublicKey = (RSAPublicKey)localKeyPair.getPublic();
    RSAPrivateKey localRSAPrivateKey = (RSAPrivateKey)localKeyPair.getPrivate();
    HttpSession localHttpSession = request.getSession();
    localHttpSession.setAttribute(PRIVATE_KEY, localRSAPrivateKey);
    return localRSAPublicKey;
  }

  @Transactional(readOnly=true)
  public void removePrivateKey(HttpServletRequest request)
  {
    Assert.notNull(request);
    HttpSession localHttpSession = request.getSession();
    localHttpSession.removeAttribute(PRIVATE_KEY);
  }

  @Transactional(readOnly=true)
  public String decryptParameter(String name, HttpServletRequest request)
  {
    Assert.notNull(request);
    if (name != null)
    {
      HttpSession localHttpSession = request.getSession();
      RSAPrivateKey localRSAPrivateKey = (RSAPrivateKey)localHttpSession.getAttribute(PRIVATE_KEY);
      String str = request.getParameter(name);
      if ((localRSAPrivateKey != null) && (StringUtils.isNotEmpty(str)))
        return RSAUtils.decrypt(localRSAPrivateKey, str);
    }
    return null;
  }
}