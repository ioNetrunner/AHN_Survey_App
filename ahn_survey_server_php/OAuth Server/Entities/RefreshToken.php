<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Entities;

use Doctrine\ORM\Mapping as ORM;
use League\OAuth2\Server\Entities\RefreshTokenEntityInterface;
use League\OAuth2\Server\Entities\AccessTokenEntityInterface;

/**
 * @ORM\Entity
 * @ORM\Table(name="OAuth2_RefreshToken")
 */
class RefreshToken implements RefreshTokenEntityInterface {

    /**
     * @ORM\Id
     * @ORM\(name="RefreshID", type="string")
     */
    protected $identifier;

    /**
     * @ORM\Column(name="Expires", type="datetime")
     */
    protected $expiryDateTime;

    /*
     * @ORM\(name="AccessID", type="string")
     */
    protected $accessIdentifier;
    protected $accessToken;

    /**
     * @ORM\Column(name="Revoked", type="boolean")
     */
    protected $isRevoked;

    /**
     * 
     * @param type $identifier
     * @param type $expiryDateTime
     * @param type $accessToken
     */
    public function __construct($identifier, $expiryDateTime, $accessToken) {
        $this->identifier     = $identifier;
        $this->expiryDateTime = $expiryDateTime;
        $this->accessToken    = $accessToken;
    }

    /**
     * Identifies token in database
     * 
     * @return string|integer
     */
    public function getIdentifier() {
        return $this->identifier;
    }

    /**
     * Sets identifier
     * 
     * @param string|integer $identifier
     */
    public function setIdentifier($identifier) {
        $this->identifier = $identifier;
    }

    /**
     * Get the token's expiry date time.
     *
     * @return DateTime
     */
    public function getExpiryDateTime() {
        return $this->expiryDateTime;
    }

    /**
     * Get the token's expiry date time.
     *
     * @param DateTime when token expires
     */
    public function setExpiryDateTime($expiryDateTime) {
        $this->expiryDateTime = $expiryDateTime;
    }

    /**
     * Token allowing access to refresh
     * 
     * @return AccessToken
     */
    public function getAccessToken() {
        return $this->accessToken;
    }

    /**
     * Sets token
     * 
     * @param AccessToken $accessToken
     */
    public function setAccessToken(AccessTokenEntityInterface $accessToken) {
        $this->accessToken      = $accessToken;
        $this->accessIdentifier = $accessToken->getIdentifier();
    }

    /**
     * Has token been revoked
     * 
     * @return boolean
     */
    public function getIsRevoked() {
        return $this->isRevoked;
    }

    /**
     * Revoke token
     * 
     * @param boolean $isRevoked
     */
    public function setIsRevoked(boolean $isRevoked) {
        $this->isRevoked = $isRevoked;
    }

}
