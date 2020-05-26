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
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import zenryokuservice.opencv.fx.CommandIF;

/**
 * @author takunoji
 *
 * 2020/05/23
 */
public abstract class TestingOpenCvSuper implements CommandIF {
	protected ObservableList<Node> observableList;
	protected GraphicsContext graphicsBefore;
	protected GraphicsContext graphicsAfter;
	protected BufferedImage beforeImage;
	protected BufferedImage afterImage;
	
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
		}
		return after;
	}
}
