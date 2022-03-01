package io.github.zhangsen.dao.mongodb.demo;


import io.github.zhangsen.dao.mongodb.demo.entity.PlayerEntity;
import io.github.zhangsen.dao.mongodb.demo.service.MyService;
import io.github.zhangsen.mongodb.core.manager.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        MyService bean = context.getBean(MyService.class);
        EntityManager<Integer, PlayerEntity> playerEntityEntityManager = bean.getPlayerEntityEntityManager();
        PlayerEntity playerEntity = playerEntityEntityManager.getEntity(11948563);
        playerEntity.setName("小小");
        playerEntity.getDailyModel().getDailyNum().put(1,200L);
        playerEntity.getDailyModel().getDailyNum().put(2,0L);
        playerEntity.getDailyModel().getNumArr().add(3);
        playerEntity.getDailyModel().getNumArr().add(0);
        playerEntity.getDailyModel().getNumArr().add(1);
        bean.updateEntity(playerEntity);
        System.out.println();

    }

}
