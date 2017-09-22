package com.senacor;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

import javax.sound.sampled.*;
import java.io.IOException;

public class TextToSpeech implements Sound {
    private final LocalMaryInterface mary;
    private final String text;

    public TextToSpeech(String text) {
        this.text = text;
        try {
            mary = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
        mary.setVoice("dfki-pavoque-neutral");
    }

    @Override
    public AudioInputStream getStream() {
        try {
            return mary.generateAudio(text);
        } catch (SynthesisException e) {
            throw new RuntimeException(e);
        }
    }
}
