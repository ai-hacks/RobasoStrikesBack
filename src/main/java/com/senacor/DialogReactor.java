package com.senacor;

import edu.cmu.sphinx.api.SpeechResult;

import java.util.Map;

class DialogReactor implements ReactToAnswer {
    private final SpeechRecognizer dialogRecognizer;

    private final Map<String, String> abbrevationMap;

    public DialogReactor(SpeechRecognizer dialogRecognizer) {
        this.dialogRecognizer = dialogRecognizer;
        abbrevationMap = new AbbreviationImporter().getAbbrevationMap("baa.csv");
    }

    @Override
    public Reaction reactTo(SpeechResult answer) {
        String hypothesis = answer.getHypothesis();
        if (StateChangeDemo.UNKOWN_HYPOTHESIS.equals(hypothesis)) {
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
