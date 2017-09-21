package com.senacor;

import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;

import java.io.InputStream;
import java.util.Optional;

public class ResultProducer implements AutoCloseable {
    private final Recognizer recognizer;
    private final Context context;

    ResultProducer(Context context) {
        this.context = context;
        recognizer = context.getInstance(Recognizer.class);
        recognizer.allocate();
    }

    public Optional<SpeechResult> getResult(InputStream input){
        context.getInstance(StreamDataSource.class)
                .setInputStream(input);
        Result result = recognizer.recognize();
        return Optional.ofNullable(result).map(SpeechResult::new);
    }

    @Override
    public void close() throws Exception {
        recognizer.deallocate();
    }
}
