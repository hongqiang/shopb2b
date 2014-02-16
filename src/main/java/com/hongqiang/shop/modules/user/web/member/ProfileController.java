package com.hongqiang.shop.modules.user.web.member;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.model.CommonAttributes;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.MemberAttribute;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.user.service.MemberAttributeService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberProfileController")
@RequestMapping({"${memberPath}/profile"})
public class ProfileController extends BaseController
{

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberAttributeService memberAttributeService;

  @Autowired
  private AreaService areaService;

  @RequestMapping(value={"/check_email"}, method=RequestMethod.GET)
  @ResponseBody
  public boolean checkEmail(String email)
  {
    if (StringUtils.isEmpty(email))
      return false;
    Member localMember = this.memberService.getCurrent();
    return this.memberService.emailUnique(localMember.getEmail(), email);
  }

  @RequestMapping(value={"/edit"}, method=RequestMethod.GET)
  public String edit(ModelMap model)
  {
    model.addAttribute("genders", Member.Gender.values());
    model.addAttribute("memberAttributes", this.memberAttributeService.findList());
    return "shop/member/profile/edit";
  }

  @SuppressWarnings("unchecked")
@RequestMapping(value={"/update"}, method=RequestMethod.POST)
  public String update(String email, HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
    if (!beanValidator(Member.class, "email", email, new Class[0]))
      return SHOP_ERROR_PAGE;
    Setting localSetting = SettingUtils.get();
    Member localMember = this.memberService.getCurrent();
    if ((!localSetting.getIsDuplicateEmail().booleanValue()) && (!this.memberService.emailUnique(localMember.getEmail(), email)))
      return SHOP_ERROR_PAGE;
    localMember.setEmail(email);
    List<MemberAttribute> localList = this.memberAttributeService.findList();
    Iterator<MemberAttribute> localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      MemberAttribute localMemberAttribute = (MemberAttribute)localIterator.next();
      String str = request.getParameter("memberAttribute_" + localMemberAttribute.getId());
      if ((localMemberAttribute.getType() == MemberAttribute.Type.name) || (localMemberAttribute.getType() == MemberAttribute.Type.address) || (localMemberAttribute.getType() == MemberAttribute.Type.zipCode) || (localMemberAttribute.getType() == MemberAttribute.Type.phone) || (localMemberAttribute.getType() == MemberAttribute.Type.mobile) || (localMemberAttribute.getType() == MemberAttribute.Type.text) || (localMemberAttribute.getType() == MemberAttribute.Type.select))
      {
        if ((localMemberAttribute.getIsRequired().booleanValue()) && (StringUtils.isEmpty(str)))
          return SHOP_ERROR_PAGE;
        localMember.setAttributeValue(localMemberAttribute, str);
      }
      else
      {
        Member.Gender localGender;
        if (localMemberAttribute.getType() == MemberAttribute.Type.gender)
        {
          localGender = StringUtils.isNotEmpty(str) ? Member.Gender.valueOf(str) : null;
          if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
            return SHOP_ERROR_PAGE;
          localMember.setGender(localGender);
        }
        else if (localMemberAttribute.getType() == MemberAttribute.Type.birth)
        {
          try
          {
            Date birthDate = StringUtils.isNotEmpty(str) ? DateUtils.parseDate(str, CommonAttributes.DATE_PATTERNS) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && (birthDate == null))
              return SHOP_ERROR_PAGE;
            localMember.setBirth(birthDate);
          }
          catch (ParseException localParseException1)
          {
            return SHOP_ERROR_PAGE;
          }
        }
        else
        {
          if (localMemberAttribute.getType() == MemberAttribute.Type.area)
          {
            Area area = StringUtils.isNotEmpty(str) ? (Area)this.areaService.find(Long.valueOf(str)) : null;
            if (area != null)
              localMember.setArea(area);
            else if (localMemberAttribute.getIsRequired().booleanValue())
              return SHOP_ERROR_PAGE;
          }
          else
          {
            if (localMemberAttribute.getType() != MemberAttribute.Type.checkbox)
              continue;
            Object attributeValue = request.getParameterValues("memberAttribute_" + localMemberAttribute.getId());
            Object localAttributeValue = attributeValue != null ? Arrays.asList(attributeValue) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && ((localAttributeValue == null) || (((List<MemberAttribute>) localAttributeValue).isEmpty())))
              return SHOP_ERROR_PAGE;
            localMember.setAttributeValue(localMemberAttribute, localAttributeValue);
          }
        }
      }
    }
    this.memberService.update(localMember);
    addMessage(redirectAttributes, SHOP_SUCCESS);
    return "redirect:edit.jhtml";
  }
}