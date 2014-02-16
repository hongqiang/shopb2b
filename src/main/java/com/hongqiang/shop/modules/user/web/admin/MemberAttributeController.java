package com.hongqiang.shop.modules.user.web.admin;

import java.util.Iterator;
import java.util.List;

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
import com.hongqiang.shop.modules.entity.BaseEntity;
import com.hongqiang.shop.modules.entity.MemberAttribute;
import com.hongqiang.shop.modules.user.service.MemberAttributeService;

@Controller("adminMemberAttributeController")
@RequestMapping({ "${adminPath}/member_attribute" })
public class MemberAttributeController extends BaseController {

	@Autowired
	private MemberAttributeService memberAttributeService;

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String add(ModelMap model, RedirectAttributes redirectAttributes) {
		if (this.memberAttributeService.count() - 8L >= 10L)
			addMessage(redirectAttributes, Message.warn(
					"admin.memberAttribute.addCountNotAllowed",
					new Object[] { Integer.valueOf(10) }));
		return "/admin/member_attribute/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(MemberAttribute memberAttribute,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(redirectAttributes, memberAttribute,
				new Class[] { BaseEntity.Save.class }))
			return ERROR_PAGE;
		if ((memberAttribute.getType() == MemberAttribute.Type.select)
				|| (memberAttribute.getType() == MemberAttribute.Type.checkbox)) {
			List<String> localObject = memberAttribute.getOptions();
			if (localObject != null) {
				Iterator<String> localIterator = (localObject).iterator();
				while (localIterator.hasNext()) {
					String str = (String) localIterator.next();
					if (!StringUtils.isEmpty(str))
						continue;
					localIterator.remove();
				}
			}
			if ((localObject == null) || ((localObject).isEmpty()))
				return ERROR_PAGE;
		} else if (memberAttribute.getType() == MemberAttribute.Type.text) {
			memberAttribute.setOptions(null);
		} else {
			return ERROR_PAGE;
		}
		Object localObject = this.memberAttributeService
				.findUnusedPropertyIndex();
		if (localObject == null)
			return ERROR_PAGE;
		memberAttribute.setPropertyIndex((Integer) localObject);
		this.memberAttributeService.save(memberAttribute);
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return (String) "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("memberAttribute",
				this.memberAttributeService.find(id));
		return "/admin/member_attribute/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(MemberAttribute memberAttribute,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(redirectAttributes, memberAttribute, new Class[0]))
			return ERROR_PAGE;
		MemberAttribute localMemberAttribute = (MemberAttribute) this.memberAttributeService
				.find(memberAttribute.getId());
		if (localMemberAttribute == null)
			return ERROR_PAGE;
		if ((localMemberAttribute.getType() == MemberAttribute.Type.select)
				|| (localMemberAttribute.getType() == MemberAttribute.Type.checkbox)) {
			List<String> localList = memberAttribute.getOptions();
			if (localList != null) {
				Iterator<String> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					String str = (String) localIterator.next();
					if (!StringUtils.isEmpty(str))
						continue;
					localIterator.remove();
				}
			}
			if ((localList == null) || (localList.isEmpty()))
				return ERROR_PAGE;
		} else {
			memberAttribute.setOptions(null);
		}
		this.memberAttributeService.update(memberAttribute, new String[] {
				"type", "propertyIndex" });
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page",
				this.memberAttributeService.findPage(pageable));
		return "/admin/member_attribute/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		this.memberAttributeService.delete(ids);
		return ADMIN_SUCCESS;
	}

	@RequestMapping(value = { "/domematt" }, method = RequestMethod.GET)
	public void doit() {
		// test find id
//		MemberAttribute memberAttribute = this.memberAttributeService.find(1L);
//		System.out.println(memberAttribute.getName());
//		// test find page
//		Pageable pageable = new Pageable(1, 40);
//		Page<MemberAttribute> page = this.memberAttributeService
//				.findPage(pageable);
//		for (MemberAttribute o : page.getList()) {
//			System.out.print(o.getName() + "\n");
//		}
//		// test find list
//		List<MemberAttribute> lis = this.memberAttributeService.findList();
//		for (MemberAttribute o : lis) {
//			System.out.print(o.getName() + "\n");
//		}
//		// test find unused
//		Integer inr = this.memberAttributeService.findUnusedPropertyIndex();
//		System.out.println("inr= " + inr);
//		// test count
//		// long count = this.memberAttributeService.count();
//		// System.out.println("count= "+count);
//		// test update
//		memberAttribute.setName("nothing");
//		this.memberAttributeService.update(memberAttribute);
//		// test save
//		// next
//		// test delete T
//		this.memberAttributeService.delete(memberAttribute);
	}
}