package com.hongqiang.shop.modules.user.web.admin;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.hongqiang.shop.modules.content.service.MessageService;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.shipping.service.OrderService;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.modules.util.service.CaptchaService;

@Controller("adminCommonController")
@RequestMapping({"${adminPath}/common"})
public class CommonController
  implements ServletContextAware
{

  @Value("${system.name}")
  private String systemName;

  @Value("${system.version}")
  private String systemVersion;

  @Value("${system.description}")
  private String systemDescription;

  @Value("${system.show_powered}")
  private Boolean systemShowPowered;

  @Autowired
  private AreaService areaService;

  @Autowired
  private CaptchaService captchaService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private ProductService productService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private MessageService messageService;
  
  @Autowired
  private ServletContext servletContext;

  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

  @RequestMapping(value={"/main"}, method=RequestMethod.GET)
  public String main()
  {
    return "/admin/common/main";
  }

  @RequestMapping(value={"/index"}, method=RequestMethod.GET)
  public String index(ModelMap model)
  {
    model.addAttribute("systemName", this.systemName);
    model.addAttribute("systemVersion", this.systemVersion);
    model.addAttribute("systemDescription", this.systemDescription);
    model.addAttribute("systemShowPowered", this.systemShowPowered);
    model.addAttribute("javaVersion", System.getProperty("java.version"));
    model.addAttribute("javaHome", System.getProperty("java.home"));
    model.addAttribute("osName", System.getProperty("os.name"));
    model.addAttribute("osArch", System.getProperty("os.arch"));
    model.addAttribute("serverInfo", this.servletContext.getServerInfo());
    model.addAttribute("servletVersion", this.servletContext.getMajorVersion() + "." + this.servletContext.getMinorVersion());
    model.addAttribute("waitingPaymentOrderCount", this.orderService.waitingPaymentCount(null));
    model.addAttribute("waitingShippingOrderCount", this.orderService.waitingShippingCount(null));
    model.addAttribute("marketableProductCount", this.productService.count(null, Boolean.valueOf(true), null, null, Boolean.valueOf(false), null, null));
    model.addAttribute("notMarketableProductCount", this.productService.count(null, Boolean.valueOf(false), null, null, Boolean.valueOf(false), null, null));
    model.addAttribute("stockAlertProductCount", this.productService.count(null, null, null, null, Boolean.valueOf(false), null, Boolean.valueOf(true)));
    model.addAttribute("outOfStockProductCount", this.productService.count(null, null, null, null, Boolean.valueOf(false), Boolean.valueOf(true), null));
    model.addAttribute("memberCount", Long.valueOf(this.memberService.count()));
    model.addAttribute("unreadMessageCount", this.messageService.count(null, Boolean.valueOf(false)));
    return "/admin/common/index";
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