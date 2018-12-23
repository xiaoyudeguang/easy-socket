package com.zlyx.easysocket.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import com.zlyx.easysocket.annotations.EasyBean;

/**
 * @Auth 赵光
 * @Desc 线程池自动化管理工具
 * @2018年12月12日 下午2:19:03
 */
@EasyBean(todo = "线程池自动化管理工具")
public class ThreadManager{

	private static ConcurrentMap<String,Thread> threads = new ConcurrentHashMap<String,Thread>();
	
	private static ExecutorService threadPool =  Executors.newCachedThreadPool();
	
	/**
	  *  分配一个线程执行任务
	 * @param thread
	 * @return
	 */
	public static String execute(Thread thread) {
		return execute(thread, thread.getName());
	}
	
	/**
	  * 分配一个线程执行任务
	 * @param name
	 * @param command
	 * @return
	 */
	public static String execute(Thread thread, String name) {
		if(isActive()) {
			threads.put(name, thread);
			threadPool.execute(thread);
		}
		return name;
	}
	
	/**
	 *    根据名称获取线程
	 * @param name
	 * @return
	 */
	public static Thread getThread(String name) {
		return threads.get(name);
	}
	
	/**
	  *   判断某个线程是否存活
	 * @param name
	 * @return
	 */
	public static boolean isAlive(String name) {
		clearDead();
		return threads.containsKey(name);
	}
	     
	/**
	  *  判断线程池是否可用
	 * @return
	 */
	private static boolean isActive() {
		return threadPool != null && !threadPool.isShutdown();
	}
	
	/**
	 * 获取当前存活的所有线程的名称
	 * @return
	 */
	public static Set<String> getNames(){
		clearDead();
		return threads.keySet();
	}
  
	/**
	 * 获取所有存活中的线程列表
	 * @return
	 */
	public static List<Thread> getThreads(){
		return new ArrayList<Thread>(threads.values());
	}
	
	/**
	  *  清除已经死亡的线程
	 */
	public static void clearDead() {
		Set<String> names = threads.keySet();
		for(String name : names) {
			if(!isAlive(name)) {
				threads.remove(name);
			}
		}
	}
	
	@EasyBean(todo = "系统更新时重新启动线程池")
	public static class ThreadRefresher implements ApplicationListener<ContextRefreshedEvent> {
		@Override 
		public void onApplicationEvent(ContextRefreshedEvent event) {
			if(isActive()) { 
				threadPool.shutdown();
			} 
			threadPool =  Executors.newCachedThreadPool();
		} 
	}
	
}
