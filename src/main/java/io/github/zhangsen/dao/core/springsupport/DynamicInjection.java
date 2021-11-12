package io.github.zhangsen.dao.core.springsupport;

import io.github.zhangsen.dao.core.annotations.EntityCache;
import io.github.zhangsen.dao.core.entity.AbstractEntity;
import io.github.zhangsen.dao.core.manager.EntityManager;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * //注册Bean定义，
 * BeanDefinitionBuilder beanDefinitionBuilder =
 * BeanDefinitionBuilder.genericBeanDefinition(PersonManager.class);
 * beanDefinitionBuilder.addPropertyReference("personDao", "personDao");
 * BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
 * defaultListableBeanFactory.registerBeanDefinition("personManager1", personManagerBeanDefinition);
 * <p>
 * //注册bean实例
 * <p>
 * PersonDao personDao = beanFactory.getBean(PersonDao.class);
 * PersonManager personManager = new PersonManager();
 * personManager.setPersonDao(personDao);
 * beanFactory.registerSingleton("personManager2", personManager);
 */
@Component
public class DynamicInjection implements BeanDefinitionRegistryPostProcessor {
    //名字前缀
    static final String MANAGER_NAME_PREFIX = "entityManager_";
    private String scanPacket = "io.github.zhangsen.dao";

    public DynamicInjection() {
    }

    public DynamicInjection(String scanPacket) {
        this.scanPacket = scanPacket;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Reflections reflections = new Reflections(scanPacket);
        Set<Class<? extends AbstractEntity>> entityClasses = reflections.getSubTypesOf(AbstractEntity.class);



        for (Class<? extends AbstractEntity> entityClass : entityClasses) {
            BeanDefinitionBuilder beanDefinitionBuilder =
                    BeanDefinitionBuilder.genericBeanDefinition(EntityManager.class);
            //beanDefinitionBuilder.addPropertyReference("mongoTemplate", "mongoTemplate");
            beanDefinitionBuilder.addPropertyValue("entityClass", entityClass);
            AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(getBeanName(entityClass), beanDefinition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory
                = (DefaultListableBeanFactory) beanFactory;


        Iterator<String> beanNamesIterator = defaultListableBeanFactory.getBeanNamesIterator();
        while (beanNamesIterator.hasNext()){
            String beanName = beanNamesIterator.next();
            Class<?> beanClass = defaultListableBeanFactory.getType(beanName);
            ReflectionUtils.doWithFields(beanClass,field -> {
                if (!field.isAnnotationPresent(EntityCache.class)) {
                    return;
                }
                Type genericType = field.getGenericType();
                if (!(genericType instanceof ParameterizedType)) {
                    return;
                }
                Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                if (Objects.isNull(actualTypeArguments) || actualTypeArguments.length != 2) {
                    return;
                }
                Type entityType = actualTypeArguments[1];
                BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(beanName);
                if (Objects.isNull(beanDefinition)) {
                    return;
                }
                Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) entityType;
                beanDefinition.getPropertyValues().add(field.getName(), new RuntimeBeanReference(getBeanName(entityClass)));
            });
        }
    }



    //beanDefinition.getPropertyValues().add("dao",null);


    private String getBeanName(Class<? extends AbstractEntity> entityClass){
        return MANAGER_NAME_PREFIX + entityClass.getSimpleName();
    }

}
