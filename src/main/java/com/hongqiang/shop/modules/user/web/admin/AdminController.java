package com.hongqiang.shop.modules.user.web.admin;

import java.util.HashSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Admin;
import com.hongqiang.shop.modules.entity.BaseEntity;
import com.hongqiang.shop.modules.entity.Role;
import com.hongqiang.shop.modules.user.service.AdminService;
import com.hongqiang.shop.modules.user.service.RoleService;

@Controller("adminAdminController")
@RequestMapping({"${adminPath}/admin"})
public class AdminController extends BaseController
{

  @Autowired
  private AdminService adminService;

  @Autowired
  private RoleService roleService;

  @RequestMapping(value={"/check_username"}, method=RequestMethod.GET)
  @ResponseBody
  public boolean checkUsername(String username)
  {
    if (StringUtils.isEmpty(username))
      return false;
    return !this.adminService.usernameExists(username);
  }

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    model.addAttribute("roles", this.roleService.findAll());
    return "/admin/admin/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes)
  {
    admin.setRoles(new HashSet<Role>(this.roleService.findList(roleIds)));
    if (!beanValidator(redirectAttributes,admin, new Class[] { BaseEntity.Save.class }))
      return ERROR_PAGE;
    if (this.adminService.usernameExists(admin.getUsername()))
      return ERROR_PAGE;
    admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    admin.setIsLocked(Boolean.valueOf(false));
    admin.setLoginFailureCount(Integer.valueOf(0));
    admin.setLockedDate(null);
    admin.setLoginDate(null);
    admin.setLoginIp(null);
    admin.setOrders(null);
    this.adminService.save(admin);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("roles", this.roleService.findAll());
    model.addAttribute("admin", this.adminService.find(id));
    return "/admin/admin/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes)
  {
    admin.setRoles(new HashSet<Role>(this.roleService.findList(roleIds)));
    if (!beanValidator(redirectAttributes,admin, new Class[0]))
      return ERROR_PAGE;
    Admin localAdmin = (Admin)this.adminService.find(admin.getId());
    if (localAdmin == null)
      return ERROR_PAGE;
    if (StringUtils.isNotEmpty(admin.getPassword()))
      admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    else
      admin.setPassword(localAdmin.getPassword());
    if ((localAdmin.getIsLocked().booleanValue()) && (!admin.getIsLocked().booleanValue()))
    {
      admin.setLoginFailureCount(Integer.valueOf(0));
      admin.setLockedDate(null);
    }
    else
    {
      admin.setIsLocked(localAdmin.getIsLocked());
      admin.setLoginFailureCount(localAdmin.getLoginFailureCount());
      admin.setLockedDate(localAdmin.getLockedDate());
    }
    this.adminService.update(admin, new String[] { "username", "loginDate", "loginIp", "orders" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.adminService.findPage(pageable));
    return "/admin/admin/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids.length >= this.adminService.count())
      return Message.error("admin.common.deleteAllNotAllowed", new Object[0]);
    this.adminService.delete(ids);
    return ADMIN_SUCCESS;
  }
  
  @RequestMapping(value={"/doadmin"},method=RequestMethod.GET)
	public void doadmin(){
	
//		//test findid
//		Admin admin1 = this.adminService.find(1L);
//		System.out.println(admin1.getName()+","+admin1.getPassword());
//		//test findpage
//		Pageable pageable = new Pageable(1,40);
//  		Page<Admin> page=this.adminService.findPage(pageable);
//  		for (Admin o : page.getList()) {
//			System.out.print(o.getName()+", "+o.getPassword()+"\n");
//		}
//  		System.out.println("==============================================");
//		//test count
////		long count = this.adminService.count();
////		System.out.println("count= "+count);
//		//test usernameExists
//		String s1= "abc";
//		String s2= "admin";
//		boolean sboo1=this.adminService.usernameExists(s1);
//		boolean sboo2=this.adminService.usernameExists(s2);
//		System.out.println("sboo1= "+sboo1);
//		System.out.println("sboo2= "+sboo2);
//		System.out.println("==============================================");
//		//test findByUsername
//		Admin a1 = this.adminService.findByUsername("admin");
//		if(a1!=null){
//			System.out.print(a1.getName()+", "+a1.getPassword()+"\n");
//		}
//		else{
//			System.out.print("do not find a1");
//		}
//		System.out.println("==============================================");
//		Admin a2 = this.adminService.findByUsername("admin000");
//		if(a2!=null){
//			System.out.print(a2.getName()+", "+a2.getPassword()+"\n");
//		}
//		else{
//			System.out.print("do not find a2");
//		}
//		System.out.println("==============================================");
//		//test findAuthorities
//		
//		//test isAuthenticated
//		
//		//test getCurrent
//		
//		//test getCurrentUsername
//		
//		//test update 
//		admin1.setName("nonono");
//		this.adminService.update(admin1);
//		System.out.println("==============================================");
//		//test save---测试失败，原因为无法获取admin的role。
//		Admin admin = new Admin();
//		admin.setName("doit");
//		admin.setPassword("gogogo");
//		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
//    admin.setIsLocked(Boolean.valueOf(false));
//    admin.setLoginFailureCount(Integer.valueOf(0));
//    admin.setLockedDate(null);
//    admin.setLoginDate(null);
//    admin.setLoginIp(null);
//    admin.setOrders(null);
//    admin.setEmail("1s@126.com");
//    admin.setUsername("hehe");
//    admin.setRoles(admin1.getRoles());
//    admin.setIsEnabled(true);
//    this.adminService.save(admin);
//    System.out.println("==============================================");
//		//test delete id
//		this.adminService.delete(1L);
//		System.out.println("==============================================");
//		//test delete admin
//		this.adminService.delete(admin);
//		System.out.println("==============================================");
//	}
//	
//	@RequestMapping(value={"/dorole"},method=RequestMethod.GET)
//	public void doit(){
//		//test findid
//		Role admin1 = this.roleService.find(1L);
//		System.out.println(admin1.getName());
//		//test findpage
//		Pageable pageable = new Pageable(1,40);
//  		Page<Role> page=this.roleService.findPage(pageable);
//  		for (Role o : page.getList()) {
//			System.out.print(o.getName()+"\n");
//		}
//  		System.out.println("==============================================");
//		//test findlist
//		
//		//test findall
//		List<Role> rolelist= this.roleService.findAll();
//		for (Role o : rolelist) {
//			System.out.print(o.getName()+"\n");
//		}
//		System.out.println("==============================================");
//		//test save--测试到这里没继续
//		Role role = new Role();
//		role.setName("呵呵呵");
//		role.setIsSystem(Boolean.valueOf(false));
//		role.setAdmins(null);
//		this.roleService.save(role);
//		System.out.println("==============================================");
//		//test update
//		admin1.setName("呵呵呵");
//		this.roleService.update(admin1);
//		System.out.println("==============================================");
//		//test delete id
//		this.roleService.delete(1L);
//		//test delete role
//		this.roleService.delete(role);
	}
}