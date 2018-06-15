package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Alan Brown
 * @date 2018/5/24 19:51
 */
// 用来计算级别
// 0就是根级别
// 0.1就是第二级
// 0.1.3就是第三级
public class LevelUtil {

	// 分隔符
	private final static String SEPARATOR = ".";
	// 根级别
	public final static String ROOT = "0";

	public static String calculateLevel(String Level, int id){
		if(StringUtils.isBlank(Level)){
			return ROOT;
		}else{
			return StringUtils.join(Level, SEPARATOR, id);
		}
	}

}
