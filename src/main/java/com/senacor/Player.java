package com.senacor;

import javax.sound.sampled.*;
import java.io.IOException;

public class Player {

    public static void playAndBlockUntilFinished(Sound sound) {
        Clip speech = prepareClip(sound);
        BlockUntilEndListener listener = new BlockUntilEndListener();
        speech.addLineListener(listener);
        speech.start();
        listener.waitUntilDone();
    }

    public static Clip prepareClip(Sound sound) {
        try {
            AudioInputStream audio = sound.getStream();
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class BlockUntilEndListener implements LineListener {
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
