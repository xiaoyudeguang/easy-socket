package com.zlyx.easysocket.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapClass{
		
	public  Map<String,Field> clazz = new HashMap<String,Field>();
	
	public void addField(Field field) {
		this.clazz.put(field.getName(), field);
	}
	
	public Set<String> getFieldNames() {
		return this.clazz.keySet();
	}
	
	public Field getField(String fieldName) {	
		return this.clazz.get(fieldName);
	}
	
	public boolean haveField(String fieldName) {
		return this.clazz.containsKey(fieldName);
	}
	
	public String toString() {
		return clazz.toString();
	}

	public List<Field> getFields() {		
		return new ArrayList<Field>(clazz.values());
	}
}