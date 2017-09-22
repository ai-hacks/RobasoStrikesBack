package com.senacor;

import edu.cmu.sphinx.api.SpeechResult;

import java.util.Map;

class DialogReactor implements ReactToAnswer {
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
            return new Reaction(new AfterRobasoReactor(dialogRecognizer), "jawohl");
        }


        return null;
    }

    @Override
    public ResultProducer produceResults() {
        return dialogRecognizer.startRecognition();
    }
}
