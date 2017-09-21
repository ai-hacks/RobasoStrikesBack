package com.senacor;

import edu.cmu.sphinx.api.SpeechResult;

public interface ReactToAnswer {
    Reaction reactTo(SpeechResult answer);
    ResultProducer produceResults();
}
