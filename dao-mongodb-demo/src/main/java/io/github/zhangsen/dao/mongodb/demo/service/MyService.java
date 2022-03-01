package io.github.zhangsen.dao.mongodb.demo.service;

import io.github.zhangsen.dao.mongodb.demo.entity.PlayerEntity;
import io.github.zhangsen.mongodb.core.entity.EntityCache;
import io.github.zhangsen.mongodb.core.manager.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    @EntityCache
    private EntityManager<Integer, PlayerEntity> playerEntityEntityManager;

    @EntityCache
    private EntityManager<Integer, PlayerEntity> playerEntityEntityManager2;

    public void addPlayer(){
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setName("小米");
        playerEntity.setPlayerId(11948564);
        playerEntityEntityManager.updateEntity(playerEntity);
    }

    public EntityManager<Integer, PlayerEntity> getPlayerEntityEntityManager() {
        return playerEntityEntityManager;
    }

    public PlayerEntity getPlayerEntity(int id){
        return playerEntityEntityManager.getEntity(id);
    }

    public void updateEntity(PlayerEntity playerEntity){
        playerEntityEntityManager.updateEntity(playerEntity);
    }
}
