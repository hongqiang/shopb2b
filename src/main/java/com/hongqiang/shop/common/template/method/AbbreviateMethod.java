package com.hongqiang.shop.common.template.method;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;

import java.util.List;
//import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
/**
 * ftl模板调用的压缩字符串的类
 * @author jackial
 *
 */
@Component("abbreviateMethod")
public class AbbreviateMethod implements TemplateMethodModel {
	private static final Pattern pattern = Pattern
			.compile("[\\u4e00-\\u9fa5\\ufe30-\\uffa0]+$");

	/**
	 * 重载TemplateMethodModel的exec方法，接收一个list，为ftl模板中定义方法的参数列表
	 */
	public Object exec(@SuppressWarnings("rawtypes") List arguments) {
		if ((arguments != null) && (!arguments.isEmpty())
				&& (arguments.get(0) != null)
				&& (StringUtils.isNotEmpty(arguments.get(0).toString()))) {
			Integer localInteger = null;
			String str = null;
			if (arguments.size() == 2) {
				if (arguments.get(1) != null)
					localInteger = Integer.valueOf(arguments.get(1).toString());
			} else if (arguments.size() > 2) {
				if (arguments.get(1) != null)
					localInteger = Integer.valueOf(arguments.get(1).toString());
				if (arguments.get(2) != null)
					str = arguments.get(2).toString();
			}
			return new SimpleScalar(compose(arguments.get(0).toString(),
					localInteger, str));
		}
		return null;
	}

	/**
	 * 传进需要压缩的字符串和传出的字符串长度，(可选字符串后缀)，返回压缩后的字符串。
	 * 
	 * @param oriString  待压缩的字符串
	 * @param stringLength   字符串压缩后的长度，不包含subfix字符串
	 * @param subfix    字符串后缀，可选
	 * @return 压缩后的字符串
	 */
	private String compose(String oriString, Integer stringLength, String subfix) {
		if (stringLength != null) {
			int i = 0;
			int j = 0;
			while (i < oriString.length()) {
				//中文字符占两位，所以每个字符位置加2
				j = pattern.matcher(String.valueOf(oriString.charAt(i))).find() ? j + 2 : j + 1;
				if (j >= stringLength.intValue())
					break;
				i++;
			}
			if (i < oriString.length()) {
				if (subfix != null)
					return oriString.substring(0, i + 1) + subfix;
				return oriString.substring(0, i + 1);
			}
			return oriString;
		}
		if (subfix != null)
			return oriString + subfix;
		return oriString;
	}
}