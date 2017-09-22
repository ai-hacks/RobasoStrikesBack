package com.senacor.reactors;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

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
                    if (split0.length() < 6) {
                        if (StringUtils.isNotBlank(split0) && StringUtils.isNotBlank(split1)) {
                            map.put(split0, split1);
                        }
                    }
                }

            }

            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

