<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Entities;

use League\OAuth2\Server\Entities\ClientEntityInterface;
use League\OAuth2\Server\Entities\Traits\ClientTrait;
use League\OAuth2\Server\Entities\Traits\EntityTrait;

class Client implements ClientEntityInterface {

    use EntityTrait,
        ClientTrait;

    private $identifier;
    private $name;
    private $secret;

    const redirectUri = "http://ahn.org/survey/api";

    /**
     * Constructs new ClientEntity
     * 
     * @param Integer   $identifier
     * @param String    $username
     */
    function __construct(Integer $identifier,
                         String $username,
                         $secret) {
        $this->identifier = $identifier;
        $this->name       = $username;
        $this->secret     = $secret;
    }

    public function getIdentifier() {
        return $this->identifier;
    }

    public function setIdentifier($identifier) {
        $this->identifier = $identifier;
    }

    public function getName() {
        return $this->name;
    }

    public function setName($name) {
        $this->name = $name;
    }

    public function getSecret() {
        return $this->secret;
    }

    public function setSecret($secret) {
        $this->secret = $secret;
    }

    /**
     * Returns the registered redirect URI (as a string).
     *
     * Alternatively return an indexed array of redirect URIs.
     *
     * @return string|string[]
     */
    public function getRedirectUri() {
        return redirectUri;
    }

}
