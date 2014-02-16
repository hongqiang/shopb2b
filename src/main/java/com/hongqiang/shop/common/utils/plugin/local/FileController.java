package com.hongqiang.shop.common.utils.plugin.local;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.plugin.service.PluginConfigService;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.PluginConfig;

@Controller("adminPluginFileController")
@RequestMapping({"${adminPath}/storage_plugin/file"})
public class FileController extends BaseController
{

  @Resource(name="filePlugin")
  private FilePlugin filePlugin;

  @Resource(name="pluginConfigServiceImpl")
  private PluginConfigService pluginConfigService;

  @RequestMapping(value={"/setting"}, method=RequestMethod.GET)
  public String setting(ModelMap model)
  {
    PluginConfig localPluginConfig = this.filePlugin.getPluginConfig();
    model.addAttribute("pluginConfig", localPluginConfig);
    return "/admin/local/setting";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Integer order, RedirectAttributes redirectAttributes)
  {
    PluginConfig localPluginConfig = this.filePlugin.getPluginConfig();
    localPluginConfig.setIsEnabled(Boolean.valueOf(true));
    localPluginConfig.setOrder(order);
    this.pluginConfigService.update(localPluginConfig);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/storage_plugin/list.jhtml";
  }
}