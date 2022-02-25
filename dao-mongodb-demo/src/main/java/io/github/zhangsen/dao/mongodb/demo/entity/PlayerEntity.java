package io.github.zhangsen.dao.mongodb.demo.entity;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;

public class PlayerEntity extends AbstractEntity<Integer> {

    private int playerId;

    @Override
    public Integer getId() {
        return playerId;
    }
}
