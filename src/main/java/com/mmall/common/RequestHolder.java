package com.mmall.common;

import com.mmall.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alan Brown
 * @date 2018/5/26 01:31
 */

public class RequestHolder {

	private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

	private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

	public static void add(SysUser sysUser){
		userHolder.set(sysUser);
	}

	public static  void add(HttpServletRequest request){
		requestHolder.set(request);
	}

	public static SysUser getCurrentUser(){
		return userHolder.get();
	}

	public static HttpServletRequest getCurrentRequest(){
		return requestHolder.get();
	}

	// 一个进程结束的时候，从线程中移除用户和请求
	// 不然会越积越多，系统就崩了
	public static void remove(){
		userHolder.remove();
		requestHolder.remove();
	}


}
