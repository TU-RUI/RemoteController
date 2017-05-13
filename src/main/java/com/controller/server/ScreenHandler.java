package com.controller.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.controller.model.ImageManager;
import com.controller.window.ScreenWindow;
import com.server.message.ObjectMessage;
import com.server.net.MsgType;

public class ScreenHandler extends IoHandlerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(ScreenHandler.class);
	private static boolean flag = true;

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(session, message);
		
		ObjectMessage objectMessage = (ObjectMessage) message;
		if(MsgType.SCREEN.equals(objectMessage.getMsgType())){		
			ImageManager.getQueue().put(objectMessage.getImage());
			if(flag){
				ScreenWindow.getInstance().updataThread.start();;
				flag = false;
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
		logger.info("发送消息:"+message);
	}
	
}
