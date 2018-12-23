package com.zlyx.easysocket.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
@Desc("BIO方式处理UDP消息工厂")
public class BioUdpMsgHandlerFactory extends AbstractMsgHandlerFactory{
	
	DatagramSocket socket = null;
	DatagramPacket packet = null;
	byte[] data = null;
	
	public BioUdpMsgHandlerFactory(int port, int size, IMsgHandler handler) throws Exception {
		super(port,size,handler);
		socket = new DatagramSocket(this.PORT);
		data = new byte[BLOCK];	
		packet = new DatagramPacket(data, data.length);
	}

	@Override
	public void run() {
		DatagramPacket client = null;
		String msg = null;
		String res = null;
		try {
			while(true) {
		        socket.receive(packet);
		        msg = new String(data, 0, packet.getLength());
		        res = this.handler.doHandler(msg);
		        if(!StringUtils.isEmpty(res)) {
		    		byte[] output = res.getBytes();
		    		client = new DatagramPacket(output, output.length, packet.getAddress(), packet.getPort());
		    		socket.send(client);
		        } 
			}
		} catch (Exception e) { 
			
		}
	}
 
	@Override
	public void closeServer() throws Exception {
		socket.close(); 
	}    
}
