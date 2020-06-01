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

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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
 * 2020/05/31
 */
public class Prac3 implements CommandIF {

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#execute(javafx.scene.layout.Pane)
	 */
	@Override
	public void execute(Pane pane) throws Exception {
		ObservableList<Node> obsList = pane.getChildren();
		Canvas before = null;
		for (Node node : obsList) {
			before = (Canvas) node.lookup("#testCanvasBefore");
		}
		URL url = this.getClass().getResource("/road.png");
		Mat img = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_UNCHANGED);
		printAttribute(img);

		 URL url2 = this.getClass().getResource("/Tea.png");
		 Mat tea = Imgcodecs.imread(url2.getPath(), Imgcodecs.IMREAD_UNCHANGED);
		// 文字列の配置
		Imgproc.putText(img, "sample!", new Point(10, 10), Core.FONT_HERSHEY_SIMPLEX, 0.5, Scalar.all(0));
		// 
		Mat roi = img.submat(new Rect(20, 20, tea.width(), tea.height()));
		Mat dst = new Mat();
		tea.copyTo(roi);

//		Core.addWeighted(roi, 0.8, tea, 0.8, 0, dst);
		
		MatOfByte imgByte = new MatOfByte();
		Imgcodecs.imencode(".png", img, imgByte);

		BufferedImage out = ImageIO.read(new ByteArrayInputStream(imgByte.toArray()));

		GraphicsContext g = before.getGraphicsContext2D();
		g.drawImage(SwingFXUtils.toFXImage(out, null), 0, 0);
	}

	private void printAttribute(Mat img) {
		System.out.println("行: " + img.rows());
		System.out.println("列: " + img.rows());
		System.out.println("チャンネル: " + img.channels());
		System.out.println("画素数: " + img.size().toString());
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
