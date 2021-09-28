package zenryokuservice.opencv.fx.speach;

import javax.speech.synthesis.Speakable;

/**
 * Simple Speakable
 *  Returns marked-up text to be spoken
 */
public class BriefSpeakable implements Speakable {

    /**
     * Returns marked-up text.  The markup is used to help the vice engine.
     */
    public String getJSMLText() {
        return "<jsml><para>This Speech <sayas class='literal'>API</sayas> " +
                "can integrate with <emp> most </emp> " +
                "of the speech engines on the market today.</para>" +
                "<break msecs='300'/><para>Keep on top of the latest developments " +
                "by reading all you can about " +
                "<sayas class='literal'>JSR113</sayas></para></jsml>";
    }

    /**
     * Implemented so the listener can print out the source
     */
    public String toString() {
        return getJSMLText();
    }

}