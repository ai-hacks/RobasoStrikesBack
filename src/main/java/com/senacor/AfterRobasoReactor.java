package com.senacor;

import edu.cmu.sphinx.api.SpeechResult;

import java.util.Map;

public class AfterRobasoReactor implements ReactToAnswer {
    private final SpeechRecognizer afterRobasoRecognizer;

    private final Map<String, String> abbrevationMap;


    public AfterRobasoReactor(SpeechRecognizer afterRobasoRecognizer) {
        this.afterRobasoRecognizer = afterRobasoRecognizer;
        abbrevationMap = new AbbreviationImporter().getAbbrevationMap("baa.csv");
    }


    @Override
    public Reaction reactTo(SpeechResult answer) {
        String hypothesis = answer.getHypothesis();
        if (StateChangeDemo.UNKOWN_HYPOTHESIS.equals(hypothesis)) {
            return new Reaction(this, "Was");
        }

        if ("robaso".equals(hypothesis)) {
            return new Reaction(this, "jawohl");
        }

        String utterance = answer.getHypothesis().trim();
        System.out.println(utterance);
        String nodefniere = utterance.replaceAll("definiere", "");
        String nospaces = nodefniere.replaceAll(" ", "");
        String acro = abbrevationMap.get(nospaces);
        if (acro != null) {
            return new Reaction(new DialogReactor(afterRobasoRecognizer), nodefniere + " bedeutet " + acro);
        }

        System.out.println("#### undefined #### '" + nodefniere + '\'');
        return new Reaction(new DialogReactor(afterRobasoRecognizer), "Ich kann " + nodefniere + " nicht definieren");
    }

    @Override
    public ResultProducer produceResults() {
        return afterRobasoRecognizer.startRecognition();
    }
}
