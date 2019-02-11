package zenryokuservice.opencv.fx;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import zenryokuservice.opencv.fx.util.Utils;

/**
 * FXMLで定義したコントローラクラス。
 * 参考サイトを写経しようとしたが、ソースを少しいじる
 * 基本的には写経している。
 * @see 
 * @author takunoji
 * 2018/11/17
 */
public class OpenCvController {
	 private final int FOURCC_MP4S = VideoWriter.fourcc('M', 'P', '4', 'S');
	 /** FXML定義のボタン */
	@FXML
	private Button button;
	/** 録画ボタン */
	@FXML
	private Button recButton;
	/** FXML定義のImageViewer */
	@FXML
	private ImageView currentFrame;
	/** ロゴを表示するチェックボックス */
	@FXML
	private CheckBox logoCheckBox;
	/** グレースケースのチェックボックス */
	@FXML
	private CheckBox grayscale;
	/** ヒストグラムの表示領域 */
	@FXML
	private ImageView histogram;
	/** ロゴ */
	private Mat logo;
	
	/** ビデオキャプチャ */
	private VideoCapture capture = new VideoCapture();
	/** ビデオライター */
	private VideoWriter writer;
	
	private ScheduledExecutorService timer;
	// a flag to change the button behavior
	private boolean cameraActive = false;
	// the id of the camera to be used
	private static int cameraId = 0;
	private String VIDEO_FILE_NAME = null;
		
	/**
	 * ボタン押下時のアクション処理。
	 * "@FXML"アノテーションでFXMLとの同期を取っている
	 * @param event ボタン押下のイベント
	 */
	@FXML
	protected void startCamera(ActionEvent event) {
		// フレームサイズの指定
		this.currentFrame.setFitWidth(600);
		this.currentFrame.setPreserveRatio(true);
		// カメラがアクティブ状態の時は停止する
		if (this.cameraActive) {
			this.cameraActive = false;
			System.out.println("Stop");
			this.button.setText("Start Camera");
			this.stopAcquisition();
			// 処理終了
			return;
		} 
		if (this.cameraActive == false && this.capture.isOpened() == false) {
			this.cameraActive = true;
			// カメラを起動する
			this.capture.open(this.cameraId);
		}
		// カメラが開いていない時
		if (this.capture.isOpened() == false) {
			// エラーログを出力して処理を終了する
			System.err.println("Impossible to open the camera connection...");
			return;
		}
		// カメラが正常に開いている時
		this.cameraActive = true;
		// 匿名クラス
		Runnable frameGrabber = new Runnable() {
			@Override
			public void run() {
				Mat frame = grabFrame();
				Image imageToShow = Utils.mat2Image(frame);
				updateImageView(currentFrame, imageToShow);
			}
		};
		this.timer = Executors.newSingleThreadScheduledExecutor();
		this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
		this.button.setText("Stop Camera");
	}

	@FXML
	private void exeRecButtonCheck() {
		System.out.println("***[" + this.recButton.getText() + "]***");
		if ("Rec".equals(this.recButton.getText())) {
			this.recButton.setText("Stop");
			FileChooser choice = new FileChooser();
			choice.setTitle("ファイルを指定してください。");
			choice.setInitialDirectory(new File("movies/"));
			File movFile = choice.showSaveDialog(null);
			choice.getExtensionFilters().addAll(
			         new FileChooser.ExtensionFilter("Text Files", "*.txt"),
			         new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
			         new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
			         new FileChooser.ExtensionFilter("All Files", "*.*"));
			System.out.println(movFile.getName());
			VIDEO_FILE_NAME = movFile.getName();
			this.startCamera(null);
		} else if ("Stop".equals(this.recButton.getText()) && writer != null) {
			writer.release();
		} else {
			System.out.println("想定外の文字列が設定されています、強制終了します。");
			System.exit(-1);
		}
	}
	
	/**
	 * 開いているビデオストリームからフレームを取得する
	 * @return {@link Mat}
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();
		if (this.capture.isOpened()) {
			try {
				this.capture.read(frame);
				if (writer == null && "Stop".equals(this.recButton.getText())) {
					System.out.println("[" + VIDEO_FILE_NAME + "] Start!");
					writer = new VideoWriter("/Users/takk/git/OpenCvFX/OpeCvFX/movies/" + VIDEO_FILE_NAME
							, FOURCC_MP4S, 20, new Size(frame.cols(), frame.rows()), true);
					writer.write(frame);
				}
				if (frame.empty() == false) {
					// ロゴのコントロール
					logoControll(frame);
					// グレースケール
					grayScaleControll(frame);
					// ヒストグラムの表示
					this.showHistgram(frame);
				}
			} catch(Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return frame;
	}

	/**
	 * 画面を停止するときの前処理
	 */
	private void stopAcquisition() {
		if (this.timer != null && this.timer.isShutdown() == false) {
			System.out.println("Timer Shutdown");
			try {
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MICROSECONDS);
			} catch(Exception e) {
				// log any exception
				e.printStackTrace();
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		// @FIXME-[カメラを解放するだけで良い？]
		if (this.capture.isOpened()) {
			System.out.println("Camera Close");
			this.capture.release();
		}
	}
	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}
	
	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed() {
		this.stopAcquisition();
	}

	/**
	 * grayScaleのチェックボックスに
	 * チェックが入っている時、入っていない時のハンドルを行う
	 * @param frame フレーム(Mat)
	 */
	private void grayScaleControll(Mat frame) {
		if (this.grayscale.isSelected()) {
			Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
		}
	}
	/**
	 * ロゴのコントロール。
	 * チェックが入っている時、入っていない時の処理をハンドルする。
	 * @param frame フレーム(Mat)
	 */
	private void logoControll(Mat frame) {
		if (this.logoCheckBox.isSelected() && this.logo != null) {
			// new Rect(X座標, Y座標, 横幅, 縦幅)
			Rect roi = new Rect(frame.cols() - this.logo.cols(), frame.rows() - this.logo.rows()
					, this.logo.cols(), this.logo.rows());
			Mat imageRoi = frame.submat(roi);
			Core.addWeighted(imageRoi, 1.0, this.logo, 0.8, 0.0, imageRoi);
		}
	}
	/**
	 * ロゴのチェックボックスにチェックが入っている時の処理
	 */
	@FXML
	private void loadLogo() {
		if (logoCheckBox.isSelected())
		{
			// read the logo only when the checkbox has been selected
			this.logo = Imgcodecs.imread("resources/images/Poli.png");
		}
	}

	/**
	 * ヒストグラムの表示を行う。
	 * @param frame 表示するための画面領域
	 * @param isSelected グレースケールのチェックボックスが選択されているか否か
	 */
	private void showHistgram(Mat frame) {
		List<Mat> images = new ArrayList<>();
		Core.split(frame, images);
		
		MatOfInt histSize = new MatOfInt(256);
		MatOfInt channels = new MatOfInt(0);
		MatOfFloat histRange = new MatOfFloat(0, 256);

		Mat hist_b = new Mat();
		Mat hist_g = new Mat();
		Mat hist_r = new Mat();

		// ヒストグラムの表示領域
		int histHeight = 150;
		int histWidth = 150;
		int binWidth = (int) Math.round(histWidth / histSize.get(0,  0)[0]);
		
		Mat histImage = new Mat(histWidth, histHeight, CvType.CV_8UC3, new Scalar(0,0,0));
		Core.normalize(hist_b, hist_b, 0, histImage.rows(), Core.NORM_MINMAX, -1 , new Mat());
		// RGB別の計算、グレースケールの時はBのみを描画する
		Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), hist_b, histSize, histRange, false);
		if (this.grayscale.isSelected() == false) {
			Imgproc.calcHist(images.subList(1, 2), channels, new Mat(), hist_g, histSize, histRange, false);
			Imgproc.calcHist(images.subList(2, 3), channels, new Mat(), hist_r, histSize, histRange, false);
			Core.normalize(hist_g, hist_g, 0, histImage.rows(), Core.NORM_MINMAX, -1 , new Mat());
			Core.normalize(hist_r, hist_r, 0, histImage.rows(), Core.NORM_MINMAX, -1 , new Mat());
		}
		// ヒストグラムの表示(描画)
		this.calcHistgram(histSize, histImage, binWidth, histHeight, hist_b, hist_g, hist_r);
	}

	/**
	 * ヒストグラムの表示と計算処理。
	 * 
	 * @param histSize ヒストグラムの値（高さ）
	 * @param histImage ヒストグラムのイメージ(Mat)
	 * @param binWidth ???
	 * @param histHeight ヒストグラムの表示領域（高さ）
	 * @param hist_b RGBの「B」
	 * @param hist_g RGBの「G」
	 * @param hist_r RGBの「R」
	 */
	private void calcHistgram(MatOfInt histSize, Mat histImage, int binWidth, int histHeight, Mat hist_b, Mat hist_g, Mat hist_r) {
		for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
			long firstParam = binWidth * (i - 1);
			long secondParam = histHeight - Math.round(hist_b.get(i - 1, 0)[0]);
			long thirdParam = binWidth * (i);
			long fourthParam = histHeight - Math.round(hist_b.get(i, 0)[0]);
			
			Imgproc.line(histImage, new Point(firstParam, secondParam), new Point(thirdParam, fourthParam ), new Scalar(255, 0, 0), 2, 8, 0);
			if (this.grayscale.isSelected()) {
				long g_secondParam = histHeight - Math.round(hist_b.get(i - 1, 0)[0]);
				long g_fourthParam = histHeight - Math.round(hist_b.get(i, 0)[0]);
				Imgproc.line(histImage, new Point(firstParam, g_secondParam), new Point(thirdParam, g_fourthParam), new Scalar(255, 0, 0), 2, 8, 0);
				long r_secondParam = histHeight - Math.round(hist_b.get(i - 1, 0)[0]);
				long r_fourthParam = histHeight - Math.round(hist_b.get(i, 0)[0]);
				Imgproc.line(histImage, new Point(firstParam, r_secondParam), new Point(thirdParam, r_fourthParam), new Scalar(255, 0, 0), 2, 8, 0);
			}
		}
	}
}
