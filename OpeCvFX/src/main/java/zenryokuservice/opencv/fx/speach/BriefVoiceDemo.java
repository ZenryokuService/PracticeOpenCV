package zenryokuservice.opencv.fx.speach;
import java.beans.PropertyVetoException;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineList;
import javax.speech.EngineModeDesc;
import javax.speech.EngineStateError;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RecognizerModeDesc;
import javax.speech.synthesis.JSMLException;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public class BriefVoiceDemo {

    Synthesizer synthesizer;

    public static void main(String[] args) {

        //default synthesizer values
        SynthesizerModeDesc modeDesc = new SynthesizerModeDesc(
                null,       // engine name
                "general",  // mode name use 'general' or 'time'
                Locale.US,  // locale, see MBROLA Project for i18n examples
                null,       // prefer a running synthesizer (Boolean)
                null);      // preload these voices (Voice[])

        //default voice values
        Voice voice = new Voice(
                "kevin16",              //name for this voice
                Voice.AGE_DONT_CARE,   //age for this voice
                Voice.GENDER_DONT_CARE,//gender for this voice
                null);                 //prefer a running voice (Boolean)

        boolean error=false;
        for (int r=0;r<args.length;r++) {
            String token= args[r];
            String value= token.substring(2);

            //overide some of the default synthesizer values
            if (token.startsWith("-E")) {
                //modeDesc.setEngineName(value);
            } else if (token.startsWith("-M")) {
                //modeDesc.setModeName(value);
            } else
                //overide some of the default voice values
                if (token.startsWith("-V")) {
                    voice.setName(value);
                } else if (token.startsWith("-GF")) {
                    voice.setGender(Voice.GENDER_FEMALE);
                } else if (token.startsWith("-GM")) {
                    voice.setGender(Voice.GENDER_MALE);
                } else
                //dont recognize this value so flag it and break out
                {
                    System.out.println(token+
                            " was not recognized as a supported parameter");
                    error = true;
                    break;
                }
        }

        //The example starts here
        BriefVoiceDemo briefExample = new BriefVoiceDemo();
        if (error) {
            System.out.println("BriefVoiceDemo -E<ENGINENAME> " +
                    "-M<time|general> -V<VOICENAME> -GF -GM");
            //list all the available voices for the user
            briefExample.listAllVoices();
            System.exit(1);
        }

        //select synthesizer by the required parameters
        briefExample.createSynthesizer(modeDesc);
        //print the details of the selected synthesizer
        briefExample.printSelectedSynthesizerModeDesc();

        //allocate all the resources needed by the synthesizer
        briefExample.allocateSynthesizer();

        //change the synthesisers state from PAUSED to RESUME
        briefExample.resumeSynthesizer();

        //set the voice
        briefExample.selectVoice(voice);
        //print the details of the selected voice
        briefExample.printSelectedVoice();

        //create a listener to be notified of speech events.
        SpeakableListener optionalListener= new BriefListener();

        //The Date and Time can be spoken by any of the selected voices
        SimpleDateFormat formatter = new SimpleDateFormat("h mm");
        String dateText = "The time is now " + formatter.format(new Date());
        //briefExample.speakTextSynchronously(dateText, optionalListener);

        //General text like this can only be spoken by general voices
        if (briefExample.isModeGeneral()) {
            String[] moji = {"ah", "yee" , "hu", "a", "oh"};
            String[] kagyo = {"kah", "kee", "ku", "ckea", "koh"};
            String[] gagyo = {"gaah", "gy", "goo", "gue", "goh"};
            String[] sagyo = {"saeh", "see", "su", "thea", "soh"};
            String[] zagyo = {"zaeh", "zee", "zoo", "zea", "zoh"};
            String[] tagyo = {"taeh", "tiee", "tsu", "te", "toh"};
            String[] dagyo = {"daeh", "dgee", "do", "de", "doh"};
            String[] nagyo = {"naeh", "niee", "nuh", "nea", "noh"};
            String[] hagyo = {"haeh", "hiee", "hu", "hea", "hoh"};
            String[] bagyo = { "baeh", "bee", "boo", "be", "boh"};
            String[] magyo = {"maeh", "miee", "muh", "me", "moh"};
            String[] yagyo = {"yaeh", "yu", "yoh"};
            String[] ragyo = {"ra", "ri", "ru", "re", "roh"};
            String[] wagyo = {"wa", "oh", "um"};
            //speak plain text
//            String plainText =
//                    "Hello World, This is an example of plain text," +
//                            " any markup like <jsml></jsml> will be spoken as is";
//            for (String val : moji) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : kagyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : sagyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : tagyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : nagyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : hagyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : magyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
//            for (String val : zagyo) {
//                System.out.println(val);
//                briefExample.speakTextSynchronously(val, optionalListener);
//            }
            for (String val : gagyo) {
                System.out.println(val);
                briefExample.speakTextSynchronously(val, optionalListener);
            }

//            //speak marked-up text from Speakable object
//            Speakable speakableExample = new BriefSpeakable();
//            briefExample.speakSpeakableSynchronously(speakableExample,
//                    optionalListener);
        }
        //must deallocate the synthesizer before leaving
        briefExample.deallocateSynthesizer();
    }

    /**
     * Select voice supported by this synthesizer that matches the required
     * properties found in the voice object.  If no matching voice can be
     * found the call is ignored and the previous or default voice will be used.
     *
     * @param voice required voice properties.
     */
    private void selectVoice(Voice voice) {
        try {
            synthesizer.getSynthesizerProperties().setVoice(voice);
        } catch (PropertyVetoException e) {
            System.out.println("unsupported voice");
            exit(e);
        }
    }

    /**
     * This method prepares the synthesizer for speech by moving it from the
     * PAUSED state to the RESUMED state. This is needed because all newly
     * created synthesizers start in the PAUSED state.
     * See Pause/Resume state diagram.
     *
     * The pauseSynthesizer method is not shown but looks like you would expect
     * and can be used to pause any speech in process.
     */
    private void resumeSynthesizer() {
        try {
            //leave the PAUSED state, see state diagram
            synthesizer.resume();
        } catch (AudioException e) {
            exit(e);
        }
    }

    /**
     * The allocate method may take significant time to return depending on the
     * size and capabilities of the selected synthesizer.  In a production
     * application this would probably be done on startup with a background thread.
     *
     * This method moves the synthesizer from the DEALLOCATED state to the
     * ALLOCATING RESOURCES state and returns only after entering the ALLOCATED
     * state. See Allocate/Deallocate state diagram.
     */
    private void allocateSynthesizer() {
        //ensure that we only do this when in the DEALLOCATED state
        if ((synthesizer.getEngineState()&Synthesizer.DEALLOCATED)!=0)
        {
            try {
                //this call may take significant time

                synthesizer.getEngineState();
                synthesizer.allocate();
            } catch (EngineException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (EngineStateError e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * deallocate the synthesizer.  This must be done before exiting or
     * you will run the risk of having a resource leak.
     *
     * This method moves the synthesizer from the ALLOCATED state to the
     * DEALLOCATING RESOURCES state and returns only after entering the
     * DEALLOCATED state. See Allocate/Deallocate state diagram.
     */
    private void deallocateSynthesizer() {
        //ensure that we only do this when in the ALLOCATED state
        if ((synthesizer.getEngineState()&Synthesizer.ALLOCATED)!=0)
        {
            try {
                //free all the resources used by the synthesizer
                synthesizer.deallocate();
            } catch (EngineException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (EngineStateError e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Helper method to ensure the synthesizer is always deallocated before
     * existing the VM.  The synthesiser may be holding substantial native
     * resources that must be explicitly released.
     *
     * @param e exception to print before exiting.
     */
    private void exit(Exception e) {
        e.printStackTrace();
        deallocateSynthesizer();
        System.exit(1);
    }

    /**
     * create a synthesiser with the required properties.  The Central class
     * requires the speech.properties file to be in the user.home or the
     * java.home/lib folders before it can create a synthesizer.
     *
     * @param modeDesc required properties for the created synthesizer
     */
    private void createSynthesizer(SynthesizerModeDesc modeDesc) {
        try {
            //Create a Synthesizer with specified required properties.
            //if none can be found null is returned.
            synthesizer = Central.createSynthesizer(modeDesc);
        }
        catch (IllegalArgumentException e1) {
            e1.printStackTrace();
            System.exit(1);
        } catch (EngineException e1){
            e1.printStackTrace();
            System.exit(1);
        }

        if (synthesizer==null) {
            System.out.println("Unable to create synthesizer with " +
                    "the required properties");
            System.out.println();
            System.out.println("Be sure to check that the \"speech.properties\"" +
                    " file is in one of these locations:");
            System.out.println("  user.home     : "+System.getProperty("user.home"));
            System.out.println("  java.home/lib : "+System.getProperty("java.home")
                    +File.separator+"lib");
            System.out.println();
            System.exit(1);
        }
    }

    private void createSynthesizer(EngineModeDesc modeDesc) {
        try {
            //Create a Synthesizer with specified required properties.
            //if none can be found null is returned.
            synthesizer = Central.createSynthesizer(modeDesc);
        }
        catch (IllegalArgumentException e1) {
            e1.printStackTrace();
            System.exit(1);
        } catch (EngineException e1){
            e1.printStackTrace();
            System.exit(1);
        }

        if (synthesizer==null) {
            System.out.println("Unable to create synthesizer with " +
                    "the required properties");
            System.out.println();
            System.out.println("Be sure to check that the \"speech.properties\"" +
                    " file is in one of these locations:");
            System.out.println("  user.home     : "+System.getProperty("user.home"));
            System.out.println("  java.home/lib : "+System.getProperty("java.home")
                    +File.separator+"lib");
            System.out.println();
            System.exit(1);
        }
    }

    /**
     * is the selected synthesizer capable of speaking general text
     * @return is Mode General
     */
    private boolean isModeGeneral() {
        String mode=this.synthesizer.getEngineModeDesc().getModeName();
        return "general".equals(mode);
    }

    /**
     * Speak the marked-up text provided by the Speakable object and wait for
     * synthesisers queue to empty.  Support for specific markup tags is
     * dependent upon the selected synthesizer.  The text will be read as
     * though the mark up was not present if unsuppored tags are encounterd by
     * the selected synthesizer.
     *
     * @param speakable
     * @param optionalListener
     */
    private void speakSpeakableSynchronously(
            Speakable speakable,
            SpeakableListener optionalListener) {

        try {
            this.synthesizer.speak(speakable, optionalListener);
        } catch (JSMLException e) {
            exit(e);
        }

        try {
            //wait for the queue to empty
            this.synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

        } catch (IllegalArgumentException e) {
            exit(e);
        } catch (InterruptedException e) {
            exit(e);
        }
    }



    /**
     * Speak plain text 'as is' and wait until the synthesizer queue is empty
     *
     * @param plainText that will be spoken ignoring any markup
     * @param optionalListener will be notified of voice events
     */
    private void speakTextSynchronously(String plainText,
                                        SpeakableListener optionalListener) {
        this.synthesizer.speakPlainText(plainText, optionalListener);
        try {
            //wait for the queue to empty
            this.synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

        } catch (IllegalArgumentException e) {
            exit(e);
        } catch (InterruptedException e) {
            exit(e);
        }
    }

    /**
     * Print all the properties of the selected voice
     */
    private void printSelectedVoice() {

        Voice voice = this.synthesizer.getSynthesizerProperties().getVoice();
        System.out.println();
        System.out.println("Selected Voice:"+voice.getName());
        System.out.println("         Style:"+voice.getStyle());
        System.out.println("         Gender:"+genderToString(voice.getGender()));
        System.out.println("         Age:"+ageToString(voice.getAge()));
        System.out.println();
    }

    /**
     * Helper method to convert gender constants to strings
     * @param gender as defined by the Voice constants
     * @return gender description
     */
    private String genderToString(int gender) {
        switch (gender) {
            case Voice.GENDER_FEMALE:
                return "Female";
            case Voice.GENDER_MALE:
                return "Male";
            case Voice.GENDER_NEUTRAL:
                return "Neutral";
            case Voice.GENDER_DONT_CARE:
            default:
                return "Unknown";
        }
    }

    /**
     * Helper method to convert age constants to strings
     * @param age as defined by the Voice constants
     * @return age description
     */
    private String ageToString(int age) {
        switch (age) {
            case Voice.AGE_CHILD:
                return "Child";
            case Voice.AGE_MIDDLE_ADULT:
                return "Middle Adult";
            case Voice.AGE_NEUTRAL:
                return "Neutral";
            case Voice.AGE_OLDER_ADULT:
                return "OlderAdult";
            case Voice.AGE_TEENAGER:
                return "Teenager";
            case Voice.AGE_YOUNGER_ADULT:
                return "Younger Adult";
            case Voice.AGE_DONT_CARE:
            default:
                return "Unknown";
        }
    }

    /**
     * Print all the properties of the selected synthesizer
     */
    private void printSelectedSynthesizerModeDesc() {
        EngineModeDesc description = this.synthesizer.getEngineModeDesc();
        System.out.println();
        System.out.println("Selected Synthesizer:"+description.getEngineName());
        System.out.println("         Mode:"+description.getModeName());
        System.out.println("         Locale:"+description.getLocale());
        System.out.println("         IsRunning:"+description.getRunning());
        System.out.println();
    }

    /**
     * List all the available synthesizers and voices.
     */
    public void listAllVoices() {
        System.out.println();
        System.out.println("All available JSAPI Synthesizers and Voices:");

        //Do not set any properties so all the synthesizers will be returned
        SynthesizerModeDesc emptyDesc = new SynthesizerModeDesc();
        EngineList engineList = Central.availableSynthesizers(emptyDesc);
        //loop over all the synthesizers
        for (int e = 0; e < engineList.size(); e++) {
            SynthesizerModeDesc desc = (SynthesizerModeDesc) engineList.get(e);
            //loop over all the voices for this synthesizer
            Voice[] voices = desc.getVoices();
            for (int v = 0; v < voices.length; v++) {
                System.out.println(
                        desc.getEngineName()+
                                "  Voice:"+voices[v].getName()+
                                " Gender:"+genderToString(voices[v].getGender()));
            }
        }
        System.out.println();
    }
}