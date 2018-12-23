package com.zlyx.easysocket.core.supports;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlyx.easysocket.annotations.Desc;
import com.zlyx.easysocket.interfaces.IMsgHandler;
import com.zlyx.easysocket.utils.ClassUtil;

/** 
 * 
 * @author 赵光
 * @Description: 消息处理工厂抽象类
 * @date 2018年11月14日
 */
@Desc("消息处理工厂抽象类")
public abstract class AbstractMsgHandlerFactory extends Thread{
	
	private ConcurrentMap<InetAddress, IMsgHandler> handlers = new ConcurrentHashMap<InetAddress,IMsgHandler>();
	
	protected Logger logger = LoggerFactory.getLogger(IMsgHandler.class);

	protected IMsgHandler handler;
	protected int BLOCK = 1024;
    protected int PORT;

	public AbstractMsgHandlerFactory(int port, int size, IMsgHandler handler){
		this.handler = handler;
		this.BLOCK = BLOCK*size;
		this.PORT = port;
	}
	
	public IMsgHandler getMsgHandler(InetAddress address) {
		IMsgHandler handler = handlers.get(address);
		if(handler==null) {
			handler = ClassUtil.getEntity(this.handler.getClass());
			handlers.put(address, handler);
		}
		return handler;
	}
	
	/**
	 *  关闭服务
	 */
	public abstract void closeServer() throws Exception;
	
}
