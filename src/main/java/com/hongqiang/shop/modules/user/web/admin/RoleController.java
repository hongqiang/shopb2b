package com.hongqiang.shop.modules.user.web.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Role;
import com.hongqiang.shop.modules.user.service.RoleService;

@Controller("adminRoleController")
@RequestMapping({ "${adminPath}/role" })
public class RoleController extends BaseController {

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String add() {
		return "/admin/role/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(Role role, RedirectAttributes redirectAttributes) {
		if (!beanValidator(redirectAttributes, role, new Class[0]))
			return ERROR_PAGE;
		role.setIsSystem(Boolean.valueOf(false));
		role.setAdmins(null);
		this.roleService.save(role);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("role", this.roleService.find(id));
		return "/admin/role/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(Role role, RedirectAttributes redirectAttributes) {
		if (!beanValidator(redirectAttributes, role, new Class[0]))
			return ERROR_PAGE;
		Role localRole = (Role) this.roleService.find(role.getId());
		if ((localRole == null) || (localRole.getIsSystem().booleanValue()))
			return ERROR_PAGE;
		this.roleService.update(role, new String[] { "isSystem", "admins" });
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.roleService.findPage(pageable));
		return "/admin/role/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null) {
			for (Long localLong : ids) {
				Role localRole = (Role) this.roleService.find(localLong);
				if ((localRole != null)
						&& ((localRole.getIsSystem().booleanValue()) || ((localRole
								.getAdmins() != null) && (!localRole
								.getAdmins().isEmpty()))))
					return Message.error("admin.role.deleteExistNotAllowed",
							new Object[] { localRole.getName() });
			}
			this.roleService.delete(ids);
		}
		return ADMIN_SUCCESS;
	}
}