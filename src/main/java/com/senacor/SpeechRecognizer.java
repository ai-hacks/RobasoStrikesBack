package com.senacor;

import com.senacor.system.ResultProducer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;

import java.io.IOException;

public class SpeechRecognizer {
    private static final String ACOUSTIC_MODEL =
            "resource:/edu/cmu/sphinx/models/model_de";
    private static final String GRAMMAR_PATH =
            "resource:/grammatiken/";
//    private static final String LANGUAGE_MODEL =
//            "resource:/edu/cmu/sphinx/demo/dialog/lm_klein.lm";

    private final Context context;

    public static SpeechRecognizer fromGrammar(String name, String dictionary) {
        Configuration configuration = getBaseConfiguration(dictionary);
        configuration.setUseGrammar(true);
        configuration.setGrammarName(name);
        return buildRecognizer(configuration);
    }

//    public static SpeechRecognizer fromLanguageModel(String path) {
//        Configuration configuration = getBaseConfiguration(dictionary);
//        configuration.setLanguageModelPath(path);
//        return buildRecognizer(configuration);
//    }

    private static SpeechRecognizer buildRecognizer(Configuration configuration) {
        try {

            return new SpeechRecognizer(new Context("resource:/config.xml", configuration));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Configuration getBaseConfiguration(String dictionary) {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(dictionary);
        configuration.setGrammarPath(GRAMMAR_PATH);
        return configuration;
    }

    private SpeechRecognizer(Context context) throws IOException {
        this.context = context;
        getRecognizer().allocate();
    }

    public ResultProducer startRecognition() {
        return new ResultProducer(getRecognizer(), getDataSource());
    }

    private Recognizer getRecognizer() {
        return context.getInstance(Recognizer.class);
    }

    public StreamDataSource getDataSource() {
        return context.getInstance(StreamDataSource.class);
    }
}
