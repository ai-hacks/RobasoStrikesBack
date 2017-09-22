package com.senacor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Only needed for an inital grammar setup
 */
public class GrammarSetup {

    private File file;
    private PrintWriter pw;

    GrammarSetup(String filepath) {
        file = new File(filepath);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pw = new PrintWriter(fw);
    }

    public void addSentence(String s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            builder.append(c);
            builder.append(" ");
        }
        builder.append("|");
        System.out.println(builder.toString());
        pw.println(builder.toString());

    }

}
