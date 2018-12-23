package com.zlyx.easysocket.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.zlyx.easysocket.annotations.Desc;

public class ClassUtil {
	
	@Desc(value = { "以Map形式存储类的属性" })
	private static ConcurrentMap<Class<?>, MapClass> classes = new ConcurrentHashMap<Class<?>,MapClass>();

	@Desc(value = { "存储类的实例对象" })
	private static ConcurrentMap<Class<?>,Object> objects = new ConcurrentHashMap<Class<?>,Object>();
	
	public static <K> K createEntity(Class<K> kClass, Map<String,?> params) {
		if(params==null) {
			return null;
		}
		MapClass mapClass = ClassUtil.getMapClass(kClass);
		K k = getEntity(kClass);
		Field field = null;
		Class<?> fieldType = null;
		Object value = null;
		try {
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			for(String fieldName : params.keySet()) {
				if(mapClass.haveField(fieldName)) {
					field = mapClass.getField(fieldName);
					fieldType = field.getType();
					field.setAccessible(true);
					value = params.get(fieldName);
					if(fieldType == Integer.class&&value.getClass()!=Integer.class) {
						value = Integer.parseInt((String) value);
					}else if(fieldType == Long.class&&value.getClass()!=Long.class) {
						value = Long.parseLong((String) value);
					}else if(fieldType == Date.class&&value.getClass()!=Date.class) {
						if(((String) value).trim().length()==10) {
							value = (String) value+" 00:00:00";
						}
						value = sdf.parse((String) value);
					}
					field.set(k, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return k;
	}

	@SuppressWarnings("unchecked")
	public static <K> K getEntity(Class<K> kClass) {
		try {
			K obj = kClass.getConstructor().newInstance();
			objects.put(kClass, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (K) objects.get(kClass);
	}
	
	public static MapClass getMapClass(Class<?> tClass){
		if(!classes.containsKey(tClass)) {
			registryMapClass(tClass);
		}
		return classes.get(tClass);
	}
	
	public static List<Field> getClassFields(Class<?> tClass){
		if(!classes.containsKey(tClass)) {
			registryMapClass(tClass);
		}
		return classes.get(tClass).getFields();
	}
	
	public static void registryMapClass(Class<?> tClass) {
		List<Field> fields = getAllFields(tClass);
		MapClass mapClass = new MapClass();
		try {
			for(Field field : fields) {
				mapClass.addField(field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		classes.put(tClass, mapClass);
	}
	
	public static List<Field> getAllFields(Class<?> tClass){
		return new ArrayList<Field>(Arrays.asList(tClass.getDeclaredFields()));
	}
	
	/**
	 *  获取类名
	 * @param tClass
	 * @return
	 */
	public static String getClassName(Class<?> tClass) {
		return tClass.getName();
	}
}
