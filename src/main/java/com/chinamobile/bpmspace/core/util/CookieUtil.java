package com.chinamobile.bpmspace.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.UserRepository;

public class CookieUtil {

	public static final String COOKIE_KEY = "hDMDWl0g";

	/**
	 * 判断cookie值是否存在，如果不存在返回null
	 */
	public static Cookie isCookieExist(String name, HttpServletRequest request) {
		Cookie[] cookieList = request.getCookies();
		// firefox浏览时如果cookie不存在返回null而不是空数组
		if (cookieList != null) {
			for (Cookie cookie : cookieList) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 移除Cookie
	 */
	public static void removeCookie(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookieList = request.getCookies();
		// firefox浏览时如果cookie不存在返回null而不是空数组
		if (cookieList != null) {
			for (Cookie cookie : cookieList) {
				if (cookie.getName().equals(name)) {
					cookie.setMaxAge(0);
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie);
				}
			}
		}
	}

	/**
	 * 验证cookie中的用户名和密码，如果验证成功返回用户名、否则返回null
	 */
	@SuppressWarnings("unused")
	public static String validateCookieLoginIdAndPassword(Cookie cookie) {
		UserRepository ur = new UserRepository();
		String cookieStr = cookie.getValue();
		String[] values = cookieStr.split(",");
		if (cookieStr != null) {
			try {
				String userId = ur.login(values[0], values[1]);
				return userId + "," + values[0];
			} catch (BasicException e) {
				return null;
			}
		}

		return null;
	}
}
