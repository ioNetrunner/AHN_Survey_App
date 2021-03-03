/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.helpers;

import org.ahn.recserver.resources.Options;
import org.ahn.recserver.resources.Question;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rgustafs
 */
public class SurveyServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyServlet.class);

    private final HashMap<Integer, Options> options;
    private final HashMap<Integer, Question[]> questions;

    public SurveyServlet(String dir) throws IOException {
        this.options = new HashMap<>();
        this.questions = new HashMap<>();

        File[] files = new File(dir).listFiles();
        for (File file : files) {
            add(file.getPath());
        }
    }

    public Options[] getAllOptions() {
        Options[] ret = (Options[]) options.values().toArray();
        Arrays.sort(ret, (a, b) -> Integer.compare(a.getID(), b.getID()));
        return ret;
    }

    public Options getOptions(Integer ID) {
        return options.get(ID);
    }

    public Question[] getQuestionSet(Integer ID) {
        return questions.get(ID);
    }

    public Integer getQuestionCount(Integer ID) {
        return questions.get(ID).length;
    }

    /**
     * Takes path, constructs resources, adds to data structures
     *
     * @param path
     * @throws IOException
     */
    private Integer add(String path) throws IOException {

        // "COPD_AssessmentTest_en-US_1.json" ->
        // {"COPD", "Assessment Test", "en-US", "1"}
        String[] parts;
        Integer ID;

        parts = jsonSplit(path);
        ID = Integer.parseInt(parts[4]);

        this.options.put(ID, new Options(
                ID,
                parts[0] + ' ' + parts[1],
                parts[2],
                Integer.parseInt(parts[3])
        ));

        this.questions.put(ID, parseQuestions(path));

        return ID;
    }

    /* Messy string / json helpers */
    /**
     * Un-formats JSON filename
     *
     * @param path to format
     * @return list of string parts
     */
    private static String[] jsonSplit(String path) {
        return path.substring(
                0, path.lastIndexOf(".")
        ).replaceAll(
                "(\\p{Ll})(\\p{Lu})", "$1 $2"
        ).split("-");
    }

    /**
     * Turns JSON file into object array
     *
     * @param path of JSON file
     * @return list of SurveyQuestion objects
     * @throws IOException
     */
    private Question[] parseQuestions(String path) throws IOException {

        JSONArray json;
        JSONObject obj;
        ArrayList<Question> list;

        try {
            // File to string
            String JSON = new String(Files.readAllBytes(Paths.get(path)), UTF_8);
            // String to json array
            json = new JSONArray(JSON);
        } catch (IOException ex) {
            LOGGER.debug("Error reading JSON file:{}", ex);
            throw ex;
        }

        list = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {

            obj = json.getJSONObject(i);

            list.add(new Question(
                    obj.getString("low_text"),
                    obj.getString("high_text"),
                    obj.getInt("minimum"),
                    obj.getInt("maximum"),
                    obj.getInt("interval")
            ));
        }

        return list.toArray(new Question[list.size()]);
    }
}
