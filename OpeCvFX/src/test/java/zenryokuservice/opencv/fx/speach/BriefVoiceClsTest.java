package zenryokuservice.opencv.fx.speach;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BriefVoiceClsTest {
    private static BriefVoiceCls target;
    @BeforeClass
    public static void init() {
        target = new BriefVoiceCls();
    }

    @Test
    public void testTalkVoice() {
        target.execute("本日はJava晴天なり");
    }

    @Test
    public void testTalkVoice2() {
        target.execute("今回は、JavaAPIの学習を行います。");
    }

    @Test
    public void testTalkVoice3() {
        target.execute("Hello Worldを実装してみましょう。");
    }

    @Test
    public void testTalkVoice4() {
        target.execute("12345。");
    }

}
