<?php

/*
 * Roy Gustafson - (C) Allegheny Health Network 2017
 * <roy.gustafson@ahn.org> or <royagustafson@gmail.com>
 */

namespace AHN\Survey\OAuth2\Repositories;

use Doctrine\ORM\EntityManager;

/**
 * Description of DatabaseResource
 *
 * @author rgustafs
 */
abstract class AuthDAL {

    /**
     * @var \Doctrine\ORM\EntityManager
     */
    protected $entityManager = null;

    public function __construct(EntityManager $entityManager) {
        $this->entityManager = $entityManager;
    }

    public function getEntityManager(): EntityManager {
        return $this->entityManager;
    }

    public function setEntityManager(EntityManager $entityManager) {
        $this->entityManager = $entityManager;
        return $this;
    }

}
