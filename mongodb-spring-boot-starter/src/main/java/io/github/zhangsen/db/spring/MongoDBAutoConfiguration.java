package io.github.zhangsen.db.spring;

import io.github.zhangsen.db.spring.config.SpringMongoProperties;
import io.github.zhangsen.db.spring.support.DynamicHandleEntityManager;
import io.github.zhangsen.mongodb.core.manager.EntityManagerStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(SpringMongoProperties.class)
@Import(DynamicHandleEntityManager.class)
public class MongoDBAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(EntityManagerStorage.class)
    public EntityManagerStorage entityManagerStorage() {
        return new EntityManagerStorage();
    }
}
