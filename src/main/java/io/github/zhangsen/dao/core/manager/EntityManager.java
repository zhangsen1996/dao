package io.github.zhangsen.dao.core.manager;

import io.github.zhangsen.dao.core.entity.AbstractEntity;
import io.github.zhangsen.dao.core.interfaces.EntitySupplier;
import org.springframework.data.mongodb.core.MongoTemplate;

public class EntityManager<Id, EntityClass extends AbstractEntity<Id>> {

    private MongoTemplate mongoTemplate;

    private Class<EntityClass> entityClass;

    public EntityManager() {
    }

    public EntityManager(MongoTemplate mongoTemplate, Class<EntityClass> entityClass) {
        this.mongoTemplate = mongoTemplate;
        this.entityClass = entityClass;
    }

    public EntityClass getEntity(Id id) {
        return mongoTemplate.findById(id, entityClass);
    }

    public EntityClass getOrCreateEntity(Id id,EntitySupplier<Id,EntityClass> supplier) {
        EntityClass entity = mongoTemplate.findById(id, entityClass);
        if (entity != null) {
            return entity;
        }
        entity = supplier.get(id);

        return entity;
    }

    public EntityClass updateEntity(EntityClass entity) {
        return mongoTemplate.save(entity);
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
}
