package io.github.zhangsen.dao.mongodb.demo.entity;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;
import io.github.zhangsen.mongodb.core.entity.MongoEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@MongoEntity
public class PlayerEntity extends AbstractEntity<Integer> {

    @Id
    private int playerId;

    private String name;

    private short age = 1;

    private DailyModel dailyModel = new DailyModel();

    @Override
    public Integer getId() {
        return playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DailyModel getDailyModel() {
        return dailyModel;
    }

    public void setDailyModel(DailyModel dailyModel) {
        this.dailyModel = dailyModel;
    }
}
