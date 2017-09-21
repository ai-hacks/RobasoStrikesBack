package com.senacor;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AbbreviationImporter {

    public Map<String, String> getAbbrevationMap(String fileName) {

        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(fileName);
        if (systemResourceAsStream == null) {
            throw new RuntimeException("Could not find " + fileName);
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(systemResourceAsStream))) {

            Map<String, String> map = new HashMap<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(";");
                if (split.length > 1) {
                    String split0 = split[0];
                    String split1 = split[1];
                    split0 = removeNonalpha(split0);
                    if (StringUtils.isNotBlank(split0) && StringUtils.isNotBlank(split1)) {
                        map.put(split0, split1);
                    }
                }

            }

            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupGrammar(String s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            builder.append(c);
            builder.append(" ");
        }
        builder.append("|");
        System.out.println(builder.toString());
    }

    private String removeNonalpha(String split) {
        split = split.toLowerCase();
        split = split.replace("ü", "u");
        split = split.replace("ö", "o");
        split = split.replace("ä", "a");
        split = split.replaceAll("[^a-z]", "");
        return split;
    }
}

