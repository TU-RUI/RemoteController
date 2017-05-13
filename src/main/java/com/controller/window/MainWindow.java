package com.controller.window;

import com.controller.model.ComputerModel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainWindow extends Application {
	private static MainWindow mainWindow = null;
	private Stage stage;
	
	public static MainWindow getInstance(){
		if(mainWindow == null){
			mainWindow = new MainWindow();
		}
		return mainWindow;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("远程桌面控制");
		stage.setScene(createScene());
		stage.setResizable(false);
		stage.show();
		stage.setOnCloseRequest( e ->{
			System.exit(0);
		});
		this.stage = stage;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}
	
	public Scene createScene(){
		Pane flowPane = new Pane();
		flowPane.setPrefSize(600, 350);
		Insets insets = new Insets(50,50,50,50);
		flowPane.setPadding(insets);
		Image image;
		if(ComputerModel.model == null){
			image = new Image("offline.png",50,50,false,false);			
		}else{
			image = new Image("online.png",50,50,false,false);			
		}
		ImageView pcState = new ImageView(image);
		pcState.setLayoutX(100);
		pcState.setLayoutY(50);
		
		Label ipLabel = new Label("localhost");
		ipLabel.setFont(new Font(20));
		ipLabel.setPrefSize(600, 50);
		ipLabel.setLayoutX(50);
		ipLabel.setLayoutY(50);
		ipLabel.setAlignment(Pos.BASELINE_CENTER);
		if(ComputerModel.model != null && ComputerModel.model.getDevip() != null)
		ipLabel.setText(ComputerModel.model.getDevip());
		flowPane.getChildren().add(pcState);
		flowPane.getChildren().add(ipLabel);
		
		GridPane gridPane = new GridPane();
		gridPane.setPrefSize(600, 300);
		gridPane.setLayoutX(100);
		gridPane.setLayoutY(120);
		gridPane.setHgap(50);
		gridPane.setVgap(55);
		Button button1 = new Button("远程控制");
		button1.setPrefSize(500, 50);
		if(ComputerModel.model == null){
			button1.setDisable(true);
		}
		button1.setOnAction(e->{
			Platform.runLater(() ->{
				try {
					ScreenWindow.getInstance().start(new Stage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				stage.hide();
			});
		});
		Button button2 = new Button("命令行");
		button2.setPrefSize(500, 50);
		if(ComputerModel.model == null){
//			button2.setDisable(true);
		}
		button2.setOnAction(e->{
			Platform.runLater(() ->{
				try {
					CMDWindow.getInstance().start(new Stage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.stage.hide();
			});
		});
		
		Button button3 = new Button("远程关机");
		button3.setPrefSize(500, 50);
		if(ComputerModel.model == null){
			button3.setDisable(true);
		}
		button3.setOnAction(e->{
			
		});
		gridPane.add(button1, 0, 0);
		gridPane.add(button2, 0, 1);
		gridPane.add(button3, 0, 2);
		
		flowPane.getChildren().add(gridPane);
		Scene scene = new Scene(flowPane,700,450);
		return scene;
		
	}


}
