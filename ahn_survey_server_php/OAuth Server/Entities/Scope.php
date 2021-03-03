<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Entities;

use League\OAuth2\Server\Entities\ScopeEntityInterface;
use League\OAuth2\Server\Entities\Traits\EntityTrait;

class ScopeEntity implements ScopeEntityInterface {

    use EntityTrait;

    public function jsonSerialize() {
        return $this->getIdentifier();
    }

}
