<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Repositories;

use League\OAuth2\Server\Repositories\AccessTokenRepositoryInterface;
use League\OAuth2\Server\Exception\UniqueTokenIdentifierConstraintViolationException;
use AHN\Survey\OAuth2\Repositories\AuthDAL;
use AHN\Survey\OAuth2\Entities\AccessToken;
use AHN\Survey\OAuth2\Entities\AccessTokenBack;
use AHN\Survey\OAuth2\Entities\Client;

/**
 * Implements access token interface.
 * https://github.com/thephpleague/oauth2-server/tree/master/src/Repositories
 */
class AccessTokenRepository implements AccessTokenRepositoryInterface {

    /**
     * Create a new access token
     *
     * @param ClientEntityInterface  $clientEntity
     * @param ScopeEntityInterface[] $scopes
     * @param mixed                  $userIdentifier
     *
     * @return AccessTokenEntityInterface
     */
    public function getNewToken(Client $clientEntity, array $scopes,
                                $userIdentifier = null) {

        $accessToken = new AccessToken();
        $accessToken->setClient($clientEntity);
        $accessToken->setUser($userIdentifier); // TODO password
        foreach ($scopes as $scope) {
            $accessToken->addScope($scope);
        }

        return $accessToken;
    }

    /**
     * Persists a new access token to permanent storage.
     *
     * @param AccessTokenEntityInterface $accessTokenEntity
     *
     * @throws UniqueTokenIdentifierConstraintViolationException
     */
    public function persistNewAccessToken(AccessToken $accessTokenEntity) {

        $scopes = [];
        foreach ($accessTokenEntity->getScopes() as $scope) {
            $scopes[] = $scope->getIdentifier();
        }

        $em = AuthDAL . getEntityManager();

        $em->persist(new AccessTokenBack(
                $accessTokenEntity->getIdentifier(), $scopes,
                $accessTokenEntity->getExpiryDateTime(),
                $accessTokenEntity->getUserIdentifier(),
                $accessTokenEntity->getClient()->getIdentifier()
        ));

        $em->flush();
    }

    /**
     * Revoke an access token.
     *
     * @param string $tokenId
     */
    public function revokeAccessToken($tokenId) {

        $em = AuthDAL . getEntityManager();

        $accessToken = $em->find('AccessToken', $tokenId);
        $accessToken->set_is_revoked(true);

        $em->flush();
    }

    /**
     * Check if the access token has been revoked.
     *
     * @param string $tokenId
     *
     * @return bool Return true if this token has been revoked
     */
    public function isAccessTokenRevoked($tokenId) {

        $em = AuthDAL . getEntityManager();

        $accessToken = $em->find('AccessToken', $tokenId);

        return $accessToken->get_is_revoked();
    }

    /**
     * Remove all expired tokens
     */
    public function removeExpiredAccessTokens() {

        $em = AuthDAL . getEntityManager();

        $query = $em->createQuery(
                'DELETE FROM OAuth2_AccessToken '
                . 'WHERE expires_at < ?'
        );

        $query->setParameter(1, new DateTime());

        $query->getResult();
    }

}
