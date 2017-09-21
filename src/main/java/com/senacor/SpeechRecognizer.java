package com.senacor;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Context;

import java.io.IOException;

public class SpeechRecognizer {
    private static final String ACOUSTIC_MODEL =
            "resource:/edu/cmu/sphinx/models/model_de";
    private static final String DICTIONARY_PATH =
            "resource:/edu/cmu/sphinx/models/dict_de/alphabet.dic";
    private static final String GRAMMAR_PATH =
            "resource:/edu/cmu/sphinx/demo/dialog/";
    private static final String LANGUAGE_MODEL =
            "resource:/edu/cmu/sphinx/demo/dialog/lm_klein.lm";

    private final Context context;

    public static SpeechRecognizer fromGrammar(String name) {
        Configuration configuration = getBaseConfiguration();
        configuration.setUseGrammar(true);
        configuration.setGrammarName(name);
        return buildRecognizer(configuration);
    }

    public static SpeechRecognizer fromLanguageModel(String path) {
        Configuration configuration = getBaseConfiguration();
        configuration.setLanguageModelPath(path);
        return buildRecognizer(configuration);
    }

    private static SpeechRecognizer buildRecognizer(Configuration configuration) {
        try {
            return new SpeechRecognizer(new Context(configuration));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Configuration getBaseConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        return configuration;
    }

    private SpeechRecognizer(Context context) throws IOException {
        this.context = context;
    }

    public ResultProducer startRecognition() {
        return new ResultProducer(context);
    }

}
