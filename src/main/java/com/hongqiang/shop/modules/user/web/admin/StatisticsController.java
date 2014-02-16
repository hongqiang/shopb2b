package com.hongqiang.shop.modules.user.web.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.util.service.CacheService;

@Controller("statisticsController")
@RequestMapping({ "${adminPath}/statistics" })
public class StatisticsController extends BaseController {

	@Autowired
	private CacheService cacheService;

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view() {
		return "/admin/statistics/view";
	}

	@RequestMapping(value = { "/setting" }, method = RequestMethod.GET)
	public String setting() {
		return "/admin/statistics/setting";
	}

	@RequestMapping(value = { "/setting" }, method = RequestMethod.POST)
	public String setting(
			@RequestParam(defaultValue = "false") Boolean isEnabled,
			RedirectAttributes redirectAttributes) {
		Setting localSetting = SettingUtils.get();
		if ((isEnabled.booleanValue())
				&& ((StringUtils.isEmpty(localSetting.getCnzzSiteId())) || (StringUtils
						.isEmpty(localSetting.getCnzzPassword()))))
			try {
				String str1 = "http://intf.cnzz.com/user/companion/shopxx.php?domain="
						+ localSetting.getSiteUrl()
						+ "&key="
						+ DigestUtils.md5Hex(new StringBuilder(String
								.valueOf(localSetting.getSiteUrl())).append(
								"Lfg4uP0H").toString());
				URLConnection localURLConnection = new URL(str1)
						.openConnection();
				BufferedReader localBufferedReader = new BufferedReader(
						new InputStreamReader(
								localURLConnection.getInputStream()));
				String str2 = null;
				while ((str2 = localBufferedReader.readLine()) != null)
					if (str2.contains("@"))
						break;
				if (str2 != null) {
					localSetting.setCnzzSiteId(StringUtils.substringBefore(
							str2, "@"));
					localSetting.setCnzzPassword(StringUtils.substringAfter(
							str2, "@"));
				}
			} catch (IOException localIOException1) {
				localIOException1.printStackTrace();
			}
		localSetting.setIsCnzzEnabled(isEnabled);
		SettingUtils.set(localSetting);
		this.cacheService.clear();
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return "redirect:setting.jhtml";
	}
}