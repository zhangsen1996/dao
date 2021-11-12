package io.github.zhangsen.dao.core;

import io.github.zhangsen.dao.core.annotations.EntityCache;
import io.github.zhangsen.dao.core.entity.PlayerEntity;
import io.github.zhangsen.dao.core.manager.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    @EntityCache
    private EntityManager<Integer, PlayerEntity> entityManager;

    @Autowired
    private MyDao dao;

    public EntityManager<Integer, PlayerEntity> getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager<Integer, PlayerEntity> entityManager) {
        this.entityManager = entityManager;
    }
}
