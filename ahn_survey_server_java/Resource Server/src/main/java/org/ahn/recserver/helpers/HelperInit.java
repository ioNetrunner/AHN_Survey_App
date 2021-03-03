/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.helpers;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Starts functions for
 *
 * @author rgustafs
 */
@Component
public class HelperInit implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelperInit.class);

    @Autowired
    private AccountGenerator accountGenerator;

    @Autowired
    private SurveyServlet surveyServlet;

    @Override
    public void run(String... strings) throws Exception {

        LOGGER.info("Getting valid usernames");

        accountGenerator = new AccountGenerator(
                Files.readAllLines(Paths.get("../Dictionaries/newNouns.txt"), UTF_8),
                Files.readAllLines(Paths.get("../Dictionaries/newAdj.txt"), UTF_8),
                Files.readAllLines(Paths.get("../Dictionaries/newAnimals.txt"), UTF_8)
        );

        LOGGER.info("Getting surveys");

        surveyServlet = new SurveyServlet("./Surveys");

    }
}
