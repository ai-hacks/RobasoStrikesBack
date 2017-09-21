package com.senacor;

public class Reaction {
    private final ReactToAnswer nextReactor;
    private final String response;

    public Reaction(ReactToAnswer nextReactor, String response) {
        this.nextReactor = nextReactor;
        this.response = response;
    }

    public ReactToAnswer getNextReactor() {
        return nextReactor;
    }

    public String getResponse() {
        return response;
    }
}
