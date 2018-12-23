package com.zlyx.easysocket.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlyx.easysocket.annotations.Desc;

/**
 * 
 * @author zhaog
 * @Description: 消息处理器接口
 * @date 2018年11月14日
 */
@Desc("消息处理器接口")
public interface IMsgHandler {
	
	public Logger logger = LoggerFactory.getLogger(IMsgHandler.class);

	/**
	   *    消息处理方法
	 * @param data   消息体
	 * @return
	 * @throws Exception 
	 */
	public String doHandler(String data) throws Exception;
	
}
