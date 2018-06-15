package com.mmall.filter;


import com.mmall.common.RequestHolder;
import com.mmall.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Alan Brown
 * @date 2018/5/26 01:42
 */
// 登录拦截器
// 校验用户是否登录了，没登录的话就跳转到登录页面
@Slf4j
public class LoginFilter implements Filter {

	// init里面可以做一些过滤器的初始化操作
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletRequest;        // 拿到请求
		HttpServletResponse resp = (HttpServletResponse)servletResponse;    // 拿到响应

		SysUser sysUser = (SysUser)req.getSession().getAttribute("user"); // 拿到请求中的user属性，并在数据库中取出这个用户
		// 如果为null，说明用户没登录，跳转到登录页面
		if(sysUser == null){
			resp.sendRedirect("/signin.jsp");
			return;
		}
		// 如果请求中存在用户，就把用户和请求放到RequestHolder里面
		RequestHolder.add(sysUser);
		RequestHolder.add(req);
		// 然后继续执行其它过滤器操作
		filterChain.doFilter(servletRequest, servletResponse);
		return;
	}

	@Override
	public void destroy() {

	}
}
