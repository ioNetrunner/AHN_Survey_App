/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

/**
 * Author:  rgustafs
 * Created: Aug 3, 2017
 */
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name='SurveyOAuth')
  CREATE DATABASE SurveyOAuth
-- ------------------------------------------------------------------
-- OAuth
-- ------------------------------------------------------------------

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='OAuth2_Users' AND xtype='U')
  CREATE TABLE OAuth2_Users (
    User_ID     int PRIMARY KEY IDENTITY(1,1),
    Identifier  int,
    Username    varchar(20) NOT NULL,
    Password    varchar(255) NOT NULL,
    Attempts    tinyint NOT NULL DEFAULT 0,
    UNIQUE(Username)
  );

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='OAuth2_Client' AND xtype='U')
    CREATE TABLE OAuth2_Client ();

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='AccessToken' AND xtype='U')
    CREATE TABLE AccessToken ();

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='AuthCode' AND xtype='U')
    CREATE TABLE AuthCode ();

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name='SurveyData')
  CREATE DATABASE SurveyData
-- ------------------------------------------------------------------
-- Survey
-- ------------------------------------------------------------------

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Survey' AND xtype='U')
  CREATE TABLE Survey (
    Survey_ID   tinyint NOT NULL IDENTITY(1,1) PRIMARY KEY,
    Label       varchar(40) NOT NULL,
    Lang        varchar(6) NOT NULL,
    Version     tinyint NOT NULL DEFAULT 1,
    Questions   tinyint NOT NULL,
    UNIQUE (Label, Version)
  );

INSERT INTO Survey
  (Label, Version, Questions, Lang)
VALUES
  ('COPD Assessment Test', 1, 8, 'en-US');

DECLARE @CopdID INT = SCOPE_IDENTITY();

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Question' AND xtype='U')
  CREATE TABLE Question (
    Question_ID smallint NOT NULL PRIMARY KEY IDENTITY(1,1),
    Survey_ID   tinyint,
    Low_Text    varchar(255) NOT NULL,
    High_Text   varchar(255),
    Minimum     tinyint NOT NULL DEFAULT 0,
    Maximum     tinyint NOT NULL DEFAULT 5,
    Inter       tinyint NOT NULL DEFAULT 1,
    UNIQUE (Survey_ID, Low_Text, High_Text),
    CONSTRAINT FK_Question_Survey
      FOREIGN KEY (Survey_ID)
      REFERENCES Survey(Survey_ID)
  );


INSERT INTO Question
  (Question_ID, Survey_ID, Low_Text, High_Text, Minimum, Maximum, Inter)
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

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ResponseSet' AND xtype='U')
  CREATE TABLE ResponseSet (
    Set_ID      int PRIMARY KEY IDENTITY(1,1),
    User_ID     int,
    Survey_ID   tinyint,
    Start_Date  datetime NOT NULL DEFAULT GETDATE(),
    End_Date    date DEFAULT NULL,
    CONSTRAINT FK_Response_Survey
      FOREIGN KEY (Survey_ID, User_ID)
      REFERENCES Survey(Survey_ID), OAuth2_Users(User_ID)
  );

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Response' AND xtype='U')
  CREATE TABLE Response (
    Response_ID int PRIMARY KEY IDENTITY(1,1),
    Set_ID      int,
    Start_Time  datetime NOT NULL,
    End_Time    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Response_Survey
      FOREIGN KEY (Set_ID)
      REFERENCES ResponseSet(Set_ID)
  );

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Answer' AND xtype='U')
  CREATE TABLE Answer (
    Response_ID int,
    Question_ID smallint,
    Question    tinyint NOT NULL,
    Val         tinyint NOT NULL,
    CONSTRAINT FK_Response_Survey
      FOREIGN KEY (Response_ID, Question_ID)
      REFERENCES Response(Response_ID), Question(Question_ID)
  );
