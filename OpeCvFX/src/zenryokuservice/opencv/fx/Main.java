package zenryokuservice.opencv.fx;
	
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import zenryokuservice.opencv.fx.controller.TestingCvController;
import zenryokuservice.opencv.fx.tutorial.VideoController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
		FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("TestingCv.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setTitle("Video Processing");
		primaryStage.setScene(scene);
		primaryStage.show();
		// 作成するクラス
		TestingCvController controller = loader.getController();
		primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				controller.setClosed();
			}
		}));
	}
}
