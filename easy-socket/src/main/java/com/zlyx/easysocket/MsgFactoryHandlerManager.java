package com.zlyx.easysocket;

import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.zlyx.easysocket.abstracts.AbstractMsgHandlerFactoryManager;
import com.zlyx.easysocket.annotations.MsgHandler;

/**
 * @Auth 赵光
 * @Describle
 * @2018年12月22日 下午4:41:07
 */
public class MsgFactoryHandlerManager extends AbstractMsgHandlerFactoryManager implements ApplicationListener<ApplicationEvent>{
	
	public void init() {
		try { 
			logger.info("Socket连接管理平台启动成功!");
			Map<String, Object> receivers = applicationContext.getBeansWithAnnotation(MsgHandler.class);
			if (receivers == null || receivers.size() < 1) {
				return; 
			} 
			start(receivers);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(this.eventName.equals(event.getClass().getSimpleName())) {
			new Thread() {
				@Override 
				public void run() {
					try {
						destory();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}.start(); 
		} 
	}
}
