package com.senacor;

public class Reaction {
    private final ReactToAnswer nextReactor;
    private final Sound response;

    public Reaction(ReactToAnswer nextReactor) {
        this(nextReactor, Sounds.NO_SOUND);
    }
    public Reaction(ReactToAnswer nextReactor, String text) {
        this(nextReactor, new TextToSpeech(text));
    }

    public Reaction(ReactToAnswer nextReactor, Sound response) {
        this.nextReactor = nextReactor;
        this.response = response;
    }

    public ReactToAnswer getNextReactor() {
        return nextReactor;
    }

    public Sound getResponse() {
        return response;
    }
}
