package com.zlyx.easysocket.core;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import org.springframework.util.StringUtils;

import com.zlyx.easysocket.annotations.Desc;
import com.zlyx.easysocket.core.supports.AbstractMsgHandlerFactory;
import com.zlyx.easysocket.interfaces.IMsgHandler;

/**
 * 
 * @author 赵光
 * @Description: TCP消息处理器工厂
 * @date 2018年11月14日
 */
@Desc("AIO方式处理TCP消息工厂")
public class AioTcpMsgHandlerFactory extends AbstractMsgHandlerFactory implements MessageProcessor<String>{
	
	private AioQuickServer<String> server;
	
	public AioTcpMsgHandlerFactory(int port, int size, IMsgHandler handler) throws Exception {
		super(port,size,handler);
		server = new AioQuickServer<String>(this.PORT, new PrivateProtocol(), this);
		System.out.println("AIO开始服务"+server.hashCode());
		server.start();	
	}
 
	@Override  
	public void closeServer() throws Exception {
		System.out.println("AIO停止服务"+server.hashCode());
		server.shutdown(); 
	}   
	
	@Override
	public void process(AioSession<String> session, String msg) {
		try {
			InetAddress address = session.getRemoteAddress().getAddress();
			String res = getMsgHandler(address).doHandler(msg);
			if(!StringUtils.isEmpty(res)) {
				session.write(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stateEvent(AioSession<String> session, StateMachineEnum stateMachineEnum, Throwable throwable) {
		
	}
	
	public class PrivateProtocol implements Protocol<String> {

	    private static final int INT_LENGTH = 4;

	    @Override
	    public String decode(ByteBuffer data, AioSession<String> session, boolean eof) {
	        if (data.remaining() < INT_LENGTH)
	            return null;
	        return Charset.forName("GB2312").decode(data).toString();
	    }

	    @Override 
	    public ByteBuffer encode(String s, AioSession<String> session) {
	        return ByteBuffer.wrap(s.getBytes());
	    } 
	}
}
