package com.controller.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.controller.model.ComputerModel;
import com.controller.window.CMDWindow;
import com.controller.window.LoginWindow;
import com.server.net.MsgType;

public class BusinessHandler extends IoHandlerAdapter {

	private final static Logger logger = LoggerFactory
			.getLogger(BusinessHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof String) {
			String msg = message.toString();
			logger.info(msg);

			JSONObject json = JSON.parseObject(msg);
			if (!json.containsKey("msgType")) {
				return;
			}
			String type = json.getString("msgType");
			switch (type) {
			// 登陆结果
			case MsgType.LOGIN_RESULT:
				String login_result = json.getString(MsgType.LOGIN_RESULT);
				if (login_result.equals(MsgType.LOGIN_SUCCESS)) {
					// 登陆成功
					if (json.containsKey("controlled")) {
						JSONObject json1 = json.getJSONObject("controlled");
						ComputerModel model = new ComputerModel();
						model.setDevip(json1.getString("devip"));
						model.setScreenwidth(json1.getIntValue("width"));
						model.setScreenheight(json1.getIntValue("height"));
						model.setOs(json1.getString("os"));
						model.setTempid(json.getString("tempid"));
						ComputerModel.model = model;
					}
					LoginWindow.jump();
				} else if (login_result.equals(MsgType.WRONGPASSWORD)) {
					// 密码错误
					LoginWindow.informationDialog(null, "密码错误");
				} else if (login_result.equals(MsgType.NOSUCHUSER)) {
					// 没有该用户
					LoginWindow.informationDialog(null, "没有该用户");
				}
				break;
			case MsgType.REGIST_RESULT:
				String regist_result = json.getString(MsgType.REGIST_RESULT);
				if (regist_result.equals(MsgType.REGIST_SUCCESS)) {
					// 注册成功
					LoginWindow.informationDialog(null, "注册成功");
				} else if (regist_result.equals(MsgType.EMAIL_REGISTERED)) {
					// 邮箱已被注册
					LoginWindow.informationDialog(null, "邮箱已被注册");
				} else if (regist_result.equals(MsgType.DUPLICATENAME)) {
					// 用户名重复
					LoginWindow.informationDialog(null, "用户名已被占用");
				}
				break;
			case MsgType.CMD:
				String m = json.getString("message");
				CMDWindow.getInstance().handleReceiveMessage(m);
				
				break;
			case MsgType.CHANGEPWD_RESULT:
				break;
			case MsgType.RESTPWD_RESULT:
				break;
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
