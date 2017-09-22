package com.senacor.reactors;

import com.senacor.*;
import com.senacor.sound.TextToSpeech;
import com.senacor.system.ReactToAnswer;
import com.senacor.system.Reaction;
import com.senacor.system.ResultProducer;
import edu.cmu.sphinx.api.SpeechResult;

public class DialogReactor implements ReactToAnswer {
    private final SpeechRecognizer dialogRecognizer;


    public DialogReactor(SpeechRecognizer dialogRecognizer) {
        this.dialogRecognizer = dialogRecognizer;
    }


    @Override
    public Reaction reactTo(SpeechResult answer) {
        String hypothesis = answer.getHypothesis();

        if ("sau geil".equals(hypothesis)) {
            return new Reaction(this, "hallo roland");
        }

        if ("robaso".equals(hypothesis)) {
            return new Reaction(new AfterRobasoReactor(dialogRecognizer), new TextToSpeech("ja Chef");
        }


        return null;
    }

    @Override
    public ResultProducer produceResults() {
        return dialogRecognizer.startRecognition();
    }
}
