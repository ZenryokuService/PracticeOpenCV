/**
 * Copyright (c) 2019-present ProconServerRPG JavaFX Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name ProconServerRPG JavaFX Library nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 */
package zenryokuservice.opencv.fx.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import zenryokuservice.opencv.fx.CommandIF;
import zenryokuservice.opencv.fx.learn.LearnOpenCv;

/**
 * @author takunoji
 *
 * 2020/05/16
 */
public class TestingCvController {

	@FXML
	private Canvas testCanvasBefore;
	@FXML
	private Canvas testCanvasAfter;

	@FXML
	private TextField input;
	
	private Properties prop;
	
	private CommandIF cmd;

	private Pane pane;

	/** コンストラクタ */
	public TestingCvController() {
		this.testCanvasBefore = new Canvas();
		this.testCanvasAfter = new Canvas();
//		this.testing = new LearnOpenCv();
		this.prop = new Properties();
		String propPath = "/command.properties";
		try {
			this.prop.load(this.getClass().getResourceAsStream(propPath));
		} catch (IOException e) {
			System.out.println(">>> Error! プロパティファイルの読み込みに失敗しました。" + propPath);
			e.printStackTrace();
		}
		// 確認
		System.out.println("プロパティ: " + prop.get("hello"));
	}

	/**
	 * 画面のExecuteボタンを押下した時に起動する処理
	 */
	@FXML
	protected void clickExecute() throws Exception {
		// 初期化する
		cmd = null;
		// 入力確認用
//		System.out.println(this.input.getText());
		String inputStr = this.input.getText();
		cmd = this.getCommand(inputStr);
		if (cmd == null) {
			// プロパティファイルに、コマンドがない
			System.out.println("コマンドがあません。" + this.input.getText());
		} else {
			// コマンド実行
			cmd.execute(this.pane);
		}
	}

	@FXML
	public void setClosed() {
		// 現状は空実装
	}

	/**
	 * Clearボタンを押下した時の処理
	 */
	@FXML
	public void clear() {
		System.out.println("Clear");
		// 描画したものをクリアする
		this.testCanvasBefore.getGraphicsContext2D().clearRect(0, 0, this.testCanvasBefore.getWidth(), this.testCanvasBefore.getHeight());
		// 描画したものをクリアする
		this.testCanvasAfter.getGraphicsContext2D().clearRect(0, 0, this.testCanvasAfter.getWidth(), this.testCanvasAfter.getHeight());
	}

	public CommandIF getCommand() {
		return this.cmd;
	}

	public Canvas getBefore() {
		return this.testCanvasBefore;
	}

	public Canvas getAfter() {
		return this.testCanvasAfter;
	}

	public void onTextArea() {
		
	}
	// --------- プライベートメソッド -------- //
	private CommandIF getCommand(String command) {
		String fulClassName = this.prop.getProperty(command);
		if (fulClassName == null) {
			return null;
		}
		CommandIF cmd = null;
		try {
			// クラスオブジェクトの取得
			@SuppressWarnings("rawtypes")
			Class c = Class.forName(fulClassName);
			@SuppressWarnings({ "unchecked" })
			Class<? extends CommandIF> clz = (Class<? extends CommandIF>) c;
			// 取得したクラスオブジェクトのインスタンス生成
			cmd = clz.newInstance();
		} catch (ClassNotFoundException e) {
			System.out.println("呼び出しクラスにCommandIFが実装されていません。" + fulClassName);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmd;
	}

	@FXML
	private void terminated() {
		System.exit(0);
	}

	public void setPane(Pane pane) {
		this.pane = pane;
	}
}
