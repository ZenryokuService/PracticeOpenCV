package zenryokuservice.opencv.fx.speach;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.apache.commons.lang3.ArrayUtils;
import sun.security.util.ArrayUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KanjiConverter {
    private static final String[] KANA_LIST = {"ア", "イ", "ウ", "エ", "オ", // 1
                                                "カ", "キ", "ク", "ケ", "コ", // 2
                                                "ガ", "ギ", "グ", "ゲ", "ゴ", // 3
                                                "サ", "シ", "ス", "セ", "ソ", // 4
                                                "ザ", "ジ", "ズ", "ゼ", "ゾ", // 5
                                                "タ", "チ", "ツ", "テ", "ト", // 6
                                                "ダ", "ヂ", "ヅ", "デ", "ド", // 7
                                                "ナ", "ニ", "ヌ", "ネ", "ノ", // 8
                                                "ハ", "ヒ", "フ", "ヘ", "ホ", // 9
                                                "バ", "ビ", "ブ", "ベ", "ボ", // 10
                                                "マ", "ミ", "ム", "ﾒ", "モ", // 11
                                                "ヤ", "ユ", "ヨ", // 12
                                                "ャ", "ュ", "ョ", // 13
                                                "ラ", "リ", "ル", "レ", "ロ", // 14
                                                "ワ", "ヲ", "ン", // 15
                                                "。", "、", // 16
                                            };
    /** kuromojiトーカナイザー */
    private Tokenizer tokenizer;
    /** 変換用のマップ */
    Map<String, String> talkMap;


    /** コンストラクタ */
    public KanjiConverter() {
        talkMap = new HashMap<>();
        tokenizer = new Tokenizer();
        String[] moji = {"ah", "yee" , "hu", "a", "oh" // 1
            , "kah", "kee", "ku", "ckea", "koh" // 2
            , "gaah", "gy", "goo", "gue", "goh" // 3
            , "saeh", "see", "su", "thea", "soh" // 4
            , "zaeh", "zee", "zoo", "zea", "zoh" // 5
            , "taeh", "tiee", "tsu", "te", "toh" // 6
            , "daeh", "dgee", "do", "de", "doh" // 7
            , "naeh", "niee", "nuh", "nea", "noh" // 8
            , "haeh", "hiee", "hu", "hea", "hoh" // 9
            , "baeh", "bee", "boo", "be", "boh" // 10
            , "maeh", "miee", "muh", "me", "moh" // 11
            , "yaeh", "yu", "yoh" // 12
            , "ya", "yu", "yo" // 13
            , "ra", "ri", "ru", "re", "roh" // 14
            , "wa", "oh", "um" // 15
            ," ", " ", " "
        };

        for (int i = 0; i < KANA_LIST.length; i++) {
            String key = KANA_LIST[i];
            String value = moji[i];
            talkMap.put(key, value);
        }
        // 区切り文字のスペース(" ")を追加する
        talkMap.put(" ", " ");
    }

    /** 終了処理 */
    public void finalize() {
        tokenizer = null;
    }

    public Map<String, String> getMap() {
        return this.talkMap;
    }
    /**
     * 入力された文字をすべてひらがなに変換する
     * @param inputText
     */
    public String convert(String inputText) {
        List<Token> list = tokenizer.tokenize(inputText);
        StringBuilder build = new StringBuilder();

        for (Token token : list) {
            String[] splits = token.getAllFeatures().split(",");
            if (splits[8].equals("*")) {
                build.append(token.getSurface() + " ");
            } else {
                build.append(splits[8] + " ");
            }
        }
        return build.toString();
    }

    public String toTalk(String kanaMessage) {
        System.out.println(kanaMessage);
        char[] chars = kanaMessage.toCharArray();
        StringBuilder build = new StringBuilder();
        for (char ch : chars) {
            if (ch >= 65 && ch <= 122) {
                build.append(ch);
                continue;
            } else {
                String note = this.talkMap.get(String.valueOf(ch));
                String append = note == null ? " " : note;
                build.append(append + " ");
            }
        }
        return build.toString();
    }
}
