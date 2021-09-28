/**
 * Copyright (c) 2019-present ProconServerRPG JavaFX Library All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name ProconServerRPG JavaFX Library nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 */
package zenryokuservice.opencv.fx;

import java.awt.image.BufferedImage;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * コマンド実行するための抽象クラス。
 * 
 * @author takunoji
 *
 * 2020/05/17
 */
public interface CommandIF {
	/** Canvasクラスから取得したGraphics2Dに描画する */
	public abstract void execute(Pane pane) throws Exception;
	/** 実装クラスを取得する */
	public  abstract CommandIF getCommand();
	/** 描画したCanvasを取得する */
	public abstract Canvas getBefore() throws Exception;
	public abstract Canvas getAfter() throws Exception;
	public abstract GraphicsContext getBeforeGraphics();
	public abstract GraphicsContext getAfterGraphics();	
	public abstract BufferedImage getBeforeImage();
	public abstract BufferedImage getAfterImage();
}
