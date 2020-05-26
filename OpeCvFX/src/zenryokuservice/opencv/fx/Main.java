package zenryokuservice.opencv.fx;
	
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import zenryokuservice.opencv.fx.controller.TestingCvController;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * OpenCVの学習用のGUIを作成する。
 * JavaFXで画面を作成している<br/>
 * 1.SceneBuilderでFXMLを出力
 * 2.FXMLで取得したコンポーネントを表示する
 * 
 * @author takunoji
 * 2018/11/18
 */
public class Main extends Application {
	private double xPos;
	private double yPos;
	/** ネイティブライブラリを読み込む */
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	/**
	 * メインメソッド。
	 * @param args
	 */
	public static void main(String[] args) {
		// 親クラス(Superクラス)のメソッド起動
		launch();
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("TestingCv.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root, 800, 600);
		scene.setFill(null);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		// 作成するクラス
		final TestingCvController controller = loader.getController();
		controller.setPane(root);
		primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				controller.setClosed();
			}
		}));
		xPos = 200;
		yPos = 200;
		// キーアクションを追加する
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getEventType() == KeyEvent.KEY_PRESSED) {
					try {
					keyHandle(event.getCode(), root, primaryStage, controller);
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		primaryStage.setTitle("Video Processing");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/** Keyイベント */
	private final void keyHandle(KeyCode code, BorderPane pane, Stage primary, TestingCvController controller) throws Exception {
		CommandIF cmd = controller.getCommand();
		switch (code) {
		case A: // 左
			System.out.println("LEFT");
			leftHandle(cmd.getBefore(), cmd.getBeforeImage());
			break;
		case D: // 右
			System.out.println("RIGHT");
			rightHandle(cmd.getBefore(), cmd.getBeforeImage());
			break;
		case W: // 上
			System.out.println("UP");
			upHandle(cmd.getBefore(), cmd.getBeforeImage());
			break;
		case X: // 下
			System.out.println("DOWN");
			downHandle(cmd.getBefore(), cmd.getBeforeImage());
			break;
		case Q:
			primary.setX(0.0);
			primary.setY(0.0);
			primary.setMaximized(true);
			pane.setStyle("-fx-background-color: transparent");			
			break;
		case E:
			primary.setX(200);
			primary.setY(100);
			pane.setStyle("");
			break;
		case ESCAPE: // 終了
			System.out.println("終了");
			System.exit(0);
		default:
			System.out.println("*** 未定義コード " + code.getName() + "***");
		}
	}

	private void leftHandle(Canvas canvas, BufferedImage buf) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		xPos -= 10;
		g.drawImage(SwingFXUtils.toFXImage(buf, null), xPos, yPos);
	}
	private void rightHandle(Canvas canvas, BufferedImage buf) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		xPos += 10;
		g.drawImage(SwingFXUtils.toFXImage(buf, null), xPos, yPos);
	}
	private void upHandle(Canvas canvas, BufferedImage buf) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		yPos -= 10;
		g.drawImage(SwingFXUtils.toFXImage(buf, null), xPos, yPos);
	}
	private void downHandle(Canvas canvas, BufferedImage buf) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		yPos += 10;
		g.drawImage(SwingFXUtils.toFXImage(buf, null), xPos, yPos);
	}
}
