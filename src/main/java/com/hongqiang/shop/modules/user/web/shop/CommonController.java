package com.hongqiang.shop.modules.user.web.shop;

import java.awt.image.BufferedImage;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.util.service.CaptchaService;
import com.hongqiang.shop.modules.util.service.RSAService;

@Controller("shopCommonController")
@RequestMapping({"${frontPath}/common"})
public class CommonController
{

  @Autowired
  private RSAService rSAService;

  @Autowired
  private AreaService areaService;

  @Autowired
  private CaptchaService captchaService;

  @RequestMapping({"/resource_not_found"})
  public String resourceNotFound()
  {
    return "/shop/common/resource_not_found";
  }

  @RequestMapping({"/error"})
  public String error()
  {
    return "/shop/common/error";
  }

  @RequestMapping({"/site_close"})
  public String siteClose()
  {
    Setting localSetting = SettingUtils.get();
    if (localSetting.getIsSiteEnabled().booleanValue())
      return "redirect:/";
    return "/shop/common/site_close";
  }

  @RequestMapping(value={"/public_key"}, method=RequestMethod.GET)
  @ResponseBody
  public Map<String, String> publicKey(HttpServletRequest request)
  {
    RSAPublicKey localRSAPublicKey = this.rSAService.generateKey(request);
    HashMap<String, String> localHashMap = new HashMap<String, String>();
    localHashMap.put("modulus", Base64.encodeBase64String(localRSAPublicKey.getModulus().toByteArray()));
    localHashMap.put("exponent", Base64.encodeBase64String(localRSAPublicKey.getPublicExponent().toByteArray()));
    return localHashMap;
  }

  @RequestMapping(value={"/area"}, method=RequestMethod.GET)
  @ResponseBody
  public Map<Long, String> area(Long parentId)
  {
    List<Area> localObject = new ArrayList<Area>();
    Area localArea1 = (Area)this.areaService.find(parentId);
    if (localArea1 != null)
      localObject = new ArrayList<Area>(localArea1.getChildren());
    else
      localObject = this.areaService.findRoots();
    HashMap<Long, String> localHashMap = new HashMap<Long, String>();
    Iterator<Area> localIterator = localObject.iterator();
    while (localIterator.hasNext())
    {
      Area localArea2 = (Area)localIterator.next();
      localHashMap.put(localArea2.getId(), localArea2.getName());
    }
    return localHashMap;
  }

  @RequestMapping(value={"/captcha"}, method=RequestMethod.GET)
  public void image(String captchaId, HttpServletRequest request, HttpServletResponse response)
  {
    if (StringUtils.isEmpty(captchaId))
      captchaId = request.getSession().getId();
    String str1 = new StringBuffer().append("yB").append("-").append("der").append("ewoP").reverse().toString();
    String str2 = new StringBuffer().append("moc").append(".").append("qhp").append("ohs").reverse().toString();
    response.addHeader(str1, str2);
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setDateHeader("Expires", 0L);
    response.setContentType("image/jpeg");
    ServletOutputStream localServletOutputStream = null;
    try
    {
      localServletOutputStream = response.getOutputStream();
      BufferedImage localBufferedImage = this.captchaService.buildImage(captchaId);
      ImageIO.write(localBufferedImage, "jpg", localServletOutputStream);
      localServletOutputStream.flush();
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    finally
    {
      IOUtils.closeQuietly(localServletOutputStream);
    }
  }
}