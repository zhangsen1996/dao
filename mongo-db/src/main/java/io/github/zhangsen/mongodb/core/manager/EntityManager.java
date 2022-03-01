package io.github.zhangsen.mongodb.core.manager;

import com.github.benmanes.caffeine.cache.*;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;
import io.github.zhangsen.mongodb.core.entity.MongoEntity;
import io.github.zhangsen.mongodb.core.interfaces.EntitySupplier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class EntityManager<Id, EntityClass extends AbstractEntity<Id>> {

    private MongoTemplate mongoTemplate;

    private Class<EntityClass> entityClass;

    private String managerName;

    private EntityManagerStorage entityManagerStorage;

    private LoadingCache<Id, EntityClass> cache;

    //持久化线程下标
    private int persistIndex;

    public EntityManager(String entityManagerName,MongoTemplate mongoTemplate, Class<EntityClass> entityClass) {
        this.mongoTemplate = mongoTemplate;
        this.entityClass = entityClass;
        this.managerName = entityManagerName;
        MongoEntity annotation = entityClass.getAnnotation(MongoEntity.class);
        int maximumSize = 100;
        if (annotation != null) {
            maximumSize = annotation.cacheSize();
        }
        cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .build(key -> {
                    EntityClass byId = mongoTemplate.findById(key, entityClass);
                    return byId;
                });
    }

    public EntityClass getEntity(Id id) {
        return cache.get(id);
    }

    /**
     * 非本人玩家是不应该调用这个接口的，不需要非本人玩家来创建 id 的entity
     * @param id
     * @param supplier
     * @return
     */
    public EntityClass getOrCreateEntity(Id id, EntitySupplier<Id,EntityClass> supplier) {
        EntityClass entity = cache.get(id);
        if (entity == null) {
            entity = supplier.get(id);
            cache.put(entity.getId(), entity);
        }
        return cache.get(id);

    }

    public void updateEntity(EntityClass entity) {
        cache.put(entity.getId(), entity);
        ExecutorService executorService = this.entityManagerStorage.getExecutorService(persistIndex);
        executorService.submit(()->mongoTemplate.save(entity));
    }

    protected void setPersistIndex(int persistIndex) {
        this.persistIndex = persistIndex;
    }

    protected void setEntityManagerStorage(EntityManagerStorage entityManagerStorage) {
        this.entityManagerStorage = entityManagerStorage;
    }

    public String getManagerName() {
        return managerName;
    }


    public void delEntity(Id id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)),entityClass);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Class<EntityClass> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<EntityClass> entityClass) {
        this.entityClass = entityClass;
    }

    private static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int MAX_CACHE_SIZE;

        public LRUCache(int cacheSize) {
            // 使用构造方法 public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
            // initialCapacity、loadFactor都不重要
            // accessOrder要设置为true，按访问排序
            super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
            MAX_CACHE_SIZE = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            // 超过阈值时返回true，进行LRU淘汰
            return size() > MAX_CACHE_SIZE;
        }

    }
}
