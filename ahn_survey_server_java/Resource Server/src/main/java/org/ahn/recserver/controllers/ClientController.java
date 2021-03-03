/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.controllers;

import org.ahn.recserver.resources.Question;
import org.ahn.recserver.helpers.AccountGenerator;
import org.ahn.recserver.helpers.SurveyServlet;
import org.ahn.recserver.exceptions.DatabaseUpdateException;
import org.ahn.recserver.exceptions.InvalidAccessException;
import org.ahn.recserver.exceptions.SetTerminationException;
import org.ahn.recserver.exceptions.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author rgustafs
 */
@RestController
@RequestMapping("/api/client")
@PreAuthorize("@oauth2.hasScope('read')")
public class ClientController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ClientController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountGenerator accountGenerator;

    @Autowired
    private SurveyServlet surveyServlet;

    /* ~~ Survey calls ~~ */
    @RequestMapping(value = "/surveys/{SurveyID}", method = GET)
    @ResponseBody
    public Question[] loadSurvey(@PathVariable int SurveyID) {

        Question[] q;
        if ((q = surveyServlet.getQuestionSet(SurveyID)) == null) {
            throw new ResourceNotFoundException();
        }

        return q;
    }

    /* ~~ Set calls ~~ */
    @RequestMapping(value = "/set", method = GET)
    @ResponseBody
    public Integer currentSet() {

        Integer PatientID, SetID;

        PatientID = 0; // TODO Fix

        if ((SetID = jdbcTemplate.queryForObject(
                "SELECT ID FROM SetMap WHERE Auth = ? AND Active = ?",
                Integer.class,
                PatientID, true)) == null) {
            throw new InvalidAccessException();
        }

        return SetID;
    }

    @RequestMapping(value = "/set/submit", method = POST)
    public Integer submit(@PathVariable Integer SetID,
            @PathVariable Timestamp Started, @PathVariable Timestamp Ended,
            @PathVariable Integer[] Answers) {

        Integer PatientID, SubmittedID;
        KeyHolder lastInsertedKey;

        PatientID = 0;  // TODO Fix

        if (jdbcTemplate.queryForObject(
                "SELECT ID FROM SetMap WHERE Auth = ? AND Active = ?",
                Integer.class,
                PatientID, true) != null) {
            throw new InvalidAccessException();
        }

        lastInsertedKey = new GeneratedKeyHolder();

        if (jdbcTemplate.update((Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT SubmittedSetID, Started, Ended "
                    + "INTO Submitted VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, SetID);
            ps.setTimestamp(2, Started);
            ps.setTimestamp(3, Ended);
            return ps;
        }, lastInsertedKey) != 1) {
            throw new DatabaseUpdateException();
        }

        SubmittedID = lastInsertedKey.getKey().intValue();

        for (int numUpdatedRows : jdbcTemplate.batchUpdate(
                "INSERT Submitted, SubmittedSet, Question, Value "
                + "INTO SubmittedAnswers VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setInt(1, SubmittedID);
                ps.setInt(2, SetID);
                ps.setInt(3, i);
                ps.setInt(4, Answers[i]);
            }

            @Override
            public int getBatchSize() {
                return Answers.length;
            }
        })) {
            if (numUpdatedRows != 1) {
                throw new DatabaseUpdateException();
            }
        }

        return SubmittedID;
    }

    @RequestMapping(value = "/set/done")
    public void terminate(@PathVariable Date Ended) {

        Integer PatientID, SetID;

        PatientID = 0; // TODO Fix

        if ((SetID = jdbcTemplate.queryForObject(
                "SELECT ID FROM SetMap WHERE Auth = ? AND Active = ?",
                Integer.class,
                PatientID, true)) != null) {
            throw new SetTerminationException();
        }

        if (jdbcTemplate.update(
                "UPDATE SetMap SET Valid=? WHERE ID=?",
                false, SetID) != 1) {
            throw new DatabaseUpdateException();
        }

        if (jdbcTemplate.update(
                "UPDATE SubmittedSet SET Ended=? WHERE ID=?",
                (Ended == null)
                        ? new java.sql.Date(System.currentTimeMillis())
                        : Ended,
                SetID) != 1) {
            throw new DatabaseUpdateException();
        }
    }
}
