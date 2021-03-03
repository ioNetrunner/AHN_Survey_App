<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Repositories;

use League\OAuth2\Server\Repositories\AuthCodeRepositoryInterface;
use League\OAuth2\Server\Exception\UniqueTokenIdentifierConstraintViolationException;
use AHN\Survey\OAuth2\Repositories\AuthDAL;
use AHN\Survey\OAuth2\Entities\AuthCode;
use AHN\Survey\OAuth2\Entities\AuthCodeBack;

class AuthCodeRepository implements AuthCodeRepositoryInterface {

    /**
     * Creates a new AuthCode
     *
     * @return AuthCodeEntityInterface
     */
    public function getNewAuthCode() {
        return new AuthCodeEntity();
    }

    /**
     * Persists a new access token to permanent storage.
     *
     * @param AccessTokenEntityInterface $authCode
     * 
     * @throws UniqueTokenIdentifierConstraintViolationException
     */
    public function persistNewAuthCode(AuthCode $authCode) {

        $scopes = [];
        foreach ($authCode->getScopes() as $scope) {
            $scopes[] = $scope->getIdentifier();
        }

        $em = AuthDAL . getEntityManager();

        $em->persist(new AuthCodeBack(
                $authCode->getIdentifier(), $scopes,
                $authCode->getExpiryDateTime(),
                $authCode->getClient()->getIdentifier(),
                $authCode->getRedirectUri()
        ));

        $em->flush();
    }

    /**
     * Revoke an auth code.
     *
     * @param string $codeId
     */
    public function revokeAuthCode($codeId) {

        $em = AuthDAL . getEntityManager();

        $authCode = $em->find('AuthCode', $codeId);
        $authCode->set_is_revoked(true);

        $em->flush();
    }

    /**
     * Check if the auth code has been revoked.
     *
     * @param string $codeId
     *
     * @return bool Return true if this code has been revoked
     */
    public function isAuthCodeRevoked($codeId) {

        $em = AuthDAL . getEntityManager();

        $authCode = $em->find('AuthCode', $codeId);

        return $authCode->get_is_revoked();
    }

    /**
     * Remove all revoked codes
     */
    public function removeExpiredAuthCodes() {

        $em = AuthDAL . getEntityManager();

        $query = $em->createQuery(
                'DELETE FROM OAuth_AuthCodes'
                . 'WHERE expires_at < ?'
        );

        $query->setParameter(1, new DateTime());

        $query->getResult();
    }

}
