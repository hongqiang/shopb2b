package com.hongqiang.shop.modules.user.web.admin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.user.service.AdminService;

@Controller("adminProfileController")
@RequestMapping({"${adminPath}/profile"})
public class ProfileController extends BaseController
{

  @Autowired
  private AdminService adminService;

  @RequestMapping(value={"/check_current_password"}, method=RequestMethod.GET)
  @ResponseBody
  public boolean checkCurrentPassword(String currentPassword)
  {
    if (StringUtils.isEmpty(currentPassword))
      return false;
    Admin localAdmin = this.adminService.getCurrent();
    return StringUtils.equals(DigestUtils.md5Hex(currentPassword), localAdmin.getPassword());
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(ModelMap model)
  {
    model.addAttribute("admin", this.adminService.getCurrent());
    return "/admin/profile/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(String currentPassword, String password, String email, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(Admin.class, "email", email, new Class[0]))
      return ERROR_PAGE;
    Admin localAdmin = this.adminService.getCurrent();
    if ((StringUtils.isNotEmpty(currentPassword)) && (StringUtils.isNotEmpty(password)))
    {
      if (!beanValidator(Admin.class, "password", password, new Class[0]))
        return ERROR_PAGE;
      if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), localAdmin.getPassword()))
        return ERROR_PAGE;
      localAdmin.setPassword(DigestUtils.md5Hex(password));
    }
    localAdmin.setEmail(email);
    this.adminService.update(localAdmin);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:"+Global.getAdminPath()+"/common/main.jhtml";
  }
}