package com.aaron.stamp.frame.util;

import java.lang.reflect.Method;

/**
 * 反射工具类
 * 
 * @author linjinfa 331710168@qq.com
 * @date 2013-12-29 下午1:59:10
 */
public class InvokeUtil {

	/**
	 * 反射设置值
	 * 
	 * @param obj
	 * @param methodField
	 * @param value
	 */
	private static void setValue(Object obj, String methodName, Object value,
			Class<?>... parameterTypes) {
		try {
			Method method = obj.getClass()
					.getMethod(methodName, parameterTypes);
			method.invoke(obj, value);
		} catch (Exception e) {
			LogUtil.logDebug("(可忽略异常)反射设置值异常" + e.getMessage());
		}
	}

	/**
	 * 反射设置值 参数是Integer类型 (单个参数)
	 * 
	 * @param obj
	 * @param methodName
	 * @param value
	 */
	public static void setIntegerValue(Object obj, String methodName, int value) {
		setValue(obj, methodName, value, Integer.class);
	}

	/**
	 * 反射设置值 参数是String类型(单个参数)
	 * 
	 * @param obj
	 * @param methodName
	 * @param value
	 */
	public static void setStringValue(Object obj, String methodName,
			String value) {
		setValue(obj, methodName, value, String.class);
	}

	/**
	 * 反射获取值
	 * 
	 * @param obj
	 * @param methodName
	 * @return
	 */
	public static Object getValue(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			LogUtil.logDebug("(可忽略异常)反射获取值异常" + e.getMessage());
		}
		return null;
	}

}
