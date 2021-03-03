<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Repositories;

use League\OAuth2\Server\Repositories\UserRepositoryInterface;
use AHN\Survey\OAuth2\Entities\Client;
use AHN\Survey\OAuth2\Entities\User;
use AHN\Survey\OAuth2\Entities\UserBack;

/**
 * Implements user token interface.
 * https://github.com/thephpleague/oauth2-server/tree/master/src/Repositories
 */
class UserRepository extends DALRepository implements UserRepositoryInterface {

    /**
     * Get a user entity.
     *
     * @param string                $username
     * @param string                $password
     * @param string                $grantType    The grant type used
     * @param ClientEntityInterface $clientEntity
     *
     * @return UserEntityInterface
     */
    public function getUserEntityByUserCredentials($username, $password,
                                                   $grantType,
                                                   Client $clientEntity
    ) {
        $em = AuthDAL . getEntityManager();

        $user = $em->find('User', $username);
        
        if (!password_verify($password, $user->get_password())) {
            return; // TODO Best practice?
        }
        
        // verify grants
        // verify identity
        // create user
        // return
    }

}
