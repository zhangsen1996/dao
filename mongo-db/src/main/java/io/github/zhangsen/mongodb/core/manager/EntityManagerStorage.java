package io.github.zhangsen.mongodb.core.manager;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;
import io.netty.channel.DefaultEventLoop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class EntityManagerStorage {

    //名字前缀
    static final String MANAGER_NAME_PREFIX = "entityManager_";
    public static String getBeanName(Class<? extends AbstractEntity> entityClass){
        return MANAGER_NAME_PREFIX + entityClass.getCanonicalName();
    }
    private ExecutorService executorServices[];
    //初始化持久化线程
    private void initPersistentThread(int threadNum){
        ExecutorService executorServices[] = new ExecutorService[threadNum];
        for (int i = 0; i < threadNum; i++) {
            executorServices[i] = new DefaultEventLoop();
        }
        this.executorServices = executorServices;
    }



    private final Map<String, EntityManager> entityManagerMap = new ConcurrentHashMap<>();

    public void addEntityManager(EntityManager entityManager){
        entityManagerMap.put(entityManager.getManagerName(),entityManager);
        String managerName = entityManager.getManagerName();
        //固定持久化线程
        ExecutorService executorService = this.executorServices[managerName.hashCode() % this.executorServices.length];
        entityManager.setExecutorService(executorService);
    }

    public EntityManager getEntityManager(String name){
        return entityManagerMap.get(name);
    }
}
