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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import zenryokuservice.opencv.fx.CommandIF;

/**
 * @author takunoji
 *
 * 2020/06/03
 */
public class delBackground implements CommandIF {

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#execute(javafx.scene.layout.Pane)
	 */
	@Override
	public void execute(Pane pane) throws Exception {
		ObservableList<Node> obsList = pane.getChildren();
		Canvas before = null;
		Canvas after = null;
		for (Node node : obsList) {
			before = (Canvas) node.lookup("#testCanvasBefore");
			after =  (Canvas) node.lookup("#testCanvasAfter");
		}
		URL url = this.getClass().getResource("/himawari.png");
		Mat img = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_UNCHANGED);
		printAttribute(img);

		URL url2 = this.getClass().getResource("/road.png");
		Mat imgGray = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_GRAYSCALE);
		Mat thred = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC3);
		Imgproc.threshold(imgGray, thred, 160, 235, Imgproc.THRESH_BINARY);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC3);
		Imgproc.findContours(thred, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		printAttribute(thred);

		Mat cannyMat = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC3);
		// RBG
		Scalar color = new Scalar(0, 255, 0);
		Imgproc.drawContours(cannyMat, contours, -1, color, 1);
		
		Mat res = new Mat();
		Scalar c = new Scalar(64,64,64);
		for (MatOfPoint p : contours) {
			Imgproc.fillConvexPoly(img, p, c);
		}
		//cannyMat = removeBlack(cannyMat);
		Mat dst = new Mat();
		//Core.addWeighted(img, 0, cannyMat, 0, 0.5, dst);
		MatOfByte imgData = new MatOfByte();
		MatOfByte imgData2 = new MatOfByte();
		MatOfByte imgDataR = new MatOfByte();
		Imgcodecs.imencode(".png", cannyMat, imgData);
		Imgcodecs.imencode(".png", thred, imgDataR);
		Imgcodecs.imencode(".png", img, imgData2);

		BufferedImage out = ImageIO.read(new ByteArrayInputStream(imgData.toArray()));
		GraphicsContext g = before.getGraphicsContext2D();
		g.drawImage(SwingFXUtils.toFXImage(out, null), 0, 0);

		BufferedImage outR = ImageIO.read(new ByteArrayInputStream(imgDataR.toArray()));
		GraphicsContext gR = after.getGraphicsContext2D();
		gR.drawImage(SwingFXUtils.toFXImage(outR, null), 0, 0);

	}

	private void printAttribute(Mat img) {
		System.out.println("行: " + img.rows());
		System.out.println("列: " + img.rows());
		System.out.println("チャンネル: " + img.channels());
		System.out.println("画素数: " + img.size().toString());
	}

	private Mat removeBlack(Mat in) {
		System.out.println("channels: " + in.channels());
		for (int y = 0; y < in.rows(); y++) {
			for (int x = 0; x < in.cols(); x++) {
				double[] d = in.get(y, x);
				System.out.println("color: " + d[1] + "alph: " + d[3]);
				d[3] = 0.8;
				if (d[1] != 0.0) {
					d[2] = 255.0;
					d[3] = 0.8;
				}
				in.put(y, x, d);
			}
		}
		return in;
	}
	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getCommand()
	 */
	@Override
	public CommandIF getCommand() {
		return this;
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getBefore()
	 */
	@Override
	public Canvas getBefore() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getAfter()
	 */
	@Override
	public Canvas getAfter() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getBeforeGraphics()
	 */
	@Override
	public GraphicsContext getBeforeGraphics() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getAfterGraphics()
	 */
	@Override
	public GraphicsContext getAfterGraphics() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getBeforeImage()
	 */
	@Override
	public BufferedImage getBeforeImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getAfterImage()
	 */
	@Override
	public BufferedImage getAfterImage() {
		// TODO Auto-generated method stub
		return null;
	}

}
