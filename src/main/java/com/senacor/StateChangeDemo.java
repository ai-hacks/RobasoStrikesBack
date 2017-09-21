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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class StateChangeDemo {
    public static final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false);
    public static final String UNKOWN_HYPOTHESIS = "<unk>";


    public static void main(String[] args) throws Exception {
        TargetDataLine mic = getMicLine();
        mic.open();
        TextToSpeech tts = new TextToSpeech();

        ReactToAnswer reactor = new DialogReactor(SpeechRecognizer.fromGrammar("dialog"));
        while (true) {
            System.out.println("recognizer regonizer");
            try (ResultProducer rp = reactor.produceResults()) {
                System.out.println("recognizer initialized");
                Reaction react;
                try (Microphone m = new Microphone(mic)) {
                    System.out.println("getting results");
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
            System.out.println("recognizer closed");
        }
    }

    private static class DialogReactor implements ReactToAnswer {
        private final SpeechRecognizer dialogRecognizer;

        public DialogReactor(SpeechRecognizer dialogRecognizer) {
            this.dialogRecognizer = dialogRecognizer;
        }

        @Override
        public Reaction reactTo(SpeechResult answer) {
            String hypothesis = answer.getHypothesis();
            if(UNKOWN_HYPOTHESIS.equals(hypothesis)){
                return new Reaction(this, "Waas");
            }
            return new Reaction(this, "Du möchtest: " + hypothesis);
        }

        @Override
        public ResultProducer produceResults() {
            return dialogRecognizer.startRecognition();
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
