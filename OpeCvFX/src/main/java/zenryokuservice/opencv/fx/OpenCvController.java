package zenryokuservice.opencv.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import zenryokuservice.opencv.fx.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	/** FXML カメラ開始ボタン -> fx:id="button" */
	@FXML
	private Button button;
	/** FXML グレースケールチェックボックス -> fx:id="grayscale" */
	@FXML
	private CheckBox grayscale;
	/** FXML ロードチェックボックス -> fx:id="logoCheckBox" */
	@FXML
	private CheckBox logoCheckBox;
	/** FXML ヒストグラム -> fx:id="histogram" */
	@FXML
	private ImageView histogram;
	/** FXML 画像を表示するラベル -> fx:id="currentFrame" */
	@FXML
	private ImageView currentFrame;
	/** 録画ボタン */
	@FXML
	private Button recButton;
	
	/** タイマー */
	private ScheduledExecutorService timer;
	/** OpenCVの部品 */
	private VideoCapture capture;
	/** OpenCVの部品 */
	private VideoWriter writer;
	/** カメラ起動 */
	private boolean cameraActive;
	/** ロゴ */
	private Mat logo;
	/** 出力するファイル名 */
	private String videoFileName;

	/** デバック用フラグ */
	private boolean debug = true;
	/**
	 * FXMLLoaderに自動的に呼ばれるらしい。
	 */
	public void initialize()
	{
		this.capture = new VideoCapture();
		this.cameraActive = false;
	}

	/**
	 * Recボタン押下時の処理
	 */
	@FXML
	private void record() {
		if (writer == null) {
			FileChooser choice = new FileChooser();
			choice.setInitialDirectory(new File("movies/"));
			choice.setTitle("保存するファイルを選択してください。");
			File file = choice.showSaveDialog(null);
			if (file == null) {
				this.recButton.setText("Rec");
				writer.release();
				return;
			}
			videoFileName = file.getName();
		}
		if ("Rec".equals(this.recButton.getText())) {
			this.recButton.setText("Stop");
		} else if ("Stop".equals(this.recButton.getText())) {
			this.recButton.setText("Rec");
			writer.release();
		}
		startCamera();
	}
	/**
	 * ボタン押下時の処理(イベント処理)
	 */
	@FXML
	protected void startCamera()
	{
		// フレームサイズ指定
		this.currentFrame.setFitWidth(600);
		// イメージのレイシオを設定する
		this.currentFrame.setPreserveRatio(true);
		
		if (!this.cameraActive)
		{
			// キャプチャ開始
			this.capture.open(0);
			
			// ビデオが起動しているか否か
			if (this.capture.isOpened())
			{
				this.cameraActive = true;
				
				// フレームを33ミリ秒ごとに掴む (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run()
					{
						Mat frame = grabFrame();
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(currentFrame, imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.button.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Impossible to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.button.setText("Start Camera");
			
			// stop the timer
			this.stopAcquisition();
		}
	}
	
	/**
	 * The action triggered by selecting/deselecting the logo checkbox
	 */
	@FXML
	protected void loadLogo()
	{
		if (logoCheckBox.isSelected())
		{
			// read the logo only when the checkbox has been selected
			this.logo = Imgcodecs.imread("resources/images/Poli.png");
		}
	}
	
	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame()
	{
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);
				if (writer == null && "Stop".equals(this.recButton.getText())) {
					writer = new VideoWriter("/Users/takk/git/OpenCvFX/OpeCvFX/movies/" + videoFileName
							, FOURCC_MP4S, 20, new Size(frame.cols(), frame.rows()), true);
					writer.write(frame);
				}
				
				// if the frame is not empty, process it
				if (!frame.empty())
				{
					// add a logo...
					if (logoCheckBox.isSelected() && this.logo != null)
					{
						Rect roi = new Rect(frame.cols() - logo.cols(), frame.rows() - logo.rows(), logo.cols(),
								logo.rows());
						Mat imageROI = frame.submat(roi);
						// add the logo: method #1
						Core.addWeighted(imageROI, 1.0, logo, 0.8, 0.0, imageROI);
						
						// add the logo: method #2
						// logo.copyTo(imageROI, logo);
					}
					
					// if the grayscale checkbox is selected, convert the image
					// (frame + logo) accordingly
					if (grayscale.isSelected())
					{
						Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
					}
					
					// show the histogram
					this.showHistogram(frame, grayscale.isSelected());
				}
				
			}
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the frame elaboration: " + e);
			}
		}
		
		return frame;
	}
	
	/**
	 * Compute and show the histogram for the given {@link Mat} image
	 * 
	 * @param frame
	 *            the {@link Mat} image for which compute the histogram
	 * @param gray
	 *            is a grayscale image?
	 */
	private void showHistogram(Mat frame, boolean gray)
	{
		// split the frames in multiple images
		List<Mat> images = new ArrayList<Mat>();
		Core.split(frame, images);
		
		// set the number of bins at 256
		MatOfInt histSize = new MatOfInt(256);
		// only one channel
		MatOfInt channels = new MatOfInt(0);
		// set the ranges
		MatOfFloat histRange = new MatOfFloat(0, 256);
		
		// compute the histograms for the B, G and R components
		Mat hist_b = new Mat();
		Mat hist_g = new Mat();
		Mat hist_r = new Mat();
		
		// B component or gray image
		Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), hist_b, histSize, histRange, false);
		
		// G and R components (if the image is not in gray scale)
		if (!gray)
		{
			Imgproc.calcHist(images.subList(1, 2), channels, new Mat(), hist_g, histSize, histRange, false);
			Imgproc.calcHist(images.subList(2, 3), channels, new Mat(), hist_r, histSize, histRange, false);
		}
		
		// draw the histogram
		int hist_w = 150; // width of the histogram image
		int hist_h = 150; // height of the histogram image
		int bin_w = (int) Math.round(hist_w / histSize.get(0, 0)[0]);
		
		Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0));
		// normalize the result to [0, histImage.rows()]
		Core.normalize(hist_b, hist_b, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
		
		// for G and R components
		if (!gray)
		{
			Core.normalize(hist_g, hist_g, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
			Core.normalize(hist_r, hist_r, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
		}
		
		// effectively draw the histogram(s)
		for (int i = 1; i < histSize.get(0, 0)[0]; i++)
		{
			// B component or gray image
			Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_b.get(i - 1, 0)[0])),
					new Point(bin_w * (i), hist_h - Math.round(hist_b.get(i, 0)[0])), new Scalar(255, 0, 0), 2, 8, 0);
			// G and R components (if the image is not in gray scale)
			if (!gray)
			{
				Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_g.get(i - 1, 0)[0])),
						new Point(bin_w * (i), hist_h - Math.round(hist_g.get(i, 0)[0])), new Scalar(0, 255, 0), 2, 8,
						0);
				Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_r.get(i - 1, 0)[0])),
						new Point(bin_w * (i), hist_h - Math.round(hist_r.get(i, 0)[0])), new Scalar(0, 0, 255), 2, 8,
						0);
			}
		}
		
		// display the histogram...
		Image histImg = Utils.mat2Image(histImage);
		updateImageView(histogram, histImg);
		
	}
	
	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition()
	{
		if (this.timer != null && !this.timer.isShutdown())
		{
			try
			{
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		
		if (this.capture.isOpened())
		{
			// release the camera
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
