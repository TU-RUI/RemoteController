package com.controller.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.controller.model.ComputerModel;
import com.server.message.ObjectMessage;
import com.server.net.ClientType;
import com.server.net.MsgType;
import com.server.utils.PropertiesUtil;

public class ScreenModule {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(ScreenModule.class);

	private IoConnector connector;
	private IoSession session;

	private static final String Address = PropertiesUtil.GetValueByKey("host");
	private static final int PORT = Integer.valueOf(PropertiesUtil
			.GetValueByKey("port2"));;

	private static ScreenModule screenModule;

	public static ScreenModule getInstance() {
		if (screenModule == null) {
			screenModule = new ScreenModule();
			
		}
		return screenModule;
	}

	public void init(){
		ObjectSerializationCodecFactory codecFactory = new ObjectSerializationCodecFactory();
		connector = new NioSocketConnector();
		connector.setHandler(new ScreenHandler());
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(codecFactory));
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(
				Address, PORT));
		connFuture.awaitUninterruptibly();
		connFuture.addListener(new IoFutureListener<ConnectFuture>() {
			public void operationComplete(ConnectFuture future) {
				if (future.isConnected()) {
					LOGGER.debug("...connected");
					session = future.getSession();
					ComputerModel model = ComputerModel.model;
					int i = 0;
					while (i++ < 3) {
						if (model != null && model.getTempid() != null) {
							String tempid = ComputerModel.model.getTempid();
							ObjectMessage authMessage = new ObjectMessage();
							authMessage.setMsgType(MsgType.AUTH);
							authMessage.setTempid(String.valueOf(tempid));
							authMessage.setMessage(ClientType.CONTROLLER);
							session.write(authMessage);
						}
					}
					if (model != null && model.getTempid() != null) {
						String tempid = ComputerModel.model.getTempid();
						ObjectMessage objectMessage = new ObjectMessage();
						objectMessage.setMsgType(MsgType.MONITOR);
						objectMessage.setTempid(String.valueOf(tempid));
						objectMessage.setMessage(ClientType.CONTROLLER);
						session.write(objectMessage);
					}
					LOGGER.info("连接上画面传输服务器");
				} else {
					LOGGER.error("Not connected...exiting");
				}
			}
		});
	}

	public boolean sendData(Object message) throws Exception {
		if (!session.isConnected()) {
			return false;
		}
		session.write(message);
		return true;
	}

	public void closeModule(){
		this.connector.dispose();
		this.connector = null;
	}
	
	public void closeSession() {
		
		
		session.close(false);
	}

	public static void main(String[] args) {
		ComputerModel model = new ComputerModel();
		model.setTempid("1111111111111111");
		ComputerModel.model = model;
		ScreenModule.getInstance();
	}

}
