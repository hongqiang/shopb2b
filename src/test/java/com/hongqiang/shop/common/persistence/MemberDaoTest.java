package com.hongqiang.shop.common.persistence;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hongqiang.shop.common.test.SpringTransactionalContextTests;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.dao.MemberDao;
import com.hongqiang.shop.modules.user.service.MemberService;

public class MemberDaoTest  extends SpringTransactionalContextTests{
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private MemberService memberService;
	
	@Test
	public void find(){
		System.out.println("===============================begin==================================");
		Member member = this.memberDao.find(11L);
		System.out.println("member="+member.getUsername()+", "+member.getEmail()+" ,"
				+member.getName()+", "+member.getBalance());
		member.setEmail("liamn@163.com");
		member.setName("liman");
		this.memberDao.update(member);
		Member member2 = this.memberDao.find(11L);
		System.out.println("member="+member2.getUsername()+", "+member2.getEmail()+" ,"
				+member2.getName()+", "+member2.getBalance());
		System.out.println("===============================end====================================");
	
	
		System.out.println("===============================begin====================================");
		Member memberSer1 = this.memberService.find(11L);
		System.out.println("member="+memberSer1.getUsername()+", "+memberSer1.getEmail()+" ,"
				+memberSer1.getName()+", "+memberSer1.getBalance());
		memberSer1.setEmail("liamn111@163.com");
		memberSer1.setName("limansdf");
		this.memberService.update(memberSer1);
		Member memberSer2 = this.memberService.find(11L);
		System.out.println("member="+memberSer2.getUsername()+", "+memberSer2.getEmail()+" ,"
				+memberSer2.getName()+", "+memberSer2.getBalance());
		System.out.println("===============================end====================================");
	}
}
