package com.mmall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author Alan Brown
 * @date 2018/5/24 14:46
 */
// 用于校验bean
public class BeanValidator {

	// 获取一个ValidatorFactory实例
	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	// 单个类的校验
	private static <T> Map<String, String> validate(T t, Class... groups){
		Validator validator = validatorFactory.getValidator(); // 使用工厂实例获取一个Validator
		Set validateResult = validator.validate(t, groups); // 校验 t 是否 属于groups之一
		// 如果空的话，说明t属于groups之一，对t的校验通过
		if(validateResult.isEmpty()){
			return Collections.emptyMap();
		// 否则的话，t没校验通过
		}else{
			// key是有问题的字段，value是原因
			LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
			for (Object aValidateResult : validateResult) {
				ConstraintViolation violation = (ConstraintViolation) aValidateResult;
				errors.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			return errors;
		}
	}

	// 校验一个集合内的实例
	private static Map<String, String> validateList(Collection<?> collection) {
		Preconditions.checkNotNull(collection);
		Iterator iterator = collection.iterator();
		Map<String, String> errors;
		do {
			if (!iterator.hasNext()) {
				return Collections.emptyMap();
			}
			Object object = iterator.next();
			errors = validate(object);
		} while (errors.isEmpty()); // 如果校验没问题就继续校验下一个元素
		return errors;
	}

	// 使用统一的校验入口
	// 如果校验没通过，就会返回一个map，里面是错误字段->错误原因
	public static Map<String, String> validateObject(Object first, Object... objects) {
		if (objects != null && objects.length > 0) {
			return validateList(Lists.asList(first, objects));
		} else {
			return validate(first);
		}
	}

	// 校验参数是否符合要求
	public static void check(Object param) throws ParamException {
		Map<String, String> map = BeanValidator.validateObject(param);
		if (MapUtils.isNotEmpty(map)) {
			throw new ParamException(map.toString());
		}
	}

}
