package com.hongqiang.shop.website.service;

import java.util.Map;

import com.hongqiang.shop.modules.entity.ProductNotify;
import com.hongqiang.shop.modules.entity.SafeKey;

public interface MailService {
	public void send(String smtpFromMail, String smtpHost, Integer smtpPort,
			String smtpUsername, String smtpPassword, String toMail,
			String subject, String templatePath, Map<String, Object> model,
			boolean async);

	public void send(String toMail, String subject, String templatePath,
			Map<String, Object> model, boolean async);

	public void send(String toMail, String subject, String templatePath,
			Map<String, Object> model);

	public void send(String toMail, String subject, String templatePath);

	public void sendTestMail(String smtpFromMail, String smtpHost,
			Integer smtpPort, String smtpUsername, String smtpPassword,
			String toMail);

	public void sendFindPasswordMail(String toMail, String username,
			SafeKey safeKey);

	public void sendProductNotifyMail(ProductNotify productNotify);
}