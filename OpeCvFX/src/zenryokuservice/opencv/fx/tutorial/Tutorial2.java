/**
 * Copyright (c) 2012-present Lightweight Java Game Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name Lightweight Java Game Library nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 */
package zenryokuservice.opencv.fx.tutorial;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * OpenCVのチュートリアルを写経します。
 * @author takunoji
 * 2019/02/02
 */
public class Tutorial2 extends Application {
	public static void main(String[] args) {
		// 親クラス(Superクラス)のメソッド起動
		launch();
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = FXMLLoader.load(getClass().getResource("View.xml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setTitle("Video Processing");
		primaryStage.setScene(scene);
		primaryStage.show();
		// 作成するクラス
		VideoController controller = loader.getController();
		primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				controller.setClosed();
			}
		}));
	}
}
