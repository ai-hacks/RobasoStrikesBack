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

import java.util.HashMap;
import java.util.Map;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;


public class DialogDemo {

    private static final String ACOUSTIC_MODEL =
            "resource:/edu/cmu/sphinx/models/model_de";
    private static final String DICTIONARY_PATH =
            "resource:/edu/cmu/sphinx/models/dict_de/cmusphinx-voxforge-de.dic";
    private static final String GRAMMAR_PATH =
            "resource:/edu/cmu/sphinx/demo/dialog/";
    private static final String LANGUAGE_MODEL =
            "resource:/edu/cmu/sphinx/demo/dialog/lm_klein.lm";

    private static final Map<String, Integer> DIGITS =
            new HashMap<String, Integer>();

    static {
        DIGITS.put("oh", 0);
        DIGITS.put("zero", 0);
        DIGITS.put("one", 1);
        DIGITS.put("two", 2);
        DIGITS.put("three", 3);
        DIGITS.put("four", 4);
        DIGITS.put("five", 5);
        DIGITS.put("six", 6);
        DIGITS.put("seven", 7);
        DIGITS.put("eight", 8);
        DIGITS.put("nine", 9);
    }


    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        configuration.setUseGrammar(true);

        configuration.setGrammarName("dialog");
        LiveSpeechRecognizer lmRecognizer =
                new LiveSpeechRecognizer(configuration);

        lmRecognizer.startRecognition(true);
        while (true) {

            String utterance = lmRecognizer.getResult().getHypothesis();
            System.out.println(utterance);

            if (utterance.startsWith("exit"))
                break;

            if (utterance.endsWith("weather forecast")) {
                lmRecognizer.stopRecognition();
                lmRecognizer.startRecognition(true);
            }
        }

        lmRecognizer.stopRecognition();
    }
}
