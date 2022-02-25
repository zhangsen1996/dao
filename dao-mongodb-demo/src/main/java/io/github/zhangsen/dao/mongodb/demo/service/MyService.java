package io.github.zhangsen.dao.mongodb.demo.service;

import io.github.zhangsen.dao.mongodb.demo.entity.PlayerEntity;
import io.github.zhangsen.mongodb.core.entity.EntityCache;
import io.github.zhangsen.mongodb.core.manager.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    @EntityCache
    private EntityManager<Integer, PlayerEntity> playerEntityEntityManager;
}
