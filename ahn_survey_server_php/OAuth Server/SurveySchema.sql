/* 
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

/**
 * Author:  rgustafs
 * Created: Aug 3, 2017
 */

CREATE DATABASE IF NOT EXISTS SurveyOAuth; -- Should be schema, not entire DB

-- ------------------------------------------------------------------
-- OAuth
-- ------------------------------------------------------------------

CREATE TABLE OAuth2_Users (
    User_ID     int PRIMARY KEY AUTO_INCREMENT,
    Identifier  int,
    Username    varchar(20) NOT NULL,
    Password    varchar(255) NOT NULL,
    Attempts    tinyint NOT NULL DEFAULT 0,
    UNIQUE(Username)
);

CREATE TABLE IF NOT EXISTS OAuth2_Client (

);

CREATE TABLE IF NOT EXISTS AccessToken (

);

CREATE TABLE IF NOT EXISTS AuthCode (

);

CREATE TABLE IF NOT EXISTS AuthCode (

);

CREATE DATABASE IF NOT EXISTS SurveyData;

-- ------------------------------------------------------------------
-- Survey
-- ------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Survey (
    Survey_ID   tinyint NOT NULL AUTO_INCREMENT,
    Label       varchar(40) NOT NULL,
    Lang        varchar(6) NOT NULL,
    Version     tinyint NOT NULL DEFAULT 1,
    Questions   tinyint NOT NULL,
    UNIQUE (Label, Version)
);

INSERT Label, Version, Questions INTO Survey
VALUES ('COPD Assessment Test', 1, 8);

SET @CopdID = LAST_INSERT_ID();

CREATE TABLE IF NOT EXISTS Question (
    Question_ID smallint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Survey_ID   tinyint FOREIGN KEY REFERENCES Survey(Survey_ID),
    Low_Text    varchar(255) NOT NULL,
    High_Text   varchar(255),
    Minimum     tinyint NOT NULL DEFAULT 0,
    Maximum     tinyint NOT NULL DEFAULT 5,
    Inter       tinyint NOT NULL DEFAULT 1,
    UNIQUE (Survey_ID, Low_Text, High_Text)
);

INSERT IGNORE
    Question_ID, Survey_ID, Low_Text, High_Text, Minimum, Maximum, Inter 
INTO Question
VALUES 
    (1, @CopdID, 'I never cough.' 'I cough all the time.', 0, 5, 1),
    (2, @CopdID, 'I have no phlegm (mucus) in my chest at all.', 'My chest is completely full of phlegm (mucus).', 0, 5, 1),
    (3, @CopdID, 'My chest does not feel tight at all.', 'My chest feels very tight.', 0, 5, 1),
    (4, @CopdID, 'When I walk up a hill or one flight of stairs I am not breathless.', 'When I walk up a hill or one flight of stairs I am very breathless.', 0, 5, 1),
    (5, @CopdID, 'I am not limited doing any activities at home.', 'I am very limited doing activities at home.', 0, 5, 1),
    (6, @CopdID, 'I am confident leaving my home despite my lung condition.', 'I am not at all confident leaving my home because of my lung condition.', 0, 5, 1),
    (7, @CopdID, 'I sleep soundly.', 'I don''t sleep soundly because of my lung condition.', 0, 5, 1),
    (8, @CopdID, 'I have lots of energy.', 'I have no energy at all.', 0, 5, 1);

-- ------------------------------------------------------------------
-- Responses
-- ------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS ResponseSet (
    Set_ID      int PRIMARY KEY AUTO_INCREMENT,
    User_ID     int FOREIGN KEY REFERENCES Users(User_ID),
    Survey_ID   tinyint FOREIGN KEY REFERENCES Survey(Survey_ID),
    Start_Date  date NOT NULL DEFAULT CURRENT_DATE,
    End_Date    date DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS Response (
    Response_ID int PRIMARY KEY AUTO_INCREMENT,
    Set_ID      int FOREIGN KEY REFERENCES ResponseSet(Set_ID),
    Start_Time  timestamp NOT NULL,
    End_Time    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Answer (
    Response_ID int FOREIGN KEY REFERENCES Response(Response_ID),
    Question_ID smallint FOREIGN KEY REFERENCES Question(Question_ID),
    Question    tinyint NOT NULL,
    Val         tinyint NOT NULL
);