package com.zlyx.easysocket.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

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
@Desc("NIO方式处理TCP消息工厂")
public class NioTcpMsgHandlerFactory extends AbstractMsgHandlerFactory {

	ServerSocketChannel serverSocketChannel;
	SocketChannel socketChannel;
	Selector selector;
	String sendText = null;

	public NioTcpMsgHandlerFactory(int port, int size, IMsgHandler handler) throws Exception {
		super(port, size, handler);
	    try{
		    this.serverSocketChannel = ServerSocketChannel.open();	      
		    this.serverSocketChannel.configureBlocking(false);	
		    this.serverSocketChannel.bind(new InetSocketAddress(this.PORT));
		    this.selector = Selector.open();
		    this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
	    }catch (IOException e){
	        e.printStackTrace();
	    }
	}

	public void run() {
		try {
			while(true){
				int readyChannels = this.selector.select();
			    if (readyChannels <= 0) {
	                continue;
	            }
				Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = (SelectionKey) iterator.next();
					handleKey(selectionKey);
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleKey(SelectionKey selectionKey) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(this.BLOCK);
		if (selectionKey.isAcceptable()) {
			this.serverSocketChannel = ((ServerSocketChannel) selectionKey.channel());
			this.socketChannel = this.serverSocketChannel.accept();
			this.socketChannel.configureBlocking(false);
			this.socketChannel.register(selector, SelectionKey.OP_READ);
		}
		if (selectionKey.isReadable()) {
			this.socketChannel = ((SocketChannel) selectionKey.channel());
			buffer.clear();
			int count = this.socketChannel.read(buffer);
			if (count > 0) {
				String receiveText = new String(buffer.array(), 0, count).trim();
				InetAddress address = socketChannel.socket().getInetAddress();
				this.sendText = getMsgHandler(address).doHandler(receiveText);
				this.socketChannel.register(selector, SelectionKey.OP_WRITE);
			}
		}
		if (selectionKey.isWritable()) {
			this.socketChannel = ((SocketChannel) selectionKey.channel());
			if(!StringUtils.isEmpty(this.sendText)) {
				this.socketChannel.write(ByteBuffer.wrap(this.sendText.getBytes()));
			}
			this.socketChannel.register(selector, SelectionKey.OP_READ);
		}
	}

	public void closeServer() throws Exception {
		this.socketChannel.close();
		this.serverSocketChannel.close();
	}
}
