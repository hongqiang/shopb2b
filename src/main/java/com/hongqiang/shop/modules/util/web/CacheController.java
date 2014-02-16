package com.hongqiang.shop.modules.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.util.service.CacheService;

@Controller("adminCacheController")
@RequestMapping({ "${adminPath}/cache" })
public class CacheController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(CacheController.class);
	@Autowired
	private CacheService cacheService;

	@RequestMapping(value = { "/clear" }, method = RequestMethod.GET)
	public String clear(ModelMap model) {
		Long totalMemory = null;
		Long maxMemory = null;
		Long freeMemory = null;
		try {
			totalMemory = Long
					.valueOf(Runtime.getRuntime().totalMemory() / 1024L / 1024L);
			maxMemory = Long
					.valueOf(Runtime.getRuntime().maxMemory() / 1024L / 1024L);
			freeMemory = Long
					.valueOf(Runtime.getRuntime().freeMemory() / 1024L / 1024L);
		} catch (Exception localException) {
			localException.printStackTrace();
			logger.error("获取runtime出错" + localException);
		}
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("freeMemory", freeMemory);
		model.addAttribute("cacheSize",
				Integer.valueOf(this.cacheService.getCacheSize()));
		model.addAttribute("diskStorePath",
				this.cacheService.getDiskStorePath());
		return "/admin/cache/clear";
	}

	@RequestMapping(value = { "/clear" }, method = RequestMethod.POST)
	public String clear(RedirectAttributes redirectAttributes) {
		this.cacheService.clear();
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:clear.jhtml";
	}
}