package com.hongqiang.shop.modules.user.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.util.service.StaticService;
import com.hongqiang.shop.modules.util.service.TemplateService;
import com.hongqiang.shop.modules.utils.Template;

@Controller("adminSitemapController")
@RequestMapping({ "${adminPath}/sitemap" })
public class SitemapController extends BaseController {

	@Autowired
	private TemplateService templateService;

	@Autowired
	private StaticService staticService;

	@RequestMapping(value = { "/build" }, method = RequestMethod.GET)
	public String build(ModelMap model) {
		Template localTemplate = this.templateService.get("sitemapIndex");
		model.addAttribute("sitemapIndexPath", localTemplate.getStaticPath());
		return "/admin/sitemap/build";
	}

	@RequestMapping(value = { "/build" }, method = RequestMethod.POST)
	public String build(RedirectAttributes redirectAttributes) {
		this.staticService.buildSitemap();
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:build.jhtml";
	}
}