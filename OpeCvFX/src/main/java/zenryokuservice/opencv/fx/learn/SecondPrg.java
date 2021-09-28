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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import zenryokuservice.opencv.fx.CommandIF;
import zenryokuservice.opencv.fx.supers.TestingOpenCvSuper;

/**
 * @author takunoji
 *
 * 2020/05/17
 */
public class SecondPrg extends TestingOpenCvSuper {

	@Override
	public void execute(Pane pane) throws Exception {
		Canvas before = null;
		Canvas after = null;
		
		observableList = pane.getChildren();
		before = getBefore();
		after = getAfter();
		// 表示するイメージを取得
		URL url = getClass().getResource("/pipo-charachip001.png");
		// 表示イメージを読み取る
		Mat charactor = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_COLOR);
		Mat charactor1 = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_UNCHANGED);
		System.out.println("Channels = channelA: " + charactor.channels() + " channelB: " + charactor1.channels());
		System.out.println("Length = channelA: " + charactor.get(0, 0).length + " channelB: " + charactor1.get(0, 0).length);
		System.out.println("Byte1 = channelA: " + charactor.get(5, 24)[0] + " channelB: " + charactor1.get(6, 24)[0]);
		System.out.println("Byte last = channelA: " + charactor.get(5, 24)[2] + " channelB: " + charactor1.get(6, 24)[3]);
		boolean boo = true;

		for (int y = 0; y < charactor1.height(); y++) {
			for (int x = 0; x < charactor1.width(); x++) {
				double[] d = charactor1.get(y, x);
				
				if (d[0] == 255.0) {
					if (boo) {
						System.out.println("*** Testing ***");
						boo = false;
					}
					d[3] = 0.0;
					charactor1.put(y, x, d);
				}
			}
		}
		System.out.println("Byte last = channelA: " + charactor.get(5, 24)[2] + " channelB: " + charactor1.get(6, 24)[3]);
		MatOfByte b = new MatOfByte();
		Imgcodecs.imencode(".png", charactor, b);
		BufferedImage out = createImage(b);
		drawBufferedImage(out, before);

		MatOfByte c = new MatOfByte();
		Imgcodecs.imencode(".png", charactor1, c);
		BufferedImage out1 = createImage(c);
		drawBufferedImage(out1, after);

		beforeImage = out;
		afterImage = out1;
		graphicsBefore = before.getGraphicsContext2D();
		graphicsAfter = after.getGraphicsContext2D();
	}

	private BufferedImage createImage(MatOfByte b) {
		try {
			return ImageIO.read(new ByteArrayInputStream(b.toArray()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void drawBufferedImage(BufferedImage buf, Canvas canvas) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.drawImage(SwingFXUtils.toFXImage(buf, null), buf.getWidth(), buf.getHeight());
	}

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
