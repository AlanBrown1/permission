package com.mmall.util;

import org.codehaus.jackson.map.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;
/**
 * @author Alan Brown
 * @date 2018/5/24 15:43
 */

// 用于把一个类转为json字符串，或者把json串转为类
@Slf4j
public class JsonMapper {
	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		// objectMapper的属性配置
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
	}

	// 对象转为json串
	public static <T> String obj2String(T src) {
		if (src == null) {
			return null;
		}
		try {
			return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
		} catch (Exception e) {
			log.warn("parse object to String exception, error:{}", e);
			return null;
		}
	}

	// json串转对象
	public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
		if (src == null || typeReference == null) {
			return null;
		}
		try {
			return (T) (typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
		} catch (Exception e) {
			log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, typeReference.getType(), e);
			return null;
		}
	}
}
