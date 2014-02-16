package com.hongqiang.shop.website.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.service.LogService;

@Controller("adminLogController")
@RequestMapping({ "${adminPath}/log" })
public class LogController extends BaseController {

	@Autowired
	private LogService logService;

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.logService.findPage(pageable));
		return "/admin/log/list";
	}

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("log", this.logService.find(id));
		return "/admin/log/view";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		this.logService.delete(ids);
		return ADMIN_SUCCESS;
	}

	@RequestMapping(value = { "/clear" }, method = RequestMethod.POST)
	@ResponseBody
	public Message clear() {
		this.logService.clear();
		return ADMIN_SUCCESS;
	}
}