package com.senacor;

import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;

import javax.management.relation.RoleUnresolved;
import java.io.InputStream;
import java.util.Optional;

public class ResultProducer implements AutoCloseable {
    private final Recognizer recognizer;
    private final StreamDataSource dataSource;

    public ResultProducer(Recognizer recognizer, StreamDataSource dataSource) {
        this.recognizer = recognizer;
        this.dataSource = dataSource;
    }

    public Optional<SpeechResult> getResult(InputStream input){
        dataSource.setInputStream(input);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Result result = recognizer.recognize();
        return Optional.ofNullable(result).map(SpeechResult::new);
    }

    @Override
    public void close() throws Exception {
        recognizer.deallocate();
    }
}
