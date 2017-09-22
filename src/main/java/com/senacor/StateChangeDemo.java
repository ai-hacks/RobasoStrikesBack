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
import java.util.Objects;
import java.util.Optional;


public class StateChangeDemo {
    public static final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false);
    public static final String UNKOWN_HYPOTHESIS = "<unk>";


    public static void main(String[] args) throws Exception {
        TargetDataLine mic = getMicLine();
        mic.open();

        SpeechRecognizer dialog = SpeechRecognizer.fromGrammar("dialog", "resource:/dictionaries/alphabet_de.dic");
        System.out.println("recognizer initialized");
        Player.playAndBlockUntilFinished(Sounds.LIGHTSABER_ON);

        ReactToAnswer reactor = new DialogReactor(dialog);
        while (true) {
            ResultProducer rp = reactor.produceResults();
            Reaction react;
            try (Microphone m = new Microphone(mic)) {
                Optional<SpeechResult> result = rp.getResult(m.getStream());
                react = result.map(reactor::reactTo).orElse(new EmptyReaction());
            }
            if (!(react instanceof EmptyReaction)) {
                Player.playAndBlockUntilFinished(new TextToSpeech(react.getResponse()));
                reactor = react.getNextReactor();
            }
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
