package com.zlyx.easysocket.abstracts;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zlyx.easysocket.annotations.MsgHandler;
import com.zlyx.easysocket.core.AioTcpMsgHandlerFactory;
import com.zlyx.easysocket.core.BioTcpMsgHandlerFactory;
import com.zlyx.easysocket.core.BioUdpMsgHandlerFactory;
import com.zlyx.easysocket.core.NioTcpMsgHandlerFactory;
import com.zlyx.easysocket.core.NioUdpMsgHandlerFactory;
import com.zlyx.easysocket.core.supports.AbstractMsgHandlerFactory;
import com.zlyx.easysocket.defaults.DefaultMsgHandler;
import com.zlyx.easysocket.interfaces.IMsgHandler;
import com.zlyx.easysocket.utils.ThreadManager;

/**
 * @Auth 赵光
 * @Desc 
 * @2018年12月13日 下午12:32:12
 */
public abstract class AbstractMsgHandlerFactoryManager extends AbstractMsgHandlerFactoryRegistry implements ApplicationContextAware, InitializingBean{

	protected ApplicationContext applicationContext;
	
	protected String eventName = "ClassPathChangedEvent";

	protected void start(Map<String, Object> receivers) throws Exception {
		Object receiverBean = null;
		MsgHandler msgHandler = null; 
		for(String beanName : receivers.keySet()){
			receiverBean = receivers.get(beanName);
			msgHandler = receiverBean.getClass().getAnnotation(MsgHandler.class);
			startListener(receiverBean, msgHandler, beanName);
		}
	}

	private void startListener(Object receiverBean, MsgHandler msgHandler,
			String beanName) throws Exception {
		if(msgHandler.isOpen()){ 
			IMsgHandler handler = null;
			if(receiverBean instanceof IMsgHandler){
				handler = (IMsgHandler)receiverBean;
			}else{
				handler = new DefaultMsgHandler();
			}
			AbstractMsgHandlerFactory factory =  getFactory(msgHandler,handler);
			logger.info("Easy-socket have started an "+msgHandler.level()+" listener on port "+msgHandler.port()+" for "+handler.getClass());
			registryFactory(beanName, factory);
			ThreadManager.execute(factory);
		}
	}

	private AbstractMsgHandlerFactory getFactory(MsgHandler msgHandler, IMsgHandler handler) throws Exception {
		AbstractMsgHandlerFactory factory = null;
		int port = msgHandler.port();
		int size = msgHandler.size();
		switch(msgHandler.level()) {
		  case BTM:{
			  factory = new BioTcpMsgHandlerFactory(port, size, handler);
			  break;
		  }
		  case NTM: {
			  factory = new NioTcpMsgHandlerFactory(port, size, handler);
			  break;
		  }
		  case ATM: {
			  factory = new AioTcpMsgHandlerFactory(port, size, handler);
			  break;
		  }
		  case BUM: {
			  factory = new BioUdpMsgHandlerFactory(port, size, handler);
			  break;
		  }
		  case NUM: {
			  factory = new NioUdpMsgHandlerFactory(port, size, handler);
			  break;
		  }
		}
		return factory; 
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	        this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ThreadManager.execute(new Thread() {
			@Override 
			public void run() {
				init(); 
			}
		},"消息处理中心");  
	} 
	
	protected abstract void init() ; 
	 
	protected void destory() throws Exception {
		AbstractMsgHandlerFactory  factory = null; 
		try { 
			for(String beanName : getFactoryNames()) {					
				factory = getFactory(beanName);
				if(factory!=null) { 
					factory.closeServer();  
				}       
				deleFactory(beanName);
			}     
		}catch(Exception e) {    
			
		}
		Thread.sleep(15000); 
	}
}
