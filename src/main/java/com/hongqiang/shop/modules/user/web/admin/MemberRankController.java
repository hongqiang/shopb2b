package com.hongqiang.shop.modules.user.web.admin;

import java.math.BigDecimal;

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
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.user.service.MemberRankService;

@Controller("adminMemberRankController")
@RequestMapping({"${adminPath}/member_rank"})
public class MemberRankController extends BaseController
{

  @Autowired
  private MemberRankService memberRankService;

  @RequestMapping(value={"/check_name"}, method=RequestMethod.GET)
  @ResponseBody
  public boolean checkName(String previousName, String name)
  {
    if (StringUtils.isEmpty(name))
      return false;
    return this.memberRankService.nameUnique(previousName, name);
  }

  @RequestMapping(value={"/check_amount"}, method=RequestMethod.GET)
  @ResponseBody
  public boolean checkAmount(BigDecimal previousAmount, BigDecimal amount)
  {
    if (amount == null)
      return false;
    return this.memberRankService.amountUnique(previousAmount, amount);
  }

  @RequestMapping(value={"/add"}, method=RequestMethod.GET)
  public String add(ModelMap model)
  {
    return "/admin/member_rank/add";
  }

  @RequestMapping(value={"/save"}, method=RequestMethod.POST)
  public String save(MemberRank memberRank, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,memberRank, new Class[0]))
      return ERROR_PAGE;
    if (this.memberRankService.nameExists(memberRank.getName()))
      return ERROR_PAGE;
    if (memberRank.getIsSpecial().booleanValue())
      memberRank.setAmount(null);
    else if ((memberRank.getAmount() == null) || (this.memberRankService.amountExists(memberRank.getAmount())))
      return ERROR_PAGE;
    memberRank.setMembers(null);
    memberRank.setPromotions(null);
    this.memberRankService.save(memberRank);
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("memberRank", this.memberRankService.find(id));
    return "/admin/member_rank/edit";
  }

  @RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(MemberRank memberRank, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(redirectAttributes,memberRank, new Class[0]))
      return ERROR_PAGE;
    MemberRank localMemberRank = (MemberRank)this.memberRankService.find(memberRank.getId());
    if (localMemberRank == null)
      return ERROR_PAGE;
    if (!this.memberRankService.nameUnique(localMemberRank.getName(), memberRank.getName()))
      return ERROR_PAGE;
    if (localMemberRank.getIsDefault().booleanValue())
      memberRank.setIsDefault(Boolean.valueOf(true));
    if (memberRank.getIsSpecial().booleanValue())
      memberRank.setAmount(null);
    else if ((memberRank.getAmount() == null) || (!this.memberRankService.amountUnique(localMemberRank.getAmount(), memberRank.getAmount())))
      return ERROR_PAGE;
    this.memberRankService.update(memberRank, new String[] { "members", "promotions" });
    addMessage(redirectAttributes, ADMIN_SUCCESS);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.memberRankService.findPage(pageable));
    return "/admin/member_rank/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids != null)
    {
      for (Long localLong : ids)
      {
        MemberRank localMemberRank = (MemberRank)this.memberRankService.find(localLong);
        if ((localMemberRank != null) && (localMemberRank.getMembers() != null) && (!localMemberRank.getMembers().isEmpty()))
          return Message.error("admin.memberRank.deleteExistNotAllowed", new Object[] { localMemberRank.getName() });
      }
      long l = this.memberRankService.count();
      if (ids.length >= l)
        return Message.error("admin.common.deleteAllNotAllowed", new Object[0]);
      this.memberRankService.delete(ids);
    }
    return ADMIN_SUCCESS;
  }
  
  @RequestMapping(value={"/dorank"},method=RequestMethod.GET)
	public void doit(){
//	  //test find id
//	  MemberRank admin1= this.memberRankService.find(1L);
//	  System.out.println(admin1.getName()+","+admin1.getAmount());
//	  //test find page
//	  Pageable pageable = new Pageable(1,40);
//  	Page<MemberRank> page=this.memberRankService.findPage(pageable);
//  	for (MemberRank o : page.getList()) {
//			System.out.print(o.getName()+", "+o.getAmount()+"\n");
//		}
//	  //test nameunique
//	  boolean sboo1=this.memberRankService.nameUnique(admin1.getName(), "do it");
//	  System.out.println("sboo1= "+sboo1);//true
//	  //test name exists
//	  boolean sboo2=this.memberRankService.nameExists(admin1.getName());
//	  System.out.println("sboo2= "+sboo2);//true
//	  //test amount exists
//	  boolean sboo3=this.memberRankService.amountExists(admin1.getAmount());
//	  System.out.println("sboo3= "+sboo3);//true
//	  //test save
//	  MemberRank memberRank = new MemberRank();
//	  memberRank.setName("do do");
//      memberRank.setAmount(null);
//	  memberRank.setMembers(null);
//      memberRank.setPromotions(null);
//      memberRank.setIsDefault(true);
//      memberRank.setIsSpecial(true);
//      memberRank.setScale(admin1.getScale());
//	  this.memberRankService.save(memberRank);
//	  //test count
////	  long l = this.memberRankService.count();
////	  System.out.println("count= "+count);
//	  //test find deflaut
//	  MemberRank mm = this.memberRankService.findDefault();
//	  System.out.println(mm.getName()+","+mm.getAmount());
//	  //test find amount
//	  mm = this.memberRankService.findByAmount(admin1.getAmount());
//	  System.out.println(mm.getName()+","+mm.getAmount());
//	  //test delete 
//	  this.memberRankService.delete(mm);
	}
}