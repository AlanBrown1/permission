package com.mmall.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan Brown
 * @date 2018/5/24 13:49
 */
// 如果controller需要返回数据
// 就使用这个JsonData，而不使用String，这样能传递的信息更多
@Getter
@Setter
public class JsonData {

	private boolean ret;        // 处理成功还是失败
	private String msg;         // 原因
	private Object data;      // 成功时返回的数据

	private JsonData(boolean ret){
		this.ret = ret;
	}

	// 成功
	public static JsonData success(){
		return new JsonData(true);
	}

	// 成功，含数据
	public static JsonData success(Object object){
		JsonData jsonData = new JsonData(true);
		jsonData.data = object;
		return jsonData;
	}

	// 成功，含消息和数据
	public static JsonData success(String msg, Object object){
		JsonData jsonData = new JsonData(true);
		jsonData.msg = msg;
		jsonData.data = object;
		return jsonData;
	}


	// 失败，含消息
	public static JsonData fail(String msg){
		JsonData jsonData = new JsonData(false);
		jsonData.msg = msg;
		return jsonData;
	}

	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("ret", ret);
		result.put("msg", msg);
		result.put("data", data);
		return result;
	}
}
