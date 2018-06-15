package com.mmall.controller;

import com.mmall.model.SysUser;
import com.mmall.service.SysUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Alan Brown
 * @date 2018/5/25 17:49
 */
// UserController和SysUserController的区别是
// 前者是用户自己进行相关操作的方法
// 后者是非用户自己进行的相关操作的方法
@Controller
public class UserController {

	@Resource
	private SysUserService sysUserService;

	// 用户登录
	@RequestMapping("/login.page")
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		SysUser sysUser = sysUserService.findByKeyword(username);
		String errorMsg = "";
		String ret = request.getParameter("ret");  // 跳转参数

		if (StringUtils.isBlank(username)) {
			errorMsg = "用户名不可以为空";
		} else if (StringUtils.isBlank(password)) {
			errorMsg = "密码不可以为空";
		} else if (sysUser == null) {
			errorMsg = "用户不存在";
		} else if (!sysUser.getPassword().equals(MD5Util.encrypt(password + sysUser.getSalt()))) {
			errorMsg = "密码错误";
		} else if (sysUser.getStatus() != 1) {
			errorMsg = "用户已被冻结，请联系管理员";
		} else {
			// 登录成功
			request.getSession().setAttribute("user", sysUser);
			if (StringUtils.isNotBlank(ret)) { // 如果跳转参数不为空，则跳转
				response.sendRedirect(ret);
			} else { // 否则交给/admin/index.page这个映射去处理
				response.sendRedirect("/admin/index.page");
				return;
			}
		}

		request.setAttribute("error", errorMsg);
		request.setAttribute("username", username);
		request.setAttribute("password", password);
		if (StringUtils.isNotBlank(ret)) {
			request.setAttribute("ret", ret);
		}
		String path = "signin.jsp";
		request.getRequestDispatcher(path).forward(request, response);
		return;
	}

	// 用户退出
	@RequestMapping("/logout.page")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate(); // 让session无效
		String path = "signin.jsp";
		response.sendRedirect(path); // 跳转到登录界面
	}

}
