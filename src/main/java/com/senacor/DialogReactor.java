package com.senacor;

import edu.cmu.sphinx.api.SpeechResult;

import java.util.Map;

class DialogReactor implements ReactToAnswer {
    private final SpeechRecognizer dialogRecognizer;

    private final Map<String, String> abbrevationMap;
    private boolean isRobaso = false;


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

        if ("sau geil".equals(hypothesis)) {
            return new Reaction(this, "hallo roland");
        }


        if (!isRobaso && "robaso".equals(hypothesis)) {
            isRobaso = true;
            return new Reaction(this, "jawohl");
        }

        String utterance = answer.getHypothesis().trim();
        System.out.println(utterance);
        String nodefniere = utterance.replaceAll("definiere", "");
        String nospaces = nodefniere.replaceAll(" ", "");
        String acro = abbrevationMap.get(nospaces);
        if (acro != null) {
            isRobaso = false;
            return new Reaction(this, nodefniere + " bedeutet " + acro);
        }

        isRobaso = false;
        System.out.println("#### undefined #### '" + nodefniere + '\'');
        return new Reaction(this, "Ich kann " + nodefniere + " nicht definieren");
    }

    @Override
    public ResultProducer produceResults() {
        return dialogRecognizer.startRecognition();
    }
}
