package io.github.zhangsen.db.spring.support;

import io.github.zhangsen.mongodb.core.entity.AbstractEntity;
import io.github.zhangsen.mongodb.core.entity.EntityCache;
import io.github.zhangsen.mongodb.core.manager.EntityManager;
import io.github.zhangsen.mongodb.core.manager.EntityManagerStorage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DynamicHandleEntityManager implements BeanPostProcessor, ApplicationContextAware {


    private ApplicationContext applicationContext;

    private EntityManagerStorage entityManagerStorage;

    public DynamicHandleEntityManager() {
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (!field.isAnnotationPresent(EntityCache.class)) {
                return;
            }
            field.setAccessible(true);
            Type genericType = field.getGenericType();
            if (genericType == null || !(genericType instanceof ParameterizedType)) {
                return;
            }
            ParameterizedType pt = (ParameterizedType) genericType;
            Type[] actualTypeArguments = pt.getActualTypeArguments();
            Type entityType = actualTypeArguments[1];
            Class<AbstractEntity> entityClass = (Class) entityType;
            String entityManagerName = EntityManagerStorage.getBeanName(entityClass);
            EntityManager entityManager = getEntityManagerStorage().getEntityManager(entityManagerName);
            MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
            if (entityManager == null) {
                entityManager = new EntityManager(entityManagerName, mongoTemplate, entityClass);//applicationContext.getBean(MongoTemplate.class)
                getEntityManagerStorage().addEntityManager(entityManager);
            }
            field.set(bean, entityManager);
        });
        return bean;
    }

    public EntityManagerStorage getEntityManagerStorage() {
        if (entityManagerStorage == null) {
            entityManagerStorage = applicationContext.getBean(EntityManagerStorage.class);
        }
        return entityManagerStorage;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
