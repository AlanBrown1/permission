package com.mmall.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alan Brown
 * @date 2018/5/30 14:59
 */

public class StringUtil {

	// 1,2,3,4,,5,6
	public static List<Integer>  splitToListInt(String str){
		// 用英文逗号分割，然后去掉空的，然后转为List
		List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
		return strList.stream().map(Integer::parseInt).collect(Collectors.toList());
	}

}
