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
import zenryokuservice.opencv.fx.CommandIF;
import zenryokuservice.opencv.fx.learn.LearnOpenCv;

/**
 * @author takunoji
 *
 * 2020/05/16
 */
public class TestingCvController {

	@FXML
	private Canvas testCanvas;
	@FXML
	private TextField input;

	/** コンストラクタ */
	public TestingCvController() {
		this.testCanvas = new Canvas();
//		this.testing = new LearnOpenCv();
	}

	/**
	 * 画面のExecuteボタンを押下した時に起動する処理
	 */
	@FXML
	protected void clickExecute() {
		// 入力確認用
		System.out.println(this.input.getText());
		CommandIF cmd = new LearnOpenCv();
		cmd.execute(this.testCanvas);
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
		this.testCanvas.getGraphicsContext2D().clearRect(0, 0, this.testCanvas.getWidth(), this.testCanvas.getHeight());
	}
}
