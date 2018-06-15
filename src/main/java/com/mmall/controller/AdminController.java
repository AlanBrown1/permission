package com.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Alan Brown
 * @date 2018/5/25 23:08
 */

@Controller
@RequestMapping("/admin")
public class AdminController {

	// 管理员主界面admin.jsp
	@RequestMapping("/index.page")
	public ModelAndView index() {
		return new ModelAndView("admin");
	}

}
