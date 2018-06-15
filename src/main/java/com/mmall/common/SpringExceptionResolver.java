package com.mmall.common;

import com.mmall.exception.ParamException;
import com.mmall.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alan Brown
 * @date 2018/5/24 13:59
 */

// 全局的异常处理
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String url = request.getRequestURL().toString();    // 拿到请求URL
		ModelAndView mv;    // 异常时，被返回的结果
		String defaultMsg = "System Error"; // 默认消息

		// 下面对数据请求和页面请求两种情况分别处理
		// 本项目约定，数据请求路径以.json结尾，页面请求以.page结尾

		// 如果是数据请求时产生了异常
		if(url.endsWith(".json")){
			// 如果抛出的是我们自己定义的异常
			if(ex instanceof PermissionException || ex instanceof ParamException){
				JsonData result = JsonData.fail(ex.getMessage());
				mv = new ModelAndView("jsonView", result.toMap());
			// 如果抛出的不是我们自己定义的异常
			}else{
				log.error("unknown json exception, url:" + url, ex);
				JsonData result = JsonData.fail(defaultMsg);
				mv = new ModelAndView("jsonView", result.toMap());
			}
		// 如果是页面请求时产生了异常
		}else if(url.endsWith(".page")){
			log.error("unknown page exception, url:" + url, ex);
			JsonData result = JsonData.fail(defaultMsg);
			mv = new ModelAndView("exception", result.toMap());
		// 如果是其它请求时产生了异常
		}else {
			log.error("unknown exception, url:" + url, ex);
			JsonData result = JsonData.fail(defaultMsg);
			mv = new ModelAndView("jsonView", result.toMap());
		}
		return mv;
	}


}
