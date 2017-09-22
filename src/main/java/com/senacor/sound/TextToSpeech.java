package com.senacor.sound;

import com.senacor.sound.Sound;
import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

import javax.sound.sampled.AudioInputStream;
import java.util.Locale;

public class TextToSpeech implements Sound {
    private final LocalMaryInterface mary;
    private final String text;

    private enum Voices {
        PAVOQUE_NN("dfki-pavoque-neutral2"),
        PAVOQUE("dfki-pavoque-neutral-hsmm2");

        private final String voiceName;

        Voices(String voiceName) {
            this.voiceName = voiceName;
        }
    }

    public enum Effect {
        RATE("Rate"),
        ROBOT("Robot"),
        STADIUM("Stadium"),
        WHISPER("Whisper"),
        JETPILOT("JetPilot");

        Effect(String effectName) {
            this.effectName = effectName;
        }

        private final String effectName;
    }

    public TextToSpeech(String text, Effect eff) {
        this(text);
        mary.setAudioEffects(eff.effectName);
    }

    public TextToSpeech(String text) {
        this.text = text;
        try {
            mary = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }

        for (Voices voice : Voices.values()) {
            try {
                mary.setVoice(voice.voiceName);
            } catch (IllegalArgumentException ex) {
            }
        }

        try {
            mary.setLocale(Locale.GERMAN);
        } catch (IllegalArgumentException ex) {
        }
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
