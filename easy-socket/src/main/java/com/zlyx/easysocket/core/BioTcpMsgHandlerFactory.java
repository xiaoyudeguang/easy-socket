package com.zlyx.easysocket.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.util.StringUtils;

import com.zlyx.easysocket.annotations.Desc;
import com.zlyx.easysocket.core.supports.AbstractMsgHandlerFactory;
import com.zlyx.easysocket.interfaces.IMsgHandler;
import com.zlyx.easysocket.utils.ThreadManager;

/**
 * 
 * @author 赵光
 * @Description: TCP消息处理器工厂
 * @date 2018年11月14日
 */
@Desc("BIO方式处理TCP消息工厂")
public class BioTcpMsgHandlerFactory extends AbstractMsgHandlerFactory{
	
	ServerSocket ss;
	Socket socket;
	
	public BioTcpMsgHandlerFactory(int port, int size, IMsgHandler handler) throws Exception {
		super(port,size,handler);
		ss = new ServerSocket(this.PORT);
	}

	@Override
	public void run() {
		while (true) {	
			try {
				socket = ss.accept();
				ThreadManager.execute(new Handler(socket));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
 
	@Override
	public void closeServer() throws Exception {
		ss.close(); 
	}    
	
	public class Handler extends Thread{
		
		protected Socket socket; 
		protected BufferedReader reader;
		protected PrintWriter writer;
		private IMsgHandler handler;

		public Handler(Socket socket) {
			this.handler = getMsgHandler(socket.getInetAddress());
			this.socket = socket;
			try {
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			String rmsg = null; 
			try {
				while(true){ 
					byte[] data = new byte[BLOCK];
					int size = socket.getInputStream().read(data);
					if(size>0){
						rmsg = handler.doHandler(new String(data));
						if(!StringUtils.isEmpty(rmsg)) {
							socket.getOutputStream().write(rmsg.getBytes());
						}
					}  
				}
			} catch (Exception e) {
				closeSocket();
				e.printStackTrace();
			}
		}

		public void closeSocket() {
			try {
				reader.close();
				writer.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
