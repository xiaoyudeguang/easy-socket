package com.zlyx.easysocket.core;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.springframework.util.StringUtils;

import com.zlyx.easysocket.annotations.Desc;
import com.zlyx.easysocket.core.supports.AbstractMsgHandlerFactory;
import com.zlyx.easysocket.interfaces.IMsgHandler;

/**
 * 
 * @author 赵光
 * @Description: 消息处理器创建者
 * @date 2018年11月14日
 */
@Desc("NIO方式处理UDP消息工厂")
public class NioUdpMsgHandlerFactory extends AbstractMsgHandlerFactory{
	
	DatagramChannel datagramChannel;
	DatagramSocket datagramSocket;
	Selector selector;

	public NioUdpMsgHandlerFactory(int port, int size, IMsgHandler handler) throws Exception {
		super(port, size, handler);
		datagramChannel = DatagramChannel.open();
		datagramChannel.configureBlocking(false);
		datagramSocket = datagramChannel.socket();
		datagramSocket.setReceiveBufferSize(this.BLOCK);
		datagramSocket.bind(new InetSocketAddress(this.PORT));
		selector = Selector.open();
		datagramChannel.register(selector, SelectionKey.OP_READ);
		this.start();
	}

	@Override 
	public void run() {
		try {
			while (true) {
				selector.select();
				Iterator<SelectionKey> iterator = selector.selectedKeys()
						.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					handleKey(selectionKey);
					iterator.remove();
				}
			}
			
		} catch (Exception e) {
			
		} 
	}

	private void handleKey(SelectionKey selectionKey) throws Exception {
		ByteBuffer byteBuffer = ByteBuffer.allocate (100) ;
		if (selectionKey.isReadable()) {
			datagramChannel = (DatagramChannel) selectionKey.channel();
			byteBuffer.clear();
			SocketAddress socketAddress = datagramChannel.receive(byteBuffer);
			byteBuffer.flip();
			CharBuffer charBuffer = Charset.defaultCharset().decode(byteBuffer);
			if(!StringUtils.isEmpty(charBuffer.toString())) {
				String rmsg = handler.doHandler(charBuffer.toString());
				if(!StringUtils.isEmpty(rmsg)) {
					ByteBuffer buffer = Charset.defaultCharset().encode(rmsg);
					datagramChannel.send(buffer, socketAddress);
				}
			}
		}
	}

	@Override
	public void closeServer() throws Exception {
		selector.close();
		datagramChannel.close(); 
		datagramSocket.close();
	}
}
