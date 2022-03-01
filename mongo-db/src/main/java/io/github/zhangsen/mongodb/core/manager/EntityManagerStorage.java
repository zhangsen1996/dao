package io.github.zhangsen.mongodb.core.manager;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;
import io.netty.channel.DefaultEventLoop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityManagerStorage {

    //名字前缀
    static final String MANAGER_NAME_PREFIX = "entityManager_";

    public static String getBeanName(Class<? extends AbstractEntity> entityClass) {
        return MANAGER_NAME_PREFIX + entityClass.getCanonicalName();
    }

    private ExecutorService executorServices[];

    //初始化持久化线程
    public void initPersistentThread(int threadNum) {
        ExecutorService executorServices[] = new ExecutorService[threadNum];
        PersistThreadFactory persistThreadFactory = new PersistThreadFactory();
        for (int i = 0; i < threadNum; i++) {
            executorServices[i] = new DefaultEventLoop(persistThreadFactory);
        }
        this.executorServices = executorServices;
    }

    public ExecutorService getExecutorService(int index) {
        return this.executorServices[index];
    }

    private final Map<String, EntityManager> entityManagerMap = new ConcurrentHashMap<>();

    public void addEntityManager(EntityManager entityManager) {
        entityManagerMap.put(entityManager.getManagerName(), entityManager);
        String managerName = entityManager.getManagerName();
        //固定持久化线程
        entityManager.setPersistIndex(managerName.hashCode() % this.executorServices.length);
        entityManager.setEntityManagerStorage(this);
    }

    public EntityManager getEntityManager(String name) {
        return entityManagerMap.get(name);
    }

    private static class PersistThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        PersistThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "persist-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
