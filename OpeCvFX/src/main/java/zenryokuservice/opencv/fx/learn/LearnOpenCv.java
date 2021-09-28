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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
		
		observableList = pane.getChildren();
		BorderPane border = (BorderPane) pane;
		before = getBefore();
		after = getAfter();
		before.setWidth(pane.getWidth());
		before.setHeight(pane.getHeight());
		HBox hbox = (HBox)border.getCenter();
		hbox.setMaxSize(pane.getWidth(), pane.getHeight());
		hbox.getChildren().clear();
		hbox.getChildren().add(before);
		setTextPos(pane.getWidth(), pane.getHeight());
		// 表示するイメージを取得
		URL url = getClass().getResource("/charactors/myFace.png");
		URL url_kanaB = getClass().getResource("/charactors/kanabo.png");
System.out.println(url.getPath());
System.out.println(url_kanaB.getPath());
		// 表示イメージを読み取る
		Mat charactor = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_UNCHANGED);
		Mat gray = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_GRAYSCALE);
		Mat kanaImg = Imgcodecs.imread(url_kanaB.getPath(), Imgcodecs.IMREAD_UNCHANGED);
System.out.println("*** " + kanaImg);
//		optImg = createBufferedImage(kanaImg, ".png");
		optImg = ImageIO.read(url_kanaB);
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
		Scalar color=new Scalar(0,255,0);
	   Imgproc.drawContours(dstContuor, contours, 0, color,0, 4, hierarchy);

//		Size resize = new Size(before.getWidth() / 3, before.getHeight() / 3);
//		Mat reSizeChar = new Mat();
//		Imgproc.resize(charactor, reSizeChar, resize);

		try {
			// 書き込んだイメージを描画する
			//BufferedImage buf = createBufferedImage(charactor, ".png");
			BufferedImage buf = ImageIO.read(url);
			beforeImage = buf;
			graphicsBefore = before.getGraphicsContext2D();
			graphicsBefore.drawImage(SwingFXUtils.toFXImage(buf, null), buf.getWidth(), buf.getHeight());
			graphicsAfter = after.getGraphicsContext2D();
			graphicsAfter.setFill(Color.AQUA);
			graphicsAfter.fillRect(0, 0, after.getWidth(), after.getHeight());
		} catch (IOException e) {
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

	private BufferedImage createBufferedImage(Mat img, String ext) throws IOException {
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(ext, img, matOfByte);
		 return ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
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
