package com.hongqiang.shop.website.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.SpringContextHolder;
import com.hongqiang.shop.modules.entity.ProductNotify;
import com.hongqiang.shop.modules.entity.SafeKey;
import com.hongqiang.shop.modules.util.service.TemplateService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service("mailServiceImpl")
public class MailServiceImpl implements MailService {

	class SendAsynchronous implements Runnable {
		private JavaMailSenderImpl javaMailSender;
		private MimeMessage mimeMessage;
		
		public SendAsynchronous(JavaMailSenderImpl javaMailSender, MimeMessage mimeMessage){
			this.javaMailSender = javaMailSender;
			this.mimeMessage = mimeMessage;
		}
		
		public void run() {
			this.javaMailSender.send(this.mimeMessage);
		}
	}

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private TemplateService templateService;

	private void sendAsync(MimeMessage paramMimeMessage) {
		try {
			this.taskExecutor.execute(new SendAsynchronous(this.javaMailSender,
					paramMimeMessage));
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public void send(String smtpFromMail, String smtpHost, Integer smtpPort,
			String smtpUsername, String smtpPassword, String toMail,
			String subject, String templatePath, Map<String, Object> model,
			boolean async) {
		Assert.hasText(smtpFromMail);
		Assert.hasText(smtpHost);
		Assert.notNull(smtpPort);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		Assert.hasText(toMail);
		Assert.hasText(subject);
		Assert.hasText(templatePath);
		try {
			Setting localSetting = SettingUtils.get();
			Configuration localConfiguration = this.freeMarkerConfigurer
					.getConfiguration();
			freemarker.template.Template localTemplate = localConfiguration
					.getTemplate(templatePath);
			String str = FreeMarkerTemplateUtils.processTemplateIntoString(
					localTemplate, model);
			this.javaMailSender.setHost(smtpHost);
			this.javaMailSender.setPort(smtpPort.intValue());
			this.javaMailSender.setUsername(smtpUsername);
			this.javaMailSender.setPassword(smtpPassword);
			MimeMessage localMimeMessage = this.javaMailSender
					.createMimeMessage();
			MimeMessageHelper localMimeMessageHelper = new MimeMessageHelper(
					localMimeMessage, false, "utf-8");
			localMimeMessageHelper.setFrom(MimeUtility.encodeWord(localSetting
					.getSiteName()) + " <" + smtpFromMail + ">");
			localMimeMessageHelper.setSubject(subject);
			localMimeMessageHelper.setTo(toMail);
			localMimeMessageHelper.setText(str, true);
			if (async)
				sendAsync(localMimeMessage);
			else
				this.javaMailSender.send(localMimeMessage);
		} catch (TemplateException localTemplateException1) {
			localTemplateException1.printStackTrace();
		} catch (IOException localIOException1) {
			localIOException1.printStackTrace();
		} catch (MessagingException localMessagingException1) {
			localMessagingException1.printStackTrace();
		}
	}

	public void send(String toMail, String subject, String templatePath,
			Map<String, Object> model, boolean async) {
		Setting localSetting = SettingUtils.get();
		send(localSetting.getSmtpFromMail(), localSetting.getSmtpHost(),
				localSetting.getSmtpPort(), localSetting.getSmtpUsername(),
				localSetting.getSmtpPassword(), toMail, subject, templatePath,
				model, async);
	}

	public void send(String toMail, String subject, String templatePath,
			Map<String, Object> model) {
		Setting localSetting = SettingUtils.get();
		send(localSetting.getSmtpFromMail(), localSetting.getSmtpHost(),
				localSetting.getSmtpPort(), localSetting.getSmtpUsername(),
				localSetting.getSmtpPassword(), toMail, subject, templatePath,
				model, true);
	}

	public void send(String toMail, String subject, String templatePath) {
		Setting localSetting = SettingUtils.get();
		send(localSetting.getSmtpFromMail(), localSetting.getSmtpHost(),
				localSetting.getSmtpPort(), localSetting.getSmtpUsername(),
				localSetting.getSmtpPassword(), toMail, subject, templatePath,
				null, true);
	}

	public void sendTestMail(String smtpFromMail, String smtpHost,
			Integer smtpPort, String smtpUsername, String smtpPassword,
			String toMail) {
		Setting localSetting = SettingUtils.get();
		String str = SpringContextHolder.getMessage(
				"admin.setting.testMailSubject",
				new Object[] { localSetting.getSiteName() });
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("testMail");
		send(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword,
				toMail, str, localTemplate.getTemplatePath(), null, false);
	}

	public void sendFindPasswordMail(String toMail, String username,
			SafeKey safeKey) {
		Setting localSetting = SettingUtils.get();
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		localHashMap.put("username", username);
		localHashMap.put("safeKey", safeKey);
		String str = SpringContextHolder.getMessage(
				"shop.password.mailSubject",
				new Object[] { localSetting.getSiteName() });
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("findPasswordMail");
		send(toMail, str, localTemplate.getTemplatePath(), localHashMap);
	}

	public void sendProductNotifyMail(ProductNotify productNotify) {
		Setting localSetting = SettingUtils.get();
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		localHashMap.put("productNotify", productNotify);
		String str = SpringContextHolder.getMessage(
				"admin.productNotify.mailSubject",
				new Object[] { localSetting.getSiteName() });
		com.hongqiang.shop.modules.utils.Template localTemplate = this.templateService
				.get("productNotifyMail");
		send(productNotify.getEmail(), str, localTemplate.getTemplatePath(),
				localHashMap);
	}
}