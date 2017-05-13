package com.controller.window;

import com.alibaba.fastjson.JSONObject;
import com.controller.model.ComputerModel;
import com.controller.server.BusinessModule;
import com.server.net.ClientType;
import com.server.net.MsgType;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CMDWindow extends Application {

	private static CMDWindow cmdWindow;
	private TextArea receiveTextArea;
	private TextArea sendTextArea;
	private Button sendButton;

	public static CMDWindow getInstance() {
		if (cmdWindow == null) {
			cmdWindow = new CMDWindow();
		}
		return cmdWindow;
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		stage.setTitle("远程控制");
		stage.setScene(createScene());
		stage.setResizable(false);
		stage.show();
		stage.setOnCloseRequest(e -> {
			Platform.runLater(() -> {
				try {
					MainWindow.getInstance().start(new Stage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			stage.close();
		});
	}

	private Scene createScene() {

		Pane screenPane = new Pane();
		screenPane.setPrefSize(600, 500);
		screenPane.setLayoutX(0);
		screenPane.setLayoutY(0);

		receiveTextArea = new TextArea();
		// receiveTextField.setEditable(false);
		receiveTextArea.setPrefSize(600, 440);
		receiveTextArea.setLayoutX(0);
		receiveTextArea.setLayoutY(0);
		// receiveTextArea.setPrefColumnCount(20);

		sendTextArea = new TextArea();
		sendTextArea.setEditable(true);
		sendTextArea.setPrefSize(460, 60);
		sendTextArea.setLayoutX(0);
		sendTextArea.setLayoutY(440);

		sendButton = new Button();
		sendButton.setPrefSize(140, 60);
		sendButton.setLayoutX(460);
		sendButton.setLayoutY(440);
		sendButton.setText("发送");
		sendButton.setOnAction(e -> {
			String message = sendTextArea.getText().trim();
			handleSendMessage(message);
		});

		screenPane.getChildren().add(receiveTextArea);
		screenPane.getChildren().add(sendTextArea);
		screenPane.getChildren().add(sendButton);

		Scene scene = new Scene(screenPane, 590, 490);
		return scene;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					CMDWindow.getInstance().start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void handleReceiveMessage(String message) {
		if (ComputerModel.model != null) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {
						CMDWindow.getInstance().receiveTextArea.appendText(ComputerModel.model
								.getDevip() + " > ");
						CMDWindow.getInstance().receiveTextArea.appendText(message+ "\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void handleSendMessage(String message) {
		if (ComputerModel.model != null) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {
						JSONObject json = new JSONObject();
						json.put("msgType", MsgType.CMD);
						json.put("message", message);
						json.put("client", ClientType.CONTROLLER);
						json.put("tempid", ComputerModel.model.getTempid());
						BusinessModule.getInstance().sendData(json.toJSONString());
						sendTextArea.clear();
						CMDWindow.getInstance().receiveTextArea.appendText("local > " + message + "\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}
	}

}
