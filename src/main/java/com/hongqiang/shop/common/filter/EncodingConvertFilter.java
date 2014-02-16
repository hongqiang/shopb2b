package com.hongqiang.shop.common.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class EncodingConvertFilter extends OncePerRequestFilter {
	// private String fromEncoding = "ISO-8859-1";
	// private String toEncoding = "UTF-8";
	private String fromEncoding;
	private String toEncoding;

	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain) {
		System.out.println("ios is here.");
		if (request.getMethod().equalsIgnoreCase("GET")) {
			Iterator<?> localIterator = request.getParameterMap().values().iterator();
			while (localIterator.hasNext()) {
				String[] arrayOfString = (String[]) localIterator.next();
				for (int i = 0; i < arrayOfString.length; i++)
					try {
						System.out.println("ios is here 2.");
						System.out.println("arrayOfString[i]="+ arrayOfString[i]);
						arrayOfString[i] = new String(arrayOfString[i].getBytes(this.fromEncoding),this.toEncoding);
						System.out.println("arrayOfString[i]="+ arrayOfString[i]);
					} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
						localUnsupportedEncodingException.printStackTrace();
					}
			}
		}
		try {
			filterChain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	public String getFromEncoding() {
		return this.fromEncoding;
	}

	public void setFromEncoding(String fromEncoding) {
		this.fromEncoding = fromEncoding;
	}

	public String getToEncoding() {
		return this.toEncoding;
	}

	public void setToEncoding(String toEncoding) {
		this.toEncoding = toEncoding;
	}
}