package com.zlyx.easysocket.defaults;

import com.zlyx.easysocket.annotations.Desc;
import com.zlyx.easysocket.interfaces.IMsgHandler;

/**
 * 
 * @author zhaog
 * @Description: 默认udp消息处理器
 * @date 2018年11月14日
 */
@Desc("默认消息处理工厂")
public class DefaultMsgHandler implements IMsgHandler{

	@Override
	public String doHandler(String data) {
		logger.info("接收数据:"+data);
		return null;
	}

}
