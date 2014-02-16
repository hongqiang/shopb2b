package com.hongqiang.shop.modules.account.web.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.ResourceNotFoundException;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.PromotionService;
import com.hongqiang.shop.modules.entity.Promotion;

@Controller("shopPromotionController")
@RequestMapping({ "${frontPath}/promotion" })
public class PromotionController extends BaseController {

	@Autowired
	private PromotionService promotionService;

	@RequestMapping(value = { "/content/{id}" }, method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		Promotion localPromotion = (Promotion) this.promotionService.find(id);
		if (localPromotion == null)
			throw new ResourceNotFoundException();
		model.addAttribute("promotion", localPromotion);
		return "/shop/promotion/content";
	}
}