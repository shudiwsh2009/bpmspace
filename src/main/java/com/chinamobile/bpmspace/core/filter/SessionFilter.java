package com.chinamobile.bpmspace.core.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.chinamobile.bpmspace.core.util.CookieUtil;
import com.chinamobile.bpmspace.core.util.FileUtil;

/**
 * 登录过滤
 * 
 * @author lvcheng
 * @date 2014-3-26
 */
public class SessionFilter extends OncePerRequestFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (FileUtil.WEBAPP_ROOT.equals("")) {
			FileUtil.WEBAPP_ROOT = request.getSession().getServletContext()
					.getRealPath("/");
		}

		// 不过滤的uri
		String[] notFilter = new String[] { "login", "register",
				"forgot_password" };

		// 请求的uri
		String uri = request.getRequestURI();

		// uri中包含bpmspace时才进行过滤 并且不要过滤静态资源
		if (uri.indexOf("bpmspace") != -1 && uri.indexOf("assets") == -1) {
			// 是否过滤
			boolean doFilter = true;
			for (String s : notFilter) {
				if (uri.indexOf(s) != -1) {
					// 如果uri中包含不过滤的uri，则不进行过滤
					doFilter = false;
					break;
				}
			}
			if (doFilter) {
				// 执行过滤
				// 从session中获取登录者实体
				Object obj = request.getSession().getAttribute("userId");
				if (null == obj) {
					// 判断cookie中是否有
					Cookie cookie = CookieUtil.isCookieExist(
							CookieUtil.COOKIE_KEY, request);
					if (cookie != null) {
						String result = CookieUtil
								.validateCookieLoginIdAndPassword(cookie);
						if (result != null) {
							String[] values = result.split(",");
							request.getSession().setAttribute("userId",
									values[0]);
							request.getSession().setAttribute("username",
									values[1]);
							filterChain.doFilter(request, response);
						}
					} else {
						// 如果session中不存在登录者实体，则弹出框提示重新登录
						// 设置request和response的字符集，防止乱码
						request.setCharacterEncoding("UTF-8");
						response.setCharacterEncoding("UTF-8");
						PrintWriter out = response.getWriter();
						String loginPage = "login";
						StringBuilder builder = new StringBuilder();
						builder.append("<script type=\"text/javascript\">");
						builder.append("window.top.location.href='");
						builder.append(loginPage);
						builder.append("';");
						builder.append("</script>");
						out.print(builder.toString());
					}
				} else {
					// 如果session中存在登录者实体，则继续
					filterChain.doFilter(request, response);
				}
			} else {
				// 如果不执行过滤，则继续
				filterChain.doFilter(request, response);
			}
		} else {
			// 如果uri中不包含background，则继续
			filterChain.doFilter(request, response);
		}
	}

}
