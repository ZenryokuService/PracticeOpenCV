/**
 * Copyright (c) 2019-present ProconServerRPG JavaFX Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name ProconServerRPG JavaFX Library nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 */
package zenryokuservice.opencv.fx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 2Dゲーム作成用のイメージファイルを切り取って
 * アニメーション用の画像に出力する。
 * 
 * @author takunoji
 * 2020/05/10
 */
public class ImageSlicerMain {
	/** ネイティブライブラリを読み込む */
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static final String RESOURCE_DIR = "/";

	/** 切り取るためのサイズ指定 */
	private int dtop;
	private int dbottom;
	private int dleft;
	private int dright;
	// 切り取りするファイルの幅
	private int cutWidth;
	// 切り取りするファイルのたかさ
	private int cutHeight;
	

	public ImageSlicerMain() {
	}

	public ImageSlicerMain(int dtop, int dbottom, int dleft, int dright) {
		this.dtop = dtop;
		this.dbottom = dbottom;
		this.dleft = dleft;
		this.dright = dright;
		this.cutWidth = dright - dleft;
		this.cutHeight = dbottom - dtop;
	}

	public void loadImg(String targetDir, String fileName, int size) {
		//System.out.println(RESOURCE_DIR + targetDir + "/"+ fileName);
		URL p = this.getClass().getResource("/" + fileName);
		System.out.println("*** " + p.getPath());
		Mat downloadFile = Imgcodecs.imread(p.getPath());
		coutFile(downloadFile, fileName);
		Rect[] dsts = null;
		List<ImageIcon> iconList = new ArrayList<>();
		double heightTimes = downloadFile.height() / dtop;
		double widthTimes = downloadFile.width() / dleft;
		int times = 0;
		for (int i = 0; i < heightTimes-1; i++) {
			for (int j = 0; j < widthTimes-1; j++) {
				times++;
				break;
			}
			break;
		}
		// Create Swing Compoenent
		JFrame frame = new JFrame("Show Image");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < iconList.size(); i++) {
			frame.getContentPane().add(new JLabel(iconList.get(i)));
		}
		frame.pack();
		frame.setVisible(true);
	}

	private ImageIcon dstImage(String fileName, Mat downloadFile, String sufix, int xPos, int yPos, int size, int times) {
		Rect roi = new Rect(xPos, yPos, size, size);
		System.out.println("x: " + roi.x);
		System.out.println("y: " + roi.y);
		Mat target = downloadFile.submat(roi);

		return null;
	}

	private Mat createAlpha(Mat buf) {
		int width = buf.width();
		int height = buf.height();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
//					System.out.println("x: " + x + " y: " + y + " value: " + buf.get(y, x));
			}
		}
		BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		return buf;
	}

	private void coutFile(Mat img, String path) {
		System.out.println(path);
		System.out.println("width: " + img.width());
		System.out.println("height: " + img.height());
		System.out.println("dips: " + img.depth());
		
	}

	public static void main(String[] args) {
		int targetSize = 40;
		ImageSlicerMain main = new ImageSlicerMain(targetSize, targetSize, targetSize, targetSize);
		main.loadImg("charactors", "pipo-charachip007.png", 32);
	}
}
