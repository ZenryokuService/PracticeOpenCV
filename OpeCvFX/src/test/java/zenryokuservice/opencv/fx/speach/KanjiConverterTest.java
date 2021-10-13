package zenryokuservice.opencv.fx.speach;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * KanjiConverterクラスのテストを行うクラス。
 */
public class KanjiConverterTest {
    /** static メソッドはstatic修飾子がついてないと参照できない */
    private static KanjiConverter target;

    /** テストクラスをインスタンス化する時に行う処理　*/
    @BeforeClass
    public static void init() {
        // 前処理でテスト対象クラスをインスタンス化
        target = new KanjiConverter();
    }

    /** インスタンスが解放されるとき、ガベージコレクションで実行 */
    @AfterClass
    public static void terminated() {
        target = null;
    }

    /** ICU4Jをとりあえず起動してみる */
    //@Test
    public void test1() {
        Tokenizer tokeni = new Tokenizer();
        List<Token> list = tokeni.tokenize("本日はJava晴天なり");
        for (Token t: list) {
            String[] word = t.getAllFeatures().split(",");
            if (word[8].equals("*")) {
                System.out.println("Surface: " + t.getSurface());
                continue;
            }
            System.out.println("Length: " + t.getAllFeatures().split(",").length);
            System.out.println(t.getSurface() + "\t" + t.getAllFeatures());
        }
    }

    //@Test
    public void testTalkMap() {
        target.getMap().forEach((key, val) -> {
            System.out.println("Key: " + key + " : val: " + val);
        });
    }

    @Test
    public void testSpeach() {
        String c = target.convert("本日はJava晴天なり");
        System.out.println(c);
        String res = target.toTalk(c);
        System.out.println(res);
//        BriefVoiceCls cls = new BriefVoiceCls();
//        cls.execute("本日は晴天なり");
    }

}
