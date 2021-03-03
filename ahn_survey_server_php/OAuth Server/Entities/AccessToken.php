<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Entities\Backing;

use Doctrine\ORM\Mapping as ORM;
use League\OAuth2\Server\Entities\AccessTokenEntityInterface;
use League\OAuth2\Server\Entities\ScopeEntityInterface;
use League\OAuth2\Server\Entities\ClientEntityInterface;

/**
 * @ORM\Entity
 * @ORM\Table(name="OAuth2_AccessToken")
 */
class AccessTokenBack implements AccessTokenEntityInterface {

    /**
     * Generate a JWT from the access token
     *
     * @param CryptKey $privateKey
     *
     * @return string
     */
    public function convertToJWT(CryptKey $privateKey) {
        return (new Builder())
                        ->setAudience($this->getClient()->getIdentifier())
                        ->setId($this->getIdentifier(), true)
                        ->setIssuedAt(time())
                        ->setNotBefore(time())
                        ->setExpiration($this->getExpiryDateTime()->getTimestamp())
                        ->setSubject($this->getUserIdentifier())
                        ->set('scopes', $this->getScopes())
                        ->sign(new Sha256(),
                               new Key($privateKey->getKeyPath(),
                                       $privateKey->getPassPhrase()))
                        ->getToken();
    }

    /**
     * @ORM\Id
     * @ORM\(name="AccessID", type="string")
     */
    protected $identifier;

    /**
     * @ORM\Column(name="Scopes", type="json_array");
     */
    protected $scopeIdentifiers;
    protected $scopes;

    /**
     * @ORM\Column(name="Expires", type="datetime)
     */
    protected $expiryDateTime;

    /**
     * @ORM\Column(name="UserID", type="string")
     */
    protected $userIdentifier;

    /**
     * @ORM\Column(name="ClientID", type="string")
     */
    protected $clientIdentifier;
    protected $client;

    /**
     * @ORM\Column(name="Revoked", type="boolean")
     */
    protected $isRevoked;

    /**
     * 
     * @param type $identifier
     * @param type $scopes
     * @param type $expiryDateTime
     * @param type $userIdentifier
     * @param type $client
     */
    public function __construct($identifier, $scopes, $expiryDateTime,
                                $userIdentifier, $client) {
        $this->identifier = $identifier;
        foreach ($scopes as $scope) {
            $this->scopes[$scope->getIdentifier()] = $scope;
        }
        $this->scopeIdentifiers = array_keys($scopes);
        $this->expiryDateTime   = $expiryDateTime;
        $this->userIdentifier   = $userIdentifier;
        $this->client           = $client;
        $this->clientIdentifier = $client->getIdentifier();
    }

    /**
     * @return mixed
     */
    public function getIdentifier() {
        return $this->identifier;
    }

    /**
     * @param mixed $identifier
     */
    public function setIdentifier($identifier) {
        $this->identifier = $identifier;
    }

    /**
     * Associate a scope with the token.
     *
     * @param ScopeEntityInterface $scope
     */
    public function addScope(ScopeEntityInterface $scope) {
        $this->scopes[$scope->getIdentifier()] = $scope;
        $this->scopeIdentifiers[]              = $scope->getIdentifier();
    }

    /**
     * Return an array of scopes associated with the token.
     *
     * @return ScopeEntityInterface[]
     */
    public function getScopes() {
        return array_values($this->scopes);
    }

    /**
     * Get the token's expiry date time.
     *
     * @return \DateTime
     */
    public function getExpiryDateTime() {
        return $this->expiryDateTime;
    }

    /**
     * Set the date time when the token expires.
     *
     * @param \DateTime $dateTime
     */
    public function setExpiryDateTime(\DateTime $dateTime) {
        $this->expiryDateTime = $dateTime;
    }

    /**
     * Set the identifier of the user associated with the token.
     *
     * @param string|int $identifier The identifier of the user
     */
    public function setUserIdentifier($identifier) {
        $this->userIdentifier = $identifier;
    }

    /**
     * Get the token user's identifier.
     *
     * @return string|int
     */
    public function getUserIdentifier() {
        return $this->userIdentifier;
    }

    /**
     * Get the client that the token was issued to.
     *
     * @return ClientEntityInterface
     */
    public function getClient() {
        return $this->client;
    }

    /**
     * Set the client that the token was issued to.
     *
     * @param ClientEntityInterface $client
     */
    public function setClient(ClientEntityInterface $client) {
        $this->client           = $client;
        $this->clientIdentifier = $client->getIdentifier();
    }

    /**
     * Check if token has been revoked
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
    public function setIsRevoked($isRevoked) {
        $this->isRevoked = $isRevoked;
    }

}
