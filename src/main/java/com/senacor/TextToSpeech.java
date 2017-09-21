package com.senacor;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

import javax.sound.sampled.*;
import java.io.IOException;

public class TextToSpeech {
    private final LocalMaryInterface mary;

    public TextToSpeech() {
        try {
            mary = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
        mary.setVoice("dfki-pavoque-neutral-hsmm");
    }

    public void speakAndBlockUntilFinished(String text) {
        Clip speech = prepareSpeech(text);
        BlockUntilEndListener listener = new BlockUntilEndListener();
        speech.addLineListener(listener);
        speech.start();
        listener.waitUntilDone();
    }

    public Clip prepareSpeech(String text) {
        try {
            AudioInputStream audio = mary.generateAudio(text);
            Clip clip = AudioSystem.getClip();
            BlockUntilEndListener listener = new BlockUntilEndListener();
            clip.open(audio);
            return clip;
        } catch (LineUnavailableException | SynthesisException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class BlockUntilEndListener implements LineListener {
        private boolean done = false;

        @Override
        public synchronized void update(LineEvent event) {
            LineEvent.Type eventType = event.getType();
            if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                done = true;
                notifyAll();
            }
        }

        public synchronized void waitUntilDone() {
            while (!done) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
