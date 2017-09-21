package com.senacor;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;
import java.io.InputStream;

public class Microphone implements AutoCloseable {
    private final TargetDataLine line;

    public Microphone(TargetDataLine line) {
        this.line = line;
        line.start();
    }

    public InputStream getStream() {
        return new AudioInputStream(line);
    }

    @Override
    public void close() throws Exception {
        line.stop();
    }
}
