package com.controller.window;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.controller.server.BusinessModule;
import com.server.net.ClientType;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginWindow extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginWindow.class);
	private static Stage stage;
	private static final String ServerType = ClientType.CONTROLLER;

	private static BusinessModule serverThread = BusinessModule.getInstance();
	
	private static LoginWindow loginWindow = null;
	
	public static LoginWindow getInstance(){
		if(loginWindow == null){
			loginWindow = new LoginWindow();
		}
		return loginWindow;
	}

	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("远程桌面控制");
		primaryStage.setScene(LoginScene());
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e ->{
			System.exit(0);
		});
		primaryStage.show();
		LOGGER.info("控制端启动...");
	}

	// 登录界面
	private Scene LoginScene() {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(30, 30, 30, 30));
		pane.setHgap(7);
		pane.setVgap(7);

		// 放置面板中的node
		Label nameLabel = new Label("用户名:");
		TextField nameText = new TextField();
		Button forgetBtn = new Button("忘记密码");
		forgetBtn.setBackground(null);
		forgetBtn.setUnderline(true);
		forgetBtn.setOnAction(e -> {
			stage.setScene(ForgetScene());
		});
		Label passwordLabel = new Label("密码:");
		PasswordField passwordText = new PasswordField();
		Button loginBtn = new Button("登陆");
		loginBtn.setPadding(new Insets(10, 30, 10, 30));
		// 登录事件
		loginBtn.setOnAction(e -> {
			LoginAction(nameText.getText(), passwordText.getText());
		});
		Button registBtn = new Button("注册");
		registBtn.setBackground(null);
		registBtn.setUnderline(true);
		registBtn.setOnAction(e -> {
			stage.setScene(RegistScene());

		});
		pane.add(nameLabel, 0, 0);
		pane.add(nameText, 1, 0);
		pane.add(forgetBtn, 2, 0);
		pane.add(passwordLabel, 0, 1);
		pane.add(passwordText, 1, 1);
		pane.add(registBtn, 2, 1);
		pane.add(loginBtn, 1, 2);
		GridPane.setHalignment(loginBtn, HPos.CENTER);

		Scene scene = new Scene(pane, 350, 250);
		return scene;
	}

	// 注册界面
	private Scene RegistScene() {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(30, 30, 30, 30));
		pane.setHgap(5.5);
		pane.setVgap(5.5);

		// 放置面板中的node
		Label nameLabel = new Label("用户名:");
		TextField nameText = new TextField();
		Label mailLabel = new Label("邮箱:");
		TextField mailText = new TextField();
		Label passwordLabel = new Label("密码:");
		TextField passwordText = new TextField();
		Label passwordLabel1 = new Label("确认密码:");
		TextField passwordText1 = new TextField();
		Button loginBtn = new Button("登陆");
		loginBtn.setBackground(null);
		loginBtn.setUnderline(true);
		loginBtn.setOnAction(new LoginHandler());
		Button registBtn = new Button("注册");
		registBtn.setPadding(new Insets(10, 30, 10, 30));
		// 注册事件
		registBtn.setOnAction(e -> {
			RegistAction(nameText.getText(), mailText.getText(),
					passwordText.getText(), passwordText1.getText());
		});
		pane.add(nameLabel, 0, 0);
		pane.add(nameText, 1, 0);
		pane.add(loginBtn, 2, 0);
		pane.add(mailLabel, 0, 1);
		pane.add(mailText, 1, 1);
		pane.add(passwordLabel, 0, 2);
		pane.add(passwordText, 1, 2);
		pane.add(passwordLabel1, 0, 3);
		pane.add(passwordText1, 1, 3);
		pane.add(registBtn, 1, 4);
		GridPane.setHalignment(registBtn, HPos.CENTER);

		Scene scene = new Scene(pane, 350, 250);
		return scene;
	}

	// 忘记密码界面
	private Scene ForgetScene() {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(30, 30, 30, 30));
		pane.setHgap(5.5);
		pane.setVgap(5.5);

		// 放置面板中的node
		Label mailLabel = new Label("邮箱:");
		TextField mailText = new TextField();
		Label passwordLabel = new Label("新密码:");
		TextField passwordText = new TextField();
		Label passwordLabel1 = new Label("确认新密码:");
		TextField passwordText1 = new TextField();
		Button loginBtn = new Button("登录 ");
		loginBtn.setBackground(null);
		loginBtn.setUnderline(true);
		loginBtn.setOnAction(new LoginHandler());
		Button changeBtn = new Button("修改密码");
		changeBtn.setPadding(new Insets(10, 30, 10, 30));
		// 修改密码事件
		changeBtn.setOnAction(e -> {
			ChangeAction(mailText.getText());
		});
		pane.add(mailLabel, 0, 1);
		pane.add(mailText, 1, 1);
		pane.add(loginBtn, 2, 1);
		pane.add(passwordLabel, 0, 2);
		pane.add(passwordText, 1, 2);
		pane.add(passwordLabel1, 0, 3);
		pane.add(passwordText1, 1, 3);
		pane.add(changeBtn, 1, 4);
		GridPane.setHalignment(changeBtn, HPos.CENTER);

		Scene scene = new Scene(pane, 350, 250);
		return scene;
	}

	// 登录界面跳转
	class LoginHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			stage.setScene(LoginScene());
		}

	}

	// 注册界面跳转
	class RegistHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			stage.setScene(RegistScene());
		}

	}

	// 忘记密码界面跳转
	class ForgetHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			stage.setScene(ForgetScene());
		}

	}

	private static void LoginAction(String username, String password) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return;
		}
		JSONObject json = new JSONObject();
		json.put("msgType", "login");
		json.put("username", username);
		json.put("password", password);
		json.put("controlType", ServerType.toString());
		serverThread.sendData(json.toString());
	}

	private static void RegistAction(String username, String email,
			String password, String password1) {
		if (!password.equals(password1)) {
			informationDialog(null, "密码不一致");
			return;
		}

		JSONObject json = new JSONObject();
		json.put("msgType", "regist");
		json.put("username", username);
		json.put("password", password);
		json.put("email", email);
		json.put("controlType", ServerType.toString());
		serverThread.sendData(json.toJSONString());
	}

	private static void ChangeAction(String mail) {

	}

	public static void main(String[] args) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					LoginWindow.getInstance().start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	// 提示框
	public static void informationDialog(String title, String message) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Alert _alert = new Alert(Alert.AlertType.INFORMATION);
				_alert.setTitle("提示");
				if (title != null) {
					_alert.setHeaderText(title);
				}
				_alert.setContentText(message);
				_alert.show();
			}

		});
	}

	// 跳转
	public static void jump() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					MainWindow.getInstance().start(new Stage());
					stage.hide();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
}
