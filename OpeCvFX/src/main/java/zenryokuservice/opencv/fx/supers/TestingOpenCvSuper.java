/**
 * Copyright (c) 2019-present ProconServerRPG JavaFX Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name ProconServerRPG JavaFX Library nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 */
package zenryokuservice.opencv.fx.supers;

import java.awt.image.BufferedImage;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import zenryokuservice.opencv.fx.CommandIF;

/**
 * @author takunoji
 *
 * 2020/05/23
 */
public abstract class TestingOpenCvSuper implements CommandIF {
	protected ObservableList<Node> observableList;
	protected Canvas before;
	protected Canvas after;
	protected GraphicsContext graphicsBefore;
	protected GraphicsContext graphicsAfter;
	protected BufferedImage beforeImage;
	protected BufferedImage afterImage;
	/** 棒などのイメージ */
	protected BufferedImage optImg;

	protected double x;
	protected double y;
	
	public TestingOpenCvSuper() {
	}

	public Canvas getBefore() throws Exception {
		Canvas before = null;
		if (observableList == null) {
			throw new Exception("abservableListが設定されていません");
		}
		for (Node node : observableList) {
			before = (Canvas) node.lookup("#testCanvasBefore");
		}
		return before;
	}

	public Canvas getAfter() throws Exception{
		if (observableList == null) {
			throw new Exception("abservableListが設定されていません");
		}
		Canvas after = null;
		for (Node node : observableList) {
			after = (Canvas) node.lookup("#testCanvasAfter");
			if (after != null) {
				break;
			}
		}
		return after;
	}

	public void drawOpt() {
		graphicsBefore.drawImage(SwingFXUtils.toFXImage(optImg, null), 10, 10);
	}

	public void hideOpt() {
		
	}

	public void clearText() {
		graphicsBefore.clearRect(this.x * 0.2, this.y * 0.8, this.x * 0.6, this.y * 0.9);
	}

	public void drawText(String in) {
		System.out.println(in);
		graphicsBefore.setFill(Color.BLACK);
		graphicsBefore.fillText(in, this.x * 0.2, this.y * 0.2);
		
	}

	public void onTextArea() {
		graphicsBefore.setFill(Color.AQUA);
		graphicsBefore.fillRect(this.x * 0.2, this.y * 0.8, this.x * 0.6, this.y * 0.9);
	}

	public void setTextPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
