/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.controllers;

import org.ahn.recserver.exceptions.DatabaseUpdateException;
import org.ahn.recserver.exceptions.ResourceNotFoundException;
import org.ahn.recserver.exceptions.SetStillActiveException;
import org.ahn.recserver.helpers.AccountGenerator;
import org.ahn.recserver.helpers.SurveyServlet;
import org.ahn.recserver.resources.NewUser;
import org.ahn.recserver.resources.Options;
import org.ahn.recserver.resources.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author rgustafs
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("@oauth2.hasScope('write'")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AdminController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountGenerator accountGenerator;

    @Autowired
    private SurveyServlet surveyServlet;

    /* ~~ Survey calls ~~ */
    @GetMapping(value = "/surveys")
    public Options[] loadOptions() {
        return surveyServlet.getAllOptions();
    }

    @GetMapping(value = "/surveys/{SurveyID}")
    public Question[] loadSurvey(@PathVariable int SurveyID) {

        Question[] q;
        if ((q = surveyServlet.getQuestionSet(SurveyID)) == null) {
            throw new ResourceNotFoundException();
        }

        return q;
    }

    /* ~~ Authorization calls ~~ */
    @RequestMapping(value = "auth/new", method = POST)
    public NewUser register(@PathVariable int PatientID) {

        String Username, Password;

        Username = accountGenerator.generateUsername();
        Password = accountGenerator.generatePassword();

        if (jdbcTemplate.update(
                "INSERT ID, Username, Password, BadAttempts "
                + "INTO Auth VALUES(?, ?, ?, ?)",
                PatientID, Username, Password, -1) != 1) {
            throw new DatabaseUpdateException();
        }

        return new NewUser(PatientID, Username, Password);
    }

    /* ~~ Set Calls ~~ */
    @RequestMapping(value = "/set/new", method = POST)
    public Integer register(@PathVariable Integer PatientID,
            @PathVariable Integer SurveyID) {

        Integer SetID;
        KeyHolder insertedKey;

        if (jdbcTemplate.queryForObject(
                "SELECT ID FROM SetMap WHERE Auth = ? AND Active = ?",
                Integer.class,
                PatientID, true) != null) {
            throw new SetStillActiveException();
        }

        insertedKey = new GeneratedKeyHolder();

        if (jdbcTemplate.update((Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT Survey INTO SubmittedSet VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, SurveyID);
            return ps;
        }, insertedKey) != 1) {
            throw new DatabaseUpdateException();
        }

        SetID = insertedKey.getKey().intValue();

        if (jdbcTemplate.update(
                "INSERT ID, Auth INTO SetMap VALUES (?, ?)",
                SetID, PatientID) != 1) {
            throw new DatabaseUpdateException();
        }

        return SetID;
    }

}
