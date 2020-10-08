package spring.shuyuan.judd.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public class JSONUtil {

	private static final SerializeConfig config;

	static {
		config = new SerializeConfig();
		config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
		config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
	}

	private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};

	public static String toJSONString(Object obj) {
		return JSON.toJSONStringWithDateFormat(obj,
                "yyyy-MM-dd HH:mm:ss",
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect
                );
	}
	public static Map<String,Object> jsonToMap(String jsonStr){

		Map<String,Object> map = JSON.parseObject(jsonStr, Map.class);

		return map==null?new HashMap():map;

	}

	public static String convertObjectToJSON(Object object) {
		return JSON.toJSONString(object, config, features);
	}

	public static String toJSONNoFeatures(Object object) {
		return JSON.toJSONString(object, config);
	}



	public static Object toBean(String text) {
		return JSON.parse(text);
	}

	public static <T> T toBean(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	/**
	 *  转换为数组
	 * @param text
	 */
	public static <T> Object[] toArray(String text) {
		return toArray(text, null);
	}

	/**
	 *  转换为数组
	 * @param text
	 * @param clazz
	 */
	public static <T> Object[] toArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz).toArray();
	}

	/**
	 * 转换为List
	 * @param text
	 * @param clazz
	 */
	public static <T> List<T> toList(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * 将string转化为序列化的json字符串
	 * @param text
	 */
	public static Object textToJson(String text) {
		Object objectJson  = JSON.parse(text);
		return objectJson;
	}

	/**
	 * json字符串转化为map
	 * @param s
	 */
	public static <K, V> Map<K, V>  stringToCollect(String s) {
		Map<K, V> m = (Map<K, V>) JSONObject.parseObject(s);
		return m;
	}

	/**
	 * 转换JSON字符串为对象
	 * @param jsonData
	 * @param clazz
	 */
	public static Object convertJsonToObject(String jsonData, Class<?> clazz) {
		return JSONObject.parseObject(jsonData, clazz);
	}

	/**
	 * 将map转化为string
	 * @param m
	 */
	public static <K, V> String collectToString(Map<K, V> m) {
		String s = JSONObject.toJSONString(m);
		return s;
	}

	/**
	 * 将对象转化为Map
	 * @param obj
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if(obj == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.compareToIgnoreCase("class") == 0) {
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter!=null ? getter.invoke(obj) : null;
			map.put(key, value);
		}

		return map;
	}
	/**
	 *
	 * map转换json.
	 * <br>详细说明
	 * @param map 集合
	 */
	public static String mapToJson(Map<String, String> map) {
		Set<String> keys = map.keySet();
		String key = "";
		String value = "";
		StringBuffer jsonBuffer = new StringBuffer();
		jsonBuffer.append("{");
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			key = (String) it.next();
			value = map.get(key);
			jsonBuffer.append(key + ":" +"\""+ value+"\"");
			if (it.hasNext()) {
				jsonBuffer.append(",");
			}
		}
		jsonBuffer.append("}");
		return jsonBuffer.toString();
	}


}
