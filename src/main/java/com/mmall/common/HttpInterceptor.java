package com.mmall.common;

import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Alan Brown
 * @date 2018/5/24 16:14
 */
// 拦截器，监听所有请求
// 在请求前、后进行一些操作
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

	private static final String START_TIME = "requestStartTime";

	// 在请求路径让controller处理之前，自动调用这个方法
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		// 返回true表示不拦截,false表示拦截，拦截了就无法进入controller
		return true;
	}

	// 当一个请求正常结束的时候，自动调用这个方法
	// postHandle是afterHandle的子情况，所以一般可以不写
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		// 请求结束后，清空RequestHolder中的用户和请求
		removeThreadLocalInfo();
	}

	// 不管一个请求正常结束还是异常结束，都会自动调用这个方法
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		// 请求结束后，清空RequestHolder中的用户和请求
		removeThreadLocalInfo();
	}

	private void removeThreadLocalInfo(){
		// 清空RequestHolder中的用户和请求
		RequestHolder.remove();
	}
}
