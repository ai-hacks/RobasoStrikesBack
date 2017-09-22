package com.senacor.system;

import com.senacor.system.Reaction;
import com.senacor.system.ResultProducer;
import edu.cmu.sphinx.api.SpeechResult;

public interface ReactToAnswer {
    Reaction reactTo(SpeechResult answer);
    ResultProducer produceResults();
}
