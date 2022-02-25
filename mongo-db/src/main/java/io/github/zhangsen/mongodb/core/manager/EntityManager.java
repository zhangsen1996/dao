package io.github.zhangsen.mongodb.core.manager;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;
import io.github.zhangsen.mongodb.core.interfaces.EntitySupplier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class EntityManager<Id, EntityClass extends AbstractEntity<Id>> {


    private MongoTemplate mongoTemplate;


    private Class<EntityClass> entityClass;

    private String managerName;


    public EntityManager(String entityManagerName,MongoTemplate mongoTemplate, Class<EntityClass> entityClass) {
        this.mongoTemplate = mongoTemplate;
        this.entityClass = entityClass;
        this.managerName = entityManagerName;
    }

    public EntityClass getEntity(Id id) {
        return mongoTemplate.findById(id, entityClass);
    }

    public EntityClass getOrCreateEntity(Id id, EntitySupplier<Id,EntityClass> supplier) {
        EntityClass entity = mongoTemplate.findById(id, entityClass);
        if (entity != null) {
            return entity;
        }
        entity = supplier.get(id);

        return entity;
    }

    public String getManagerName() {
        return managerName;
    }

    public EntityClass updateEntity(EntityClass entity) {
        return mongoTemplate.save(entity);
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
}
