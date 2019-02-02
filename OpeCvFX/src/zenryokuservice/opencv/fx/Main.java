package zenryokuservice.opencv.fx;
	
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * OpenCVnogaの学習用のGUIを作成する。
 * JavaFXで画面を作成している<br/>
 * 1.SceneBuilderでFXMLを出力
 * 2.FXMLで取得したコンポーネントを表示する
 * 
 * @author takunoji
 * 2018/11/18
 */
public class Main extends Application {
	/** ビデオキャプチャ画面の名前 */
	private static final String VIDEO_VIEW = "Video";
	/** ヒストグラム画面 */
	private static final String HISTGRAM_VIEW = "Histgram";

	/** 画面切り替え用ParentMap */
	private Map<String, Parent> parentMap;
	/**
	 * このアプリケーション(Mainメソッド)が起動する前に
	 * 起動(OpenCVのロード)する
	 */
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * このMainクラスを初期化する。
	 * 1. parentMapの初期化
	 * =>OpenCvTest.fxmlをロードした時の画面
	 * =>HistgramTest.fxmlをロードした時の画面
	 */
	private void initMain() {
		parentMap = new HashMap<String, Parent>();
		// FXMLをロード
		try {
			BorderPane video = (BorderPane)FXMLLoader.load(getClass().getResource("OpenCvTest.fxml"));
			parentMap.put(VIDEO_VIEW, video);
//			BorderPane histgram = (BorderPane)FXMLLoader.load(getClass().getResource("HistgramTest.fxml"));
//			parentMap.put(HISTGRAM_VIEW, histgram);
		} catch (Exception e) {
			e.printStackTrace();
			// 例外発生時はアプリケーションをエラーコードを返却して強制終了
			System.exit(-1);
		}

	}
	/**
	 * スーパクラスApplication)のstartメソッドを起動する<br/>
	 * オーバーライドしているので、本当は親クラスのメソッドが起動するが
	 * 子クラスで上書きするので、このクラスを起動した時にはこちらのメソッドが起動する。
	 */
	@Override
	public void start(Stage primaryStage) {
		// Mainクラスを起動するための準備を行う
		initMain();
		try {
			// Parent(FXMLよりロードした画面)
			Parent root = parentMap.get(VIDEO_VIEW);
			// 表示領域を作成する
			Scene scene = new Scene(root,400,400);
			// JavaFX用のCSSを適用する
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// Sceneをステージに設定する
			primaryStage.setScene(scene);
			// 表示処理
			primaryStage.show();
			/* **バグ、と言うか実装もれ？ ** */
			// アプリケーションを閉じるときに適切な動作を設定する
			OpenCvController ctl = new OpenCvController();
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>(){
				@Override
				public void handle(WindowEvent event) {
					ctl.setClosed();
				}
			}));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * メインメソッド。
	 * JavaFXの規定部品<br/>
	 * Application{@link #start(Stage)}を起動する
	 * 
	 * @param args プログラム引数
	 */
	public static void main(String[] args) {
		// スーパークラス(親)のメソッドを起動する
		launch(args);
	}
}
