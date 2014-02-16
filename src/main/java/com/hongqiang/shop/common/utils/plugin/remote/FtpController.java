package com.hongqiang.shop.common.utils.plugin.remote;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.plugin.service.PluginConfigService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.PluginConfig;

@Controller("adminPluginFtpController")
@RequestMapping({"${adminPath}/storage_plugin/ftp"})
public class FtpController extends BaseController
{

  @Resource(name="ftpPlugin")
  private FtpPlugin ftpPlugin;

  @Resource(name="pluginConfigServiceImpl")
  private PluginConfigService pluginConfigService;

  @RequestMapping(value={"/install"}, method=RequestMethod.GET)
  public String install(RedirectAttributes redirectAttributes)
  {
    if (!this.ftpPlugin.getIsInstalled())
    {
      PluginConfig localPluginConfig = new PluginConfig();
      localPluginConfig.setPluginId(this.ftpPlugin.getId());
      localPluginConfig.setIsEnabled(Boolean.valueOf(false));
      this.pluginConfigService.save(localPluginConfig);
    }
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }

  @RequestMapping(value={"/uninstall"}, method=RequestMethod.GET)
  public String uninstall(RedirectAttributes redirectAttributes)
  {
    if (this.ftpPlugin.getIsInstalled())
    {
      PluginConfig localPluginConfig = this.ftpPlugin.getPluginConfig();
      this.pluginConfigService.delete(localPluginConfig);
    }
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }

  @RequestMapping(value={"/setting"}, method=RequestMethod.GET)
  public String setting(ModelMap model)
  {
    PluginConfig localPluginConfig = this.ftpPlugin.getPluginConfig();
    model.addAttribute("pluginConfig", localPluginConfig);
    return "/admin/remote/setting";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(String host, Integer port, String username, String password, String urlPrefix, @RequestParam(defaultValue="false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes)
  {
    PluginConfig localPluginConfig = this.ftpPlugin.getPluginConfig();
    localPluginConfig.setAttribute("host", host);
    localPluginConfig.setAttribute("port", port.toString());
    localPluginConfig.setAttribute("username", username);
    localPluginConfig.setAttribute("password", password);
    localPluginConfig.setAttribute("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
    localPluginConfig.setIsEnabled(isEnabled);
    localPluginConfig.setOrder(order);
    this.pluginConfigService.update(localPluginConfig);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }
}