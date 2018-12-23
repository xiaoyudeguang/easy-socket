package com.zlyx.easysocket.abstracts;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlyx.easysocket.core.supports.AbstractMsgHandlerFactory;

/**
 * @Auth 赵光
 * @Desc 
 * @2018年12月13日 下午12:40:34
 */
public abstract class AbstractMsgHandlerFactoryRegistry{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private static ConcurrentMap<String, AbstractMsgHandlerFactory> factorys = new ConcurrentHashMap<String,AbstractMsgHandlerFactory>();
	
	/**
	 * 注册监听程序
	 * @param name
	 * @param factory
	 */
	public static void registryFactory(String name, AbstractMsgHandlerFactory factory) {
		factorys.put(name, factory);
	}
	
	/**
	 * 获取监听程序
	 * @param beanName
	 * @return
	 */
	public static AbstractMsgHandlerFactory getFactory(String beanName) {		
		return factorys.get(beanName);
	}
	
	/**
	 * 删除监听程序
	 * @param beanName
	 */
	public static void deleFactory(String beanName) {		
		factorys.remove(beanName);
	}
	
	/**
	 * 获取所有监听程序名称
	 * @return
	 */
	public static Set<String> getFactoryNames() {
		return factorys.keySet();
	}
	
	/**
	 * 获取监听程序
	 * @param beanName
	 * @return
	 */
	public static ConcurrentMap<String, AbstractMsgHandlerFactory> getFactorys() {	
		return factorys; 
	}
}
