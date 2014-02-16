package com.hongqiang.shop.common.utils.plugin.oss;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.plugin.service.PluginConfigService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.PluginConfig;

@Controller("adminPluginOssController")
@RequestMapping({"${adminPath}/storage_plugin/oss"})
public class OssController extends BaseController
{

  @Resource(name="ossPlugin")
  private OssPlugin ossPlugin;

  @Resource(name="pluginConfigServiceImpl")
  private PluginConfigService pluginConfigService;

  @RequestMapping(value={"/install"}, method=RequestMethod.GET)
  public String install(RedirectAttributes redirectAttributes)
  {
    String str = System.getProperty("java.specification.version");
    if (StringUtils.isNotEmpty(str))
    {
      BigDecimal localObject = new BigDecimal(str);
      if (((BigDecimal)localObject).compareTo(new BigDecimal("1.6")) < 0)
      {
        addMessage(redirectAttributes, Message.error("admin.plugin.oss.unsupportedJavaVersion", new Object[0]));
        return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
      }
    }
    if (!this.ossPlugin.getIsInstalled())
    {
    	PluginConfig localObject = new PluginConfig();
      ((PluginConfig)localObject).setPluginId(this.ossPlugin.getId());
      ((PluginConfig)localObject).setIsEnabled(Boolean.valueOf(false));
      this.pluginConfigService.save(localObject);
    }
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }

  @RequestMapping(value={"/uninstall"}, method=RequestMethod.GET)
  public String uninstall(RedirectAttributes redirectAttributes)
  {
    if (this.ossPlugin.getIsInstalled())
    {
      PluginConfig localPluginConfig = this.ossPlugin.getPluginConfig();
      this.pluginConfigService.delete(localPluginConfig);
    }
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }

  @RequestMapping(value={"/setting"}, method=RequestMethod.GET)
  public String setting(ModelMap model)
  {
    PluginConfig localPluginConfig = this.ossPlugin.getPluginConfig();
    model.addAttribute("pluginConfig", localPluginConfig);
    return "/admin/ali_os/setting";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(String accessId, String accessKey, String bucketName, String urlPrefix, @RequestParam(defaultValue="false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes)
  {
    PluginConfig localPluginConfig = this.ossPlugin.getPluginConfig();
    localPluginConfig.setAttribute("accessId", accessId);
    localPluginConfig.setAttribute("accessKey", accessKey);
    localPluginConfig.setAttribute("bucketName", bucketName);
    localPluginConfig.setAttribute("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
    localPluginConfig.setIsEnabled(isEnabled);
    localPluginConfig.setOrder(order);
    this.pluginConfigService.update(localPluginConfig);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }
}