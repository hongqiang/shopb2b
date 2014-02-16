package com.hongqiang.shop.modules.content.web.admin;

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

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.content.service.MessageService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.common.utils.Message;

@Controller("adminMessageController")
@RequestMapping({ "${adminPath}/message" })
public class MessageController extends BaseController {

	@Autowired
	MessageService messageService;

	@Autowired
	MemberService memberService;

	@RequestMapping(value = { "/check_username" }, method = RequestMethod.GET)
	@ResponseBody
	public boolean checkUsername(String username) {
		return this.memberService.usernameExists(username);
	}

	@RequestMapping(value = { "/send" }, method = RequestMethod.GET)
	public String send(Long draftMessageId, Model model) {
		com.hongqiang.shop.modules.entity.Message msg = (com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(draftMessageId);
		if ((msg != null)&& (msg.getIsDraft().booleanValue())&& (msg.getSender() == null))
			model.addAttribute("draftMessage", msg);
		return "admin/message/send";
	}

	@RequestMapping(value = { "/send" }, method = RequestMethod.POST)
	public String send(Long draftMessageId, String username, String title,
			String content,
			@RequestParam(defaultValue = "false") Boolean isDraft,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!beanValidator(com.hongqiang.shop.modules.entity.Message.class,
				"content", content, new Class[0]))
			return ERROR_PAGE;
		com.hongqiang.shop.modules.entity.Message draftMsg = (com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(draftMessageId);
		if ((draftMsg != null)&& (draftMsg.getIsDraft().booleanValue())&& (draftMsg.getSender() == null))
			this.messageService.delete(draftMsg);
		Member localMember = null;
		if (StringUtils.isNotEmpty(username)) {
			localMember = this.memberService.findByUsername(username);
			if (localMember == null)
				return ERROR_PAGE;
		}
		com.hongqiang.shop.modules.entity.Message msg = new com.hongqiang.shop.modules.entity.Message();
		msg.setTitle(title);
		msg.setContent(content);
		msg.setIp(request.getRemoteAddr());
		msg.setIsDraft(isDraft);
		msg.setSenderRead(Boolean.valueOf(true));
		msg.setReceiverRead(Boolean.valueOf(false));
		msg.setSenderDelete(Boolean.valueOf(false));
		msg.setReceiverDelete(Boolean.valueOf(false));
		msg.setSender(null);
		msg.setReceiver(localMember);
		msg.setForMessage(null);
		msg.setReplyMessages(null);
		this.messageService.save(msg);
		if (isDraft.booleanValue()) {
			addMessage(redirectAttributes, Message.success(
					"admin.message.saveDraftSuccess", new Object[0]));
			return "redirect:draft.jhtml";
		}
		addMessage(redirectAttributes,
				Message.success("admin.message.sendSuccess", new Object[0]));
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view(Long id, Model model) {
		com.hongqiang.shop.modules.entity.Message msg = 
				(com.hongqiang.shop.modules.entity.Message) this.messageService.find(id);
		if ((msg == null)|| (msg.getIsDraft().booleanValue())|| (msg.getForMessage() != null))
			return ERROR_PAGE;
		if (((msg.getSender() != null) && (msg.getReceiver() != null))
				|| ((msg.getReceiver() == null) && (msg.getReceiverDelete().booleanValue()))
				|| ((msg.getSender() == null) && (msg.getSenderDelete().booleanValue())))
			return ERROR_PAGE;
		if (msg.getReceiver() == null)
			msg.setReceiverRead(Boolean.valueOf(true));
		else
			msg.setSenderRead(Boolean.valueOf(true));
		this.messageService.update(msg);
		model.addAttribute("adminMessage", msg);
		return "/admin/message/view";
	}

	@RequestMapping(value = { "/reply" }, method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(com.hongqiang.shop.modules.entity.Message.class,
				"content", content, new Class[0]))
			return ERROR_PAGE;
		com.hongqiang.shop.modules.entity.Message msg = (com.hongqiang.shop.modules.entity.Message) this.messageService
				.find(id);
		if ((msg == null)|| (msg.getIsDraft().booleanValue())|| (msg.getForMessage() != null))
			return ERROR_PAGE;
		if (((msg.getSender() != null) && (msg.getReceiver() != null))
				|| ((msg.getReceiver() == null) && (msg.getReceiverDelete().booleanValue()))
				|| ((msg.getSender() == null) && (msg.getSenderDelete().booleanValue())))
			return ERROR_PAGE;
		com.hongqiang.shop.modules.entity.Message replyMsg = new com.hongqiang.shop.modules.entity.Message();
		replyMsg.setTitle("reply: " + msg.getTitle());
		replyMsg.setContent(content);
		replyMsg.setIp(request.getRemoteAddr());
		replyMsg.setIsDraft(Boolean.valueOf(false));
		replyMsg.setSenderRead(Boolean.valueOf(true));
		replyMsg.setReceiverRead(Boolean.valueOf(false));
		replyMsg.setSenderDelete(Boolean.valueOf(false));
		replyMsg.setReceiverDelete(Boolean.valueOf(false));
		replyMsg.setSender(null);
		replyMsg.setReceiver(msg.getReceiver() == null ? msg.getSender() : msg.getReceiver());
		if (((msg.getReceiver() == null) && (!msg.getSenderDelete().booleanValue()))
				|| ((msg.getSender() == null) && (!msg.getReceiverDelete().booleanValue())))
			replyMsg.setForMessage(msg);
		replyMsg.setReplyMessages(null);
		this.messageService.save(replyMsg);
		if (msg.getSender() == null) {
			msg.setSenderRead(Boolean.valueOf(true));
			msg.setReceiverRead(Boolean.valueOf(false));
		} else {
			msg.setSenderRead(Boolean.valueOf(false));
			msg.setReceiverRead(Boolean.valueOf(true));
		}
		this.messageService.update(msg);
		if (((msg.getReceiver() == null) && (!msg.getSenderDelete().booleanValue()))
				|| ((msg.getSender() == null) && (!msg.getReceiverDelete().booleanValue()))) {
			addMessage(redirectAttributes, ADMIN_SUCCESS);
			return "redirect:view.jhtml?id=" + msg.getId();
		}
		addMessage(redirectAttributes,Message.success("admin.message.replySuccess", new Object[0]));
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, Model model) {
		model.addAttribute("page", this.messageService.findPage(null, pageable));
		return "/admin/message/list";
	}

	@RequestMapping(value = { "/draft" }, method = RequestMethod.GET)
	public String draft(Pageable pageable, Model model) {
		model.addAttribute("page",this.messageService.findDraftPage(null, pageable));
		return "/admin/message/draft";
	}

	@RequestMapping(value = { "delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null)
			for (Long localLong : ids)
				this.messageService.delete(localLong, null);
		return ADMIN_SUCCESS;
	}
}