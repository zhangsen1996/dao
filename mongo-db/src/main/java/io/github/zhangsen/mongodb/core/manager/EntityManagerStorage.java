package io.github.zhangsen.mongodb.core.manager;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManagerStorage {

    //名字前缀
    static final String MANAGER_NAME_PREFIX = "entityManager_";
    public static String getBeanName(Class<? extends AbstractEntity> entityClass){
        return MANAGER_NAME_PREFIX + entityClass.getCanonicalName();
    }

    private final Map<String, EntityManager> entityManagerMap = new ConcurrentHashMap<>();

    public void addEntityManager(EntityManager entityManager){
        entityManagerMap.put(entityManager.getManagerName(),entityManager);
    }

    public EntityManager getEntityManager(String name){
        return entityManagerMap.get(name);
    }
}
