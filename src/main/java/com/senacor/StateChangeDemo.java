/*
 * Copyright 2013 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

package com.senacor;

import edu.cmu.sphinx.api.SpeechResult;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


public class StateChangeDemo {
    public static final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false);
    public static final String UNKOWN_HYPOTHESIS = "<unk>";


    public static void main(String[] args) throws Exception {
        TargetDataLine mic = getMicLine();
        mic.open();
        TextToSpeech tts = new TextToSpeech();

        ReactToAnswer reactor = new DialogReactor(SpeechRecognizer.fromGrammar("dialog"));
        try (ResultProducer rp = reactor.produceResults()) {
            while (true) {
                System.out.println("recognizer regonizer");
                System.out.println("recognizer initialized");
                Reaction react;
                try (Microphone m = new Microphone(mic)) {
                    System.out.println("getting results");
                    beep();
                    Optional<SpeechResult> result = rp.getResult(m.getStream());
                    System.out.println("got result");
                    react = result.map(reactor::reactTo).orElse(new Reaction(reactor, "du hast nichts geantwortet"));
                }
                System.out.println("try to speak");
                tts.speakAndBlockUntilFinished(react.getResponse());
                System.out.println("spoken");
                reactor = react.getNextReactor();
                System.out.println("closing recognizer");
            }
//            System.out.println("recognizer closed");
        }
    }

    public static void beep() {
        try(InputStream beepwav = ClassLoader.getSystemResourceAsStream("beep_lo.wav")) {
            if(beepwav == null) {
                throw new RuntimeException("could not find beep wav");
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = beepwav.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] bytes = buffer.toByteArray();

            AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes));
            try(Clip clip = AudioSystem.getClip()) {
                clip.open(stream);
                TextToSpeech.BlockUntilEndListener blocker = new TextToSpeech.BlockUntilEndListener();
                clip.addLineListener(blocker);
                clip.start();
                blocker.waitUntilDone();
            }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private static TargetDataLine getMicLine() {
        try {
            return AudioSystem.getTargetDataLine(DEFAULT_AUDIO_FORMAT);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

}
