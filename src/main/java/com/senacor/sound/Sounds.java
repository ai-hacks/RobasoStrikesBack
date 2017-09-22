package com.senacor.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public enum Sounds implements Sound {
    BEEP_LO("beep_lo.wav"),
    BEEP_HI("beep_hi.wav"),
    LIGHTSABER_ON("lightsaber-turn-on.wav"),
    NO_SOUND("empty.wav");

    private final byte[] data;

    Sounds(String wavFile) {
        try (InputStream wav = ClassLoader.getSystemResourceAsStream(wavFile)) {
            if (wav == null) {
                throw new RuntimeException("could not find beep wav");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int nRead;
            byte[] buffer = new byte[16384];

            while ((nRead = wav.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, nRead);
            }

            out.flush();
            data = out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AudioInputStream getStream() {
        try {
            return AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
