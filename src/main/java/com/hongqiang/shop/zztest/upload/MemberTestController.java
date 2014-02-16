package com.hongqiang.shop.zztest.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("emberTestController")
@RequestMapping({ "/test" })
public class MemberTestController {
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = { "/tt" }, method = RequestMethod.GET)
	public void test() {
		System.out.println("we are here.");
		Member member = this.memberService.find(11L);
		System.out.println("member="+member.getUsername()+", "+member.getEmail()+" ,"+member.getName());
		member.setEmail("liamn@163.com");
		member.setName("liman");
		System.out.println("member="+member.getUsername()+", "+member.getEmail()+" ,"+member.getName());
	}
}
