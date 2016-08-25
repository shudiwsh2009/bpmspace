package com.chinamobile.bpmspace.core.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.UserRepository;
import com.chinamobile.bpmspace.core.util.CookieUtil;

@Controller
@RequestMapping("user")
public class UserController {

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void register(@RequestParam("username") String _username,
			@RequestParam("password") String _password,
			HttpServletResponse response, ModelMap model) throws IOException {
		UserRepository ur = new UserRepository();
		try {
			ur.register(_username, _password);
			response.sendRedirect("/bpmspace/login");
		} catch (BasicException e) {
			model.addAttribute("message", e.getInfo());
			response.sendRedirect("/bpmspace/register");
		}
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public void login(@RequestParam("username") String _username,
			@RequestParam("password") String _password,
			HttpServletResponse response, HttpServletRequest request,
			HttpSession session, ModelMap model) throws IOException {
		UserRepository ur = new UserRepository();
		try {
			String remember_me = request.getParameter("remember_me");
			String userId = ur.login(_username, _password);
			if (remember_me != null && remember_me.endsWith("true")) {
				String cookieValue = _username + "," + _password;
				Cookie cookie = new Cookie(CookieUtil.COOKIE_KEY, cookieValue);
				// cookie.setDomain("localhost");
				cookie.setMaxAge(2721600); // 正式的发布中最好将这个值算出来再写到程序中
				cookie.setPath(request.getContextPath());
				response.addCookie(cookie);
			}
			session.setAttribute("userId", userId);
			session.setAttribute("username", _username);
			response.sendRedirect("/bpmspace/modelDB");
		} catch (BasicException e) {
			model.addAttribute("message", e.getInfo());
			response.sendRedirect("/bpmspace/login");
		}
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public void logout(HttpServletResponse response,
			HttpServletRequest request, HttpSession session, ModelMap model)
			throws IOException {
		CookieUtil.removeCookie(CookieUtil.COOKIE_KEY, request, response);
		request.getSession().removeAttribute("userId");
		request.getSession().removeAttribute("username");
		response.sendRedirect("/bpmspace/login");
		return;
	}

}
