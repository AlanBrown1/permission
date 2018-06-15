package com.mmall.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.mmall.common.ApplicationContextHelper;
import com.mmall.common.JsonData;
import com.mmall.common.RequestHolder;
import com.mmall.model.SysUser;
import com.mmall.service.SysCoreService;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Alan Brown
 * @date 2018/5/31 14:51
 */

// 权限过滤器
@Slf4j
public class AclControlFilter implements Filter {

	private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();
	// 如果无权限访问，则交给这个路径处理
	private static final String noAuthUrl = "/sys/user/noAuth.page";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化过滤白名单
		String exclusiveUrls = filterConfig.getInitParameter("exclusionUrls");
		List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusiveUrls);
		exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
		exclusionUrlSet.add(noAuthUrl);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = request.getServletPath();  // 当前请求路径
		Map requestMap = request.getParameterMap(); // 当前请求参数
		String requestParamString = JsonMapper.obj2String(requestMap);

		// 如果路径在白名单内，则不过滤
		if(exclusionUrlSet.contains(url)){
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		// 当前登录用户
		SysUser user = RequestHolder.getCurrentUser();
		if(user == null){
			log.info("有人访问：{}，但是没有登录。参数：{}", url, requestParamString);
			noAuth(request, response);
			return;
		}
		SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
		// 如果当前用户无权限访问该url
		if(!sysCoreService.hasUrlAcl(url)){
			log.info("用户{}试图访问{}，但是没权限。参数：",user.getUsername(), url, requestParamString);
			noAuth(request, response);
			return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}


	// 如果用户无权限
	private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getServletPath();
		// 判断是json请求还是页面请求
		if(url.endsWith(".json")){
			JsonData jsonData = JsonData.fail("无权限访问，如需访问，请联系管理员分配权限");
			response.setHeader("Content-Type", "application/json");
			response.getWriter().print(JsonMapper.obj2String(jsonData));
		}else if(url.endsWith(".page")){
			clientRedirect(noAuthUrl, response);
		}
	}

	// 跳转到某个链接
	private void clientRedirect(String url, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Type", "text/html");
		response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
				+ "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
				+ "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
	}
}
