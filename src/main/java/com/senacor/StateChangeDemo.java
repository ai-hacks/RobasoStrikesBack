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
import java.io.IOException;
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

    private static class DialogReactor implements ReactToAnswer {
        private final SpeechRecognizer dialogRecognizer;

        private final Map<String, String> abbrevationMap;

        public DialogReactor(SpeechRecognizer dialogRecognizer) {
            this.dialogRecognizer = dialogRecognizer;
            abbrevationMap = new AbbreviationImporter().getAbbrevationMap("baa.csv");
        }

        @Override
        public Reaction reactTo(SpeechResult answer) {
            String hypothesis = answer.getHypothesis();
            if (UNKOWN_HYPOTHESIS.equals(hypothesis)) {
                return new Reaction(this, "Was");
            }

            String utterance = answer.getHypothesis().trim();
            System.out.println(utterance);
            String nospaces = utterance.replaceAll(" ", "");
            String acro = abbrevationMap.get(nospaces);
            if (acro != null) {
                return new Reaction(this, utterance + " bedeutet " + acro);
            }

            System.out.println("#### undefined #### '" + utterance + '\'');
            return new Reaction(this, "Ich kann " + utterance + " nicht definieren");
        }

        @Override
        public ResultProducer produceResults() {
            return dialogRecognizer.startRecognition();
        }
    }

    public static void beep() {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResourceAsStream("beep_lo.wav"));
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            TextToSpeech.BlockUntilEndListener blocker = new TextToSpeech.BlockUntilEndListener();
            clip.addLineListener(blocker);
            clip.open(stream);
            clip.start();
            blocker.waitUntilDone();
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
