<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Entities;

use League\OAuth2\Server\Entities\UserEntityInterface;

class User implements UserEntityInterface {

    private $identifier;
    private $surveyID;
    private $name;

    public function __construct(integer $identifier, integer $surveyID,
                                string $name) {
        $this->identifier = $identifier;
        $this->surveyID   = $surveyID;
        $this->name       = $name;
    }

    public function getIdentifier() {
        return $this->identifier;
    }

    public function getSurveyID() {
        return $this->surveyID;
    }

    public function getName() {
        return $this->name;
    }

    public function setIdentifier($identifier) {
        $this->identifier = $identifier;
    }

    public function setSurveyID($surveyID) {
        $this->surveyID = $surveyID;
    }

    public function setName($name) {
        $this->name = $name;
    }

}
