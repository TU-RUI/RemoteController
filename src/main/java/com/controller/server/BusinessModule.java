package com.controller.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.controller.model.ComputerModel;
import com.server.utils.PropertiesUtil;

public class BusinessModule {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BusinessModule.class);

	private IoConnector connector;
	private IoSession session;

	private static final String Address = PropertiesUtil.GetValueByKey("host");
	private static final int PORT = Integer.valueOf(PropertiesUtil
			.GetValueByKey("port1"));

	private static BusinessModule serverThread;
	public static ComputerModel model = null;

	private BusinessModule() {
		connector = new NioSocketConnector();
		connector.setHandler(new BusinessHandler());
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		TextLineCodecFactory textLineCoder = new TextLineCodecFactory(
				Charset.forName("UTF-8"));
		textLineCoder.setDecoderMaxLineLength(1024 * 1024);
		textLineCoder.setEncoderMaxLineLength(1024 * 1024);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(textLineCoder));
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(
				Address, PORT));
		connFuture.awaitUninterruptibly();
		connFuture.addListener(new IoFutureListener<ConnectFuture>() {
			public void operationComplete(ConnectFuture future) {
				if (future.isConnected()) {
					LOGGER.debug("...connected");
					session = future.getSession();
				} else {
					LOGGER.error("Not connected...exiting");
				}
			}
		});
	}

	public static BusinessModule getInstance() {
		if (serverThread == null) {
			serverThread = new BusinessModule();
		}
		return serverThread;
	}

	public boolean sendData(String message) {
		if (!session.isConnected()) {
			return false;
		}
		session.write(message);
		return true;
	}

	public void closeSession() {
		session.close(false);
	}

}
