package com.senacor;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;
import java.util.Locale;

public class SpeechTest {

    public static void main(String ...args) throws Exception {
        new TextToSpeech().speakAndBlockUntilFinished("Ich programmier dann mal weiter.");

    }
}
