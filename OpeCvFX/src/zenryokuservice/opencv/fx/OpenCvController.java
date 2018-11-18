package zenryokuservice.opencv.fx;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	 /** FXML定義のボタン */
	@FXML
	private Button button;
	/** FXML定義のImageViewer */
	@FXML
	private ImageView currentFrame;
	
	/** ビデオキャプチャ */
	private VideoCapture capture = new VideoCapture();
	
	private ScheduledExecutorService timer;
	// a flag to change the button behavior
	private boolean cameraActive = false;
	// the id of the camera to be used
	private static int cameraId = 0;
		
	/**
	 * ボタン押下時のアクション処理。
	 * "@FXML"アノテーションでFXMLとの同期を取っている
	 * @param event ボタン押下のイベント
	 */
	@FXML
	protected void startCamera(ActionEvent event) {
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
			System.out.println("Active");
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

	/**
	 * 開いているビデオストリームからフレームを取得する
	 * @return {@link Mat}
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();
		if (this.capture.isOpened()) {
			try {
				this.capture.read(frame);
				if (frame.empty() == false) {
					Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
				}
				
			} catch(Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return frame;
	}

	private void stopAcquisition() {
		if (this.timer != null && this.timer.isShutdown() == false) {
			System.out.println("Timer Shutdown");
			try {
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MICROSECONDS);
				// メモリ解放
				System.gc();
			} catch(Exception e) {
				// log any exception
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
}
