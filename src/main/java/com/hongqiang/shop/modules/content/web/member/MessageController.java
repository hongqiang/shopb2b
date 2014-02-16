package com.hongqiang.shop.modules.content.web.member;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.MessageService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberMessageController")
@RequestMapping({ "${memberPath}/message" })
public class MessageController extends BaseController {
	private static final int PAGE_SIZE = 10;

	@Autowired
	MessageService messageService;

	@Autowired
	MemberService memberService;

	@RequestMapping(value = { "/check_username" }, method = RequestMethod.GET)
	@ResponseBody
	public boolean checkUsername(String username) {
		return (!StringUtils.equalsIgnoreCase(username,
				this.memberService.getCurrentUsername()))
				&& (this.memberService.usernameExists(username));
	}

	@RequestMapping(value = { "/send" }, method = RequestMethod.GET)
	public String send(Long draftMessageId, Model model) {
		com.hongqiang.shop.modules.entity.Message localMessage =
				(com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(draftMessageId);
		if ((localMessage != null)
				&& (localMessage.getIsDraft().booleanValue())
				&& (localMessage.getSender() == this.memberService.getCurrent()))
			model.addAttribute("draftMessage", localMessage);
		return "/shop/member/message/send";
	}

	@RequestMapping(value = { "/send" }, method = RequestMethod.POST)
	public String send(Long draftMessageId, String username, String title,
			String content,
			@RequestParam(defaultValue = "false") Boolean isDraft,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!beanValidator(com.hongqiang.shop.modules.entity.Message.class,
				"content", content, new Class[0]))
			return SHOP_ERROR_PAGE;
		Member localMember1 = this.memberService.getCurrent();
		com.hongqiang.shop.modules.entity.Message localMessage1 = 
				(com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(draftMessageId);
		if ((localMessage1 != null)
				&& (localMessage1.getIsDraft().booleanValue())
				&& (localMessage1.getSender() == localMember1))
			this.messageService.delete(localMessage1);
		Member localMember2 = null;
		if (StringUtils.isNotEmpty(username)) {
			localMember2 = this.memberService.findByUsername(username);
			if ((localMember2 == null) || (localMember2 == localMember1))
				return SHOP_ERROR_PAGE;
		}
		com.hongqiang.shop.modules.entity.Message localMessage2 = 
				new com.hongqiang.shop.modules.entity.Message();
		localMessage2.setTitle(title);
		localMessage2.setContent(content);
		localMessage2.setIp(request.getRemoteAddr());
		localMessage2.setIsDraft(isDraft);
		localMessage2.setSenderRead(Boolean.valueOf(true));
		localMessage2.setReceiverRead(Boolean.valueOf(false));
		localMessage2.setSenderDelete(Boolean.valueOf(false));
		localMessage2.setReceiverDelete(Boolean.valueOf(false));
		localMessage2.setSender(localMember1);
		localMessage2.setReceiver(localMember2);
		this.messageService.save(localMessage2);
		if (isDraft.booleanValue()) {
			addMessage(redirectAttributes, Message.success(
					"shop.member.message.saveDraftSuccess", new Object[0]));
			return "redirect:draft.jhtml";
		}
		addMessage(redirectAttributes, Message.success(
				"shop.member.message.sendSuccess", new Object[0]));
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view(Long id, Model model) {
		com.hongqiang.shop.modules.entity.Message localMessage = 
				(com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(id);
		if ((localMessage == null)
				|| (localMessage.getIsDraft().booleanValue())
				|| (localMessage.getForMessage() != null))
			return SHOP_ERROR_PAGE;
		Member localMember = this.memberService.getCurrent();
		if (((localMessage.getSender() != localMember) && (localMessage
				.getReceiver() != localMember))
				|| ((localMessage.getReceiver() == localMember) && (localMessage
						.getReceiverDelete().booleanValue()))
				|| ((localMessage.getSender() == localMember) && (localMessage
						.getSenderDelete().booleanValue())))
			return SHOP_ERROR_PAGE;
		if (localMember == localMessage.getReceiver())
			localMessage.setReceiverRead(Boolean.valueOf(true));
		else
			localMessage.setSenderRead(Boolean.valueOf(true));
		this.messageService.update(localMessage);
		model.addAttribute("memberMessage", localMessage);
		return "/shop/member/message/view";
	}

	@RequestMapping(value = { "/reply" }, method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(com.hongqiang.shop.modules.entity.Message.class,
				"content", content, new Class[0]))
			return SHOP_ERROR_PAGE;
		com.hongqiang.shop.modules.entity.Message localMessage1 = 
				(com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(id);
		if ((localMessage1 == null)
				|| (localMessage1.getIsDraft().booleanValue())
				|| (localMessage1.getForMessage() != null))
			return SHOP_ERROR_PAGE;
		Member localMember = this.memberService.getCurrent();
		if (((localMessage1.getSender() != localMember) && (localMessage1
				.getReceiver() != localMember))
				|| ((localMessage1.getReceiver() == localMember) && (localMessage1
						.getReceiverDelete().booleanValue()))
				|| ((localMessage1.getSender() == localMember) && (localMessage1
						.getSenderDelete().booleanValue())))
			return SHOP_ERROR_PAGE;
		com.hongqiang.shop.modules.entity.Message localMessage2 = new com.hongqiang.shop.modules.entity.Message();
		localMessage2.setTitle("reply: " + localMessage1.getTitle());
		localMessage2.setContent(content);
		localMessage2.setIp(request.getRemoteAddr());
		localMessage2.setIsDraft(Boolean.valueOf(false));
		localMessage2.setSenderRead(Boolean.valueOf(true));
		localMessage2.setReceiverRead(Boolean.valueOf(false));
		localMessage2.setSenderDelete(Boolean.valueOf(false));
		localMessage2.setReceiverDelete(Boolean.valueOf(false));
		localMessage2.setSender(localMember);
		localMessage2
				.setReceiver(localMember == localMessage1.getReceiver() ? localMessage1
						.getSender() : localMessage1.getReceiver());
		if (((localMember == localMessage1.getReceiver()) && (!localMessage1
				.getSenderDelete().booleanValue()))
				|| ((localMember == localMessage1.getSender()) && (!localMessage1
						.getReceiverDelete().booleanValue())))
			localMessage2.setForMessage(localMessage1);
		this.messageService.save(localMessage2);
		if (localMember.equals(localMessage1.getSender())) {
			localMessage1.setSenderRead(Boolean.valueOf(true));
			localMessage1.setReceiverRead(Boolean.valueOf(false));
		} else {
			localMessage1.setSenderRead(Boolean.valueOf(false));
			localMessage1.setReceiverRead(Boolean.valueOf(true));
		}
		this.messageService.update(localMessage1);
		if (((localMember == localMessage1.getReceiver()) && (!localMessage1
				.getSenderDelete().booleanValue()))
				|| ((localMember == localMessage1.getSender()) && (!localMessage1
						.getReceiverDelete().booleanValue()))) {
			addMessage(redirectAttributes, SHOP_SUCCESS);
			return "redirect:view.jhtml?id=" + localMessage1.getId();
		}
		addMessage(redirectAttributes, Message.success(
				"shop.member.message.replySuccess", new Object[0]));
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Integer pageNumber, Model model) {
		Pageable localPageable = new Pageable(pageNumber,
				Integer.valueOf(PAGE_SIZE));
		Member localMember = this.memberService.getCurrent();
		model.addAttribute("page",
				this.messageService.findPage(localMember, localPageable));
		return "/shop/member/message/list";
	}

	@RequestMapping(value = { "/draft" }, method = RequestMethod.GET)
	public String draft(Integer pageNumber, Model model) {
		Pageable localPageable = new Pageable(pageNumber,
				Integer.valueOf(PAGE_SIZE));
		Member localMember = this.memberService.getCurrent();
		model.addAttribute("page",
				this.messageService.findDraftPage(localMember, localPageable));
		return "/shop/member/message/draft";
	}

	@RequestMapping(value = { "delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long id) {
		Member localMember = this.memberService.getCurrent();
		this.messageService.delete(id, localMember);
		return SHOP_SUCCESS;
	}
}