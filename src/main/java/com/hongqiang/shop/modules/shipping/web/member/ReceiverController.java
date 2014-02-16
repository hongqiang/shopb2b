package com.hongqiang.shop.modules.shipping.web.member;

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
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Receiver;
import com.hongqiang.shop.modules.shipping.service.ReceiverService;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberReceiverController")
@RequestMapping({ "${memberPath}/receiver" })
public class ReceiverController extends BaseController {
	private static final int PAGE_SIZE = 10;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ReceiverService receiverService;

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member localMember = this.memberService.getCurrent();
		Pageable localPageable = new Pageable(pageNumber,
				Integer.valueOf(PAGE_SIZE));
		model.addAttribute("page",
				this.receiverService.findPage(localMember, localPageable));
		return "shop/member/receiver/list";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String add(RedirectAttributes redirectAttributes) {
		Member localMember = this.memberService.getCurrent();
		if ((Receiver.MAX_RECEIVER_COUNT != null)
				&& (localMember.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT
						.intValue())) {
			addMessage(redirectAttributes, Message.warn(
					"shop.member.receiver.addCountNotAllowed",
					new Object[] { Receiver.MAX_RECEIVER_COUNT }));
			return "redirect:list.jhtml";
		}
		return "shop/member/receiver/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(Receiver receiver, Long areaId,
			RedirectAttributes redirectAttributes) {
		receiver.setArea((Area) this.areaService.find(areaId));
		if (!beanValidator(redirectAttributes, receiver, new Class[0]))
			return SHOP_ERROR_PAGE;
		Member localMember = this.memberService.getCurrent();
		if ((Receiver.MAX_RECEIVER_COUNT != null)
				&& (localMember.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT
						.intValue()))
			return SHOP_ERROR_PAGE;
		receiver.setMember(localMember);
		this.receiverService.save(receiver);
		addMessage(redirectAttributes, SHOP_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model,
			RedirectAttributes redirectAttributes) {
		Receiver localReceiver = (Receiver) this.receiverService.find(id);
		if (localReceiver == null)
			return SHOP_ERROR_PAGE;
		Member localMember = this.memberService.getCurrent();
		if (localReceiver.getMember() != localMember)
			return SHOP_ERROR_PAGE;
		model.addAttribute("receiver", localReceiver);
		return "shop/member/receiver/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(Receiver receiver, Long id, Long areaId,
			RedirectAttributes redirectAttributes) {
		receiver.setArea((Area) this.areaService.find(areaId));
		if (!beanValidator(redirectAttributes, receiver, new Class[0]))
			return SHOP_ERROR_PAGE;
		Receiver localReceiver = (Receiver) this.receiverService.find(id);
		if (localReceiver == null)
			return SHOP_ERROR_PAGE;
		Member localMember = this.memberService.getCurrent();
		if (localReceiver.getMember() != localMember)
			return SHOP_ERROR_PAGE;
		this.receiverService.update(receiver, new String[] { "member" });
		addMessage(redirectAttributes, SHOP_SUCCESS);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long id) {
		Receiver localReceiver = (Receiver) this.receiverService.find(id);
		if (localReceiver == null)
			return SHOP_ERROR;
		Member localMember = this.memberService.getCurrent();
		if (localReceiver.getMember() != localMember)
			return SHOP_ERROR;
		this.receiverService.delete(id);
		return SHOP_SUCCESS;
	}
}