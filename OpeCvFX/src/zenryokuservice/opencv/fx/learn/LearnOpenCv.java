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

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import zenryokuservice.opencv.fx.CommandIF;

/**
 * @author takunoji
 *
 * 2020/05/16
 */
public class LearnOpenCv implements CommandIF{

	/* (non-Javadoc)
	 * @see zenryokuservice.opencv.fx.CommandIF#execute(javafx.scene.canvas.GraphicsContext)
	 */
	@Override
	public void execute(Canvas canvas) {
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
		}	}

}
