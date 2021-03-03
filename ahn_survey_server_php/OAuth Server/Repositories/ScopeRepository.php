<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Repositories;

use League\OAuth2\Server\Repositories\ScopeRepositoryInterface;

use AHN\Survey\OAuth2\Entities\Scope;
use AHN\Survey\OAuth2\Entities\Client;

/**
 * Implements scope interface.
 * https://github.com/thephpleague/oauth2-server/tree/master/src/Repositories
 */
class ScopeRepository implements ScopeRepositoryInterface {

    /**
     * Return information about a scope.
     *
     * @param string $identifier The scope identifier
     *
     * @return ScopeEntityInterface
     */
    public function getScopeEntityByIdentifier($identifier) {

        $settings = include 'AHN/Survey/OAuth2/Settings.php';

        $scopes = $settings['scopes'];

        if (array_key_exists($identifier, $scopes)) {
            return;
        }

        $scope = new Scope();
        $scope->setIdentifier($identifier);
        $scope->setIcon($scopes[$identifier]['icon']);
        $scope->setDescription($scopes[$identifier]['description']);
        $scope->setAttributes($scopes[$identifier]['attributes']);

        return $scope;
    }

    /**
     * Given a client, grant type and optional user identifier validate the set 
     * of scopes requested are valid and optionally append additional scopes or 
     * remove requested scopes.
     *
     * @param ScopeEntityInterface[] $scopes
     * @param string                 $grantType
     * @param ClientEntityInterface  $clientEntity
     * @param null|string            $userIdentifier
     *
     * @return ScopeEntityInterface[]
     */
    public function finalizeScopes(array $scopes, $grantType,
                                   Client $clientEntity,
                                   $userIdentifier = null) {

        // TODO figure out what scopes we want
    }

}
