/**
 * Copyright (c) 2019-present ProconServerRPG JavaFX Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name ProconServerRPG JavaFX Library nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 */
package zenryokuservice.opencv.fx.learn;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import zenryokuservice.opencv.fx.CommandIF;
import zenryokuservice.opencv.fx.supers.TestingOpenCvSuper;

/**
 * @author takunoji
 *
 * 2020/05/16
 */
public class LearnOpenCv extends TestingOpenCvSuper {

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#execute(javafx.scene.canvas.GraphicsContext)
	 */
	@Override
	public void execute(Pane pane) throws Exception {
		Canvas before = null;
		Canvas after = null;
		
		observableList = pane.getChildren();
		BorderPane border = (BorderPane) pane;
		before = getBefore();
		after = getAfter();
		before.setWidth(500.0);
		before.setHeight(500.0);
		// 表示するイメージを取得
		URL url = getClass().getResource("/myFace.jpg");
		// 表示イメージを読み取る
		Mat charactor = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_COLOR);
		Mat gray = new Mat();
		Imgproc.cvtColor(charactor, gray, Imgproc.COLOR_BGR2GRAY);
		// 背景除去
		Mat hierarchy = Mat.zeros(new Size(200, 200), CvType.CV_8UC1);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		// -- 第二引数 -- //
		// RETR_TREE
		// RETR_EXTERNAL
		// RETR_LIST
		// RETR_CCOMP
		// -- 第三引数 -- //
		// CHAIN_APPROX_NONE
		// CHAIN_APPROX_SIMPLE
		// CHAIN_APPROX_TC89_L1
		// CHAIN_APPROX_TC89_KCOS
		Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//		printContuors(contours);
//		printHierarchy(hierarchy);
		Mat dstContuor = Mat.zeros(new Size(charactor.width(),charactor.height()),CvType.CV_8UC1);
		Scalar color=new Scalar(255,255,255);
		for (int i = 0; i < contours.size(); i++) {
	        Imgproc.drawContours(dstContuor, contours, i, color,0, 8, hierarchy);
		}

//		Size resize = new Size(before.getWidth() / 3, before.getHeight() / 3);
//		Mat reSizeChar = new Mat();
//		Imgproc.resize(charactor, reSizeChar, resize);
		
		MatOfByte charaByteBefore = new MatOfByte();
		// 表示イメージをcharaByteに書き込む
		Imgcodecs.imencode(".png", dstContuor, charaByteBefore);

		Mat dst = new Mat();
//		Imgproc.threshold(reSizeChar, dst, 100, 255, Imgproc.THRESH_BINARY);
		MatOfByte charaByte = new MatOfByte();
		// 表示イメージをcharaByteに書き込む
		Imgcodecs.imencode(".png", charactor, charaByte);
		
		try {
			// 書き込んだイメージを描画する
			BufferedImage buf = ImageIO.read(new ByteArrayInputStream(charaByteBefore.toArray()));
			BufferedImage buf1 = ImageIO.read(new ByteArrayInputStream(charaByte.toArray()));
			beforeImage = buf;
			GraphicsContext g = before.getGraphicsContext2D();
			g.drawImage(SwingFXUtils.toFXImage(buf, null), buf.getWidth(), buf.getHeight());
//			graphicsBefore = g;
			
//			graphicsAfter = after.getGraphicsContext2D();
//			graphicsAfter.drawImage(SwingFXUtils.toFXImage(buf1, null), buf1.getWidth(), buf1.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printHierarchy(Mat hierarchy) {
		int rows = hierarchy.rows();
		int cols = hierarchy.cols();
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				System.out.print("[" + hierarchy.get(y, x)[0] + ", " + hierarchy.get(y, x)[1] + hierarchy.get(y, x)[2]);
			}
		}
		System.out.println(hierarchy);
	}
	private void printContuors(List<MatOfPoint> contours) {
		for(MatOfPoint p : contours) {
			int rows = p.rows();
			int cols = p.cols();
			System.out.println("rows: " + rows + " cols: " + cols);
			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					double[] d = p.get(y, x);
					System.out.println("0: " + d[0] + " 1: " + d[1]);
				}
			}
		}
	}
	/** コードのバックアップ */
	private void bak1(Canvas canvas) {
		// 表示するイメージを取得
		URL url = getClass().getResource("/pipo-charachip007.png");
		// 表示イメージを読み取る
		Mat charactor = Imgcodecs.imread(url.getPath(), CvType.CV_8UC4);
		MatOfByte charaByte = new MatOfByte();
		// 表示イメージをcharaByteに書き込む
		Imgcodecs.imencode(".png", charactor, charaByte);
		try {
			// 書き込んだイメージを描画する
			BufferedImage buf = ImageIO.read(new ByteArrayInputStream(charaByte.toArray()));
			GraphicsContext g = canvas.getGraphicsContext2D();
			g.drawImage(SwingFXUtils.toFXImage(buf, null), buf.getWidth(), buf.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getCommand()
	 */
	@Override
	public CommandIF getCommand() {
		return this;
	}

	public GraphicsContext getBeforeGraphics() {
		return this.graphicsBefore;
	}

	public GraphicsContext getAfterGraphics() {
		return this.graphicsAfter;
	}

	public BufferedImage getBeforeImage() {
		return beforeImage;
	}

	public BufferedImage getAfterImage() {
		return afterImage;
	}
}
