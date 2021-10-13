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
        return "<jsml><para>this speach is test <sayas class='literal'>API</sayas> " +
                "to integrate with <emp> most </emp> " +
                "Of the voice engines on the market today</para>" +
                "<break msecs='300'/><para>Stay on top of the latest developments" +
                "By reading everything you can" +
                "<sayas class='literal'>JSR113</sayas></para></jsml>";
    }

    /**
     * Implemented so the listener can print out the source
     */
    public String toString() {
        return getJSMLText();
    }

}