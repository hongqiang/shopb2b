package com.hongqiang.shop.common.utils.plugin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.plugin.service.PluginService;
import com.hongqiang.shop.common.web.BaseController;

@Controller("adminStoragePluginController")
@RequestMapping({ "${adminPath}/storage_plugin" })
public class StoragePluginController extends BaseController {

	@Autowired
	private PluginService pluginService;

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("storagePlugins",this.pluginService.getStoragePlugins());
		return "admin/storage_plugin/list";
	}
}