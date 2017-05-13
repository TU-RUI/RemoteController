package com.controller.window;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import com.controller.model.ComputerModel;
import com.controller.model.ImageManager;
import com.controller.server.ScreenModule;
import com.server.message.ObjectMessage;
import com.server.net.MsgType;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScreenWindow extends Application {

	private static ImageView imageView;

	private static ScreenWindow screenWindows;

	public static ScreenWindow getInstance() {
		if (screenWindows == null) {
			screenWindows = new ScreenWindow();
		}
		return screenWindows;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("远程控制");
		stage.setScene(createScene());
		stage.setResizable(true);
		stage.show();
		stage.setOnCloseRequest(e -> {
			ScreenModule.getInstance().closeSession();
			ScreenModule.getInstance().closeModule();
			Platform.runLater(() -> {
				try {
					MainWindow.getInstance().start(new Stage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			stage.close();
		});
		ScreenModule.getInstance().init();
	}

	private Scene createScene() {

		Pane screenPane = new Pane();
		screenPane.setPrefSize(1280, 720);
		screenPane.setLayoutX(0);
		screenPane.setLayoutY(0);

		Image image = new Image("background.jpg");
		imageView = new ImageView(image);
		imageView.setFitHeight(720);
		imageView.setFitWidth(1280);
		imageView.setLayoutX(0);
		imageView.setLayoutY(0);
		imageView.setPreserveRatio(true);
		imageView.fitHeightProperty().bind(screenPane.heightProperty());
		imageView.fitWidthProperty().bind(screenPane.widthProperty());
		imageView.setCacheHint(CacheHint.SPEED);
		screenPane.getChildren().add(imageView);

		Scene scene = new Scene(screenPane, 1280, 720);

		scene.setOnKeyPressed(e -> {
			if (ComputerModel.model != null) {
				ObjectMessage objectMessage = new ObjectMessage();
				objectMessage.setMsgType(MsgType.KEY_PRESS);
				objectMessage.setKey(e.getCode());
				objectMessage.setTempid(ComputerModel.model.getTempid());
				try {
					ScreenModule.getInstance().sendData(objectMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		scene.setOnKeyReleased(e -> {
			if (ComputerModel.model != null) {
				ObjectMessage objectMessage = new ObjectMessage();
				objectMessage.setMsgType(MsgType.KEY_RELEASE);
				objectMessage.setKey(e.getCode());
				objectMessage.setTempid(ComputerModel.model.getTempid());
				try {
					ScreenModule.getInstance().sendData(objectMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		scene.setOnMouseMoved(e -> {
			if (ComputerModel.model != null) {
				double imageViewHeight = imageView.getFitHeight();
				double imageViewWidth = imageView.getFitWidth();
				double sceneX = e.getSceneX();
				double sceneY = e.getSceneY();
				System.out.println("sceneX: " + sceneX + " sceneY: " + sceneY);
				// 宽 / 高
				if (ComputerModel.model.getScreenheight() != 0
						&& ComputerModel.model.getScreenheight() != 0) {
					int originImageHeight = ComputerModel.model
							.getScreenheight();
					int originImageWidth = ComputerModel.model.getScreenwidth();
					double rate = (double) originImageWidth
							/ (double) originImageHeight;
					System.out.println(rate);
					double realImageHeight, realImageWidth;
					if (imageViewWidth / imageViewHeight > rate) {
						realImageHeight = imageViewHeight;
						realImageWidth = realImageHeight * rate;
					} else {
						realImageWidth = imageViewWidth;
						realImageHeight = realImageWidth / rate;
					}
					System.out.println("realImageWidth: " + realImageWidth
							+ " realImageHeight: " + realImageHeight);
					if (sceneX < realImageWidth && sceneY < realImageHeight) {
						ObjectMessage objectMessage = new ObjectMessage();
						objectMessage.setMsgType(MsgType.MOUSE_MOVE);
						objectMessage.setX(0);
						objectMessage.setTempid(ComputerModel.model.getTempid());
						double realX = sceneX / realImageWidth
								* originImageWidth;
						double realY = sceneY / realImageHeight
								* originImageWidth;
						objectMessage.setX(realX);
						objectMessage.setY(realY);
						System.out.println("realX: " + realX + " realY: "
								+ realY);
						try {
							ScreenModule.getInstance().sendData(objectMessage);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		scene.setOnMousePressed(e -> {
			if (ComputerModel.model != null) {
				ObjectMessage objectMessage = new ObjectMessage();
				objectMessage.setMsgType(MsgType.MOUSE_PRESS);
				objectMessage.setMouseButton(e.getButton());
				objectMessage.setTempid(ComputerModel.model.getTempid());
				try {
					ScreenModule.getInstance().sendData(objectMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
		scene.setOnMouseReleased(e -> {
			if (ComputerModel.model != null) {
				ObjectMessage objectMessage = new ObjectMessage();
				objectMessage.setMsgType(MsgType.MOUSE_PRESS);
				objectMessage.setMouseButton(e.getButton());
				objectMessage.setTempid(ComputerModel.model.getTempid());
				try {
					ScreenModule.getInstance().sendData(objectMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		return scene;
	}

	public static void main(String[] args) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				try {
					ScreenWindow.getInstance().start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setImage(byte[] image) {
		Platform.runLater(() -> {
			InputStream is = new ByteArrayInputStream(image);
			imageView.setImage(new Image(is));
		});

	}

	public Thread updataThread = new Thread(new Runnable() {

		@Override
		public void run() {
			byte[] data;
			try {
				while ((data = ImageManager.getQueue().take()) != null) {
					InputStream is;
					is = new ByteArrayInputStream(data);
					imageView.setImage(new Image(is));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});

}
