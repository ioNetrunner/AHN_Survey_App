<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

return [
    'settings' => [
        'tokens'   => [
            'authCodeDuration'     => 'PT10M',
            'refreshTokenDuration' => 'P1M',
            'accessTokenDuration'  => 'PT1H',
        ],
        'doctrine' => [
            'meta'       => [
                'entity_path'           => [
                    'AHN\Survey\OAuth2\Entities'
                ],
                'auto_generate_proxies' => false,
                'proxy_dir'             => null,
                'cache'                 => null,
            ],
            'connection' => [
                'driver'   => 'sqlsrv',
                'host'     => 'localhost', // TODO
                'dbname'   => 'SurveyData',
                'user'     => 'your-user-name', // TODO 
                'password' => 'your-password', // TODO
            ]
        ],
    ],
];
