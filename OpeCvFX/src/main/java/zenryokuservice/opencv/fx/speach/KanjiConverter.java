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
                                                "マ", "ミ", "ム", "メ", "モ", // 11
                                                "ヤ", "ユ", "ヨ", // 12
                                                "ャ", "ュ", "ョ", // 13
                                                "ラ", "リ", "ル", "レ", "ロ", // 14
                                                "ワ", "ヲ", "ン", // 15
                                                "。", "、", // 16
                                            };
    /** kuromojiトーカナイザー */
    private Tokenizer tokenizer;
    /** 変換用のマップ */
    private Map<String, String> talkMap;
    /** 数字の変換マップ */
    private Map<Character, String> noTalkMap;


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
            , "maeh", "miee", "muh", "mee", "moh" // 11
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

        noTalkMap = new HashMap<>();
        char[] nums = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        String[] noMoji = {"ichi", "knee", "sun", "see", "ggoo", "roc", "nana", "hatchi", "qu", " "};
        int count = 0;
        for (char c : nums) {
            noTalkMap.put(c, noMoji[count]);
            count++;
        }
    }

    /** 終了処理 */
    public void finalize() {
        tokenizer = null;
    }

    public Map<String, String> getMap() {
        return this.talkMap;
    }
    public Map<Character, String> getNoMap() {
        return this.noTalkMap;
    }

    /**
     * 入力された文字をすべてカタカナに変換する
     * @param inputText
     * @return カタカナ or 英数字の文字列
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

    /**
     * 入力文字列の数字部分を発音用の文字列に変換
     * @param text 数字のみが見変換の文字列
     * @return 数字を発音用の文字列に変換した文字列
     */
    private String convertNo(String text) {
        StringBuilder res = new StringBuilder();
        String[] words = text.split(" ");
        for (String val : words) {
            if (val.matches("[0-9]+")) {
                res.append(this.toNoTalk(val, val.length() - 1));
                continue;
            }
            res.append(val + " ");
        }
        return res.toString();
    }

    /**
     * 数の単語を発音用の文字列に変換
     * @param num 数字の単語
     * @param len 単語の長さ
     * @return 発音用の文字列
     */
    private String toNoTalk(String num, int len) {
        StringBuilder build = new StringBuilder();
        char[] ch = num.toCharArray();
        String[] tani = {"", " jue ", " hyacku ", " sen ",  " man "};
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '0') {
                continue;
            }
            build.append(noTalkMap.get(ch[i]) + " " + tani[len - i] + " ");
        }
        System.out.println("No: " + build.toString());
        return build.toString();
     }

    /**
     * 入力文字列を発音用の文字列に変換
     * @param kanaMessage カタカナの入力文字列
     * @return 発音用の文字列
     */
    public String toTalk(String kanaMessage) {
        kanaMessage = kanaMessage.replaceAll(" ", "");
        char[] chars = kanaMessage.toCharArray();
        StringBuilder build = new StringBuilder();
        boolean isNo = false;
        for (char ch : chars) {
            // 英字が数字の場合はそのまま追加
            if (ch >= 65 && ch <= 122) {
                if (isNo) {
                    build.append(" ");
                }
                build.append(ch);
                isNo = false;
            } else if (Character.toString(ch).matches("[0-9]")) {
                build.append(ch);
                isNo = true;
            } else {
                if (isNo) {
                    build.append(" ");
                }
                String note = this.talkMap.get(String.valueOf(ch));
                String append = note == null ? " " : note;
                build.append(append + " ");
                isNo = false;
            }
        }
        return convertNo(build.toString());
    }
}
