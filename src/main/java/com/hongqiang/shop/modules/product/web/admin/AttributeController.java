package com.hongqiang.shop.modules.product.web.admin;

import java.util.Iterator;

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
import com.hongqiang.shop.modules.entity.Attribute;
import com.hongqiang.shop.modules.entity.BaseEntity;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.product.service.AttributeService;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;

@Controller("adminAttributeController")
@RequestMapping({ "${adminPath}/attribute" })
public class AttributeController extends BaseController {

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("attributeValuePropertyCount", Integer.valueOf(20));
		return "/admin/attribute/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(Attribute attribute, Long productCategoryId,
			RedirectAttributes redirectAttributes) {
		Iterator<String> localIterator = attribute.getOptions().iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			if (!StringUtils.isEmpty(str))
				continue;
			localIterator.remove();
		}
		attribute
				.setProductCategory((ProductCategory) this.productCategoryService
						.find(productCategoryId));
		if (!beanValidator(redirectAttributes, attribute,
				new Class[] { BaseEntity.Save.class }))
			return ERROR_PAGE;
		if (attribute.getProductCategory().getAttributes().size() >= 20) {
			addMessage(redirectAttributes, Message.error(
					"admin.attribute.addCountNotAllowed",
					new Object[] { Integer.valueOf(20) }));
		} else {
			// 得到index值
			Integer index = 0;
			boolean[] indexes = new boolean[20];
			for (Attribute attr : attribute.getProductCategory()
					.getAttributes()) {
				indexes[attr.getPropertyIndex()] = true;
			}
			for (int i = 0; i < 20; i++) {
				if (!indexes[i]) {
					index = i;
					break;
				}
			}
			attribute.setPropertyIndex(index);
			this.attributeService.save(attribute);
			addMessage(redirectAttributes, ADMIN_SUCCESS);
		}
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("attributeValuePropertyCount", Integer.valueOf(20));
		model.addAttribute("attribute", this.attributeService.find(id));
		return "/admin/attribute/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(Attribute attribute,
			RedirectAttributes redirectAttributes) {
		Iterator<String> localIterator = attribute.getOptions().iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			if (!StringUtils.isEmpty(str))
				continue;
			localIterator.remove();
		}
		if (!beanValidator(redirectAttributes, attribute, new Class[0]))
			return ERROR_PAGE;
		this.attributeService.update(attribute, new String[] { "propertyIndex",
				"productCategory" });
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.attributeService.findPage(pageable));
		return "/admin/attribute/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		this.attributeService.delete(ids);
		return ADMIN_SUCCESS;
	}
}