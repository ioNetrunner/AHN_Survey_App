<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Repositories;

use League\OAuth2\Server\Repositories\RefreshTokenRepositoryInterface;
use League\OAuth2\Server\Exception\UniqueTokenIdentifierConstraintViolationException;
use AHN\Survey\OAuth2\Repositories\AuthDAL;
use AHN\Survey\OAuth2\Entities\RefreshToken;
use AHN\Survey\OAuth2\Entities\RefreshTokenBack;

/**
 * Implements refresh token interface.
 * https://github.com/thephpleague/oauth2-server/tree/master/src/Repositories
 */
class RefreshTokenRepository extends DALRepository implements RefreshTokenRepositoryInterface {

    /**
     * Creates a new refresh token
     *
     * @return RefreshTokenEntityInterface
     */
    public function getNewRefreshToken() {
        return new RefreshToken();
    }

    /**
     * Create a new refresh token_name.
     *
     * @param RefreshTokenEntityInterface $refreshTokenEntity
     *
     * @throws UniqueTokenIdentifierConstraintViolationException
     */
    public function persistNewRefreshToken(RefreshToken $refreshTokenEntity) {

        $em = AuthDAL . getEntityManager();

        $em->persist(new RefreshTokenBack(
                $refreshTokenEntity->getIdentifier(),
                $refreshTokenEntity->getExpiryDateTime(),
                $refreshTokenEntity->getAccessToken()->getIdentifier()
        ));

        $em->flush();
    }

    /**
     * Revoke the refresh token.
     *
     * @param string $tokenId
     */
    public function revokeRefreshToken($tokenId) {

        $em = AuthDAL . getEntityManager();

        $refreshToken = $em->find('RefreshToken', $tokenId);
        $refreshToken->set_is_revoked(true);

        $em->flush();
    }

    /**
     * Check if the refresh token has been revoked.
     *
     * @param string $tokenId
     *
     * @return bool Return true if this token has been revoked
     */
    public function isRefreshTokenRevoked($tokenId) {

        $em = AuthDAL . getEntityManager();

        $refreshToken = $em->find('RefreshToken', $tokenId);

        return $refreshToken->get_is_revoked();
    }

    /**
     * Remove all expired tokens
     */
    public function removeExpiredRefreshTokens() {

        $em = AuthDAL . getEntityManager();

        $query = $em->createQuery(
                'DELETE FROM OAuth2_RefreshToken '
                . 'WHERE expires_at < ?'
        );

        $query->setParameter(1, new DateTime());

        $query->getResult();
    }

}
