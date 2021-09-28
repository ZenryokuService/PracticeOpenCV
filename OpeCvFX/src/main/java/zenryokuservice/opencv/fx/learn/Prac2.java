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

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
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
 * 2020/05/30
 */
public class Prac2 implements CommandIF {

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#execute(javafx.scene.layout.Pane)
	 */
	@Override
	public void execute(Pane pane) throws Exception {
		// このメソッドも実装する必要あり、というかここが動く
		ObservableList<Node> obsList = pane.getChildren();
		Canvas before = null;
		for (Node node : obsList) {
			before = (Canvas) node.lookup("#testCanvasBefore");
		}
		// イメージファイルパスを取得する
		URL url = getClass().getResource("/himawari.png");
		// イメージファイルをロードして行列(Mat)に格納
		Mat img = Imgcodecs.imread(url.getPath(), Imgcodecs.IMREAD_COLOR);
		
		// 今回はサイズ変更を行う
		Mat resizeImage = new Mat();
		Size size = new Size(200, 200);
		// 読み込んだMatをresizeImageに書き込む
		Imgproc.resize(img, resizeImage, size);
		MatOfByte bikeByte = new MatOfByte();
		// 画像データをMatOfByteに書き込む
		Imgcodecs.imencode(".jpeg", resizeImage, bikeByte);
		// BuffereImageを取得する
		BufferedImage outImage = ImageIO.read(new ByteArrayInputStream(bikeByte.toArray()));
		// Canvasへの描画準備
		GraphicsContext g = before.getGraphicsContext2D();
		// 描画ポイントを指定して描画する
		g.drawImage(SwingFXUtils.toFXImage(outImage, null), 0, 0);
		
		
	}

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#getCommand()
	 */
	@Override
	public CommandIF getCommand() {
		// このメソッドだけは実装する必要あり
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
