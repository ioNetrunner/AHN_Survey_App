<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2;

use League\OAuth2\Server\AuthorizationServer;
use League\OAuth2\Server\Exception\OAuthServerException;
use League\OAuth2\Server\Grant\PasswordGrant;
use AHN\Survey\OAuth2\Repositories\AccessTokenRepository;
use AHN\Survey\OAuth2\Repositories\ClientRepository;
use AHN\Survey\OAuth2\Repositories\RefreshTokenRepository;
use AHN\Survey\OAuth2\Repositories\ScopeRepository;
use AHN\Survey\OAuth2\Repositories\UserRepository;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\App;

require __DIR__ . 'vendor/autoload.php';

$app = new App([
    AuthorizationServer::class => function () {

        $server = new AuthorizationServer(
                new ClientRepository(), new AccessTokenRepository(),
                new ScopeRepository(), 'file://' . __DIR__ . '/../private.key',
                'file://' . __DIR__ . '/../public.key'
        );

        $grant = new PasswordGrant(
                new UserRepository(), new RefreshTokenRepository()
        );

        // Refresh tokens expire after 1 month
        $grant->setRefreshTokenTTL(new \DateInterval('P1M'));

        // Enable the password grant with 1 hour access token
        $server->enableGrantType(
                $grant, new \DateInterval('PT1H')
        );

        return $server;
    }]
);

$app->post(
        '/access_token',
        function(Request $request, Response $response) use ($app) {
    $server = $app->getContainer()->get(AuthorizationServer::class);

    try {
        return $server->respondToAccessTokenRequest($request, $response);
    } catch (OAuthServerException $ex) {
        return $ex->generateHttpResponse($response);
    } catch (Exception $ex) {
        $body = $response->getBody();
        $body->write($ex->getMessage());
        return $response->withStatus(500)->withBody($body);
    }
}
);

$app->run();
