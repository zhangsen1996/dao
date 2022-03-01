package io.github.zhangsen.db.spring;

import io.github.zhangsen.db.spring.config.SpringMongoProperties;
import io.github.zhangsen.db.spring.support.DynamicHandleEntityManager;
import io.github.zhangsen.db.spring.support.SimplifyDocListener;
import io.github.zhangsen.mongodb.core.manager.EntityManagerStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@EnableConfigurationProperties(SpringMongoProperties.class)
@Import(DynamicHandleEntityManager.class)
public class MongoDBAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(EntityManagerStorage.class)
    public EntityManagerStorage entityManagerStorage(SpringMongoProperties mongoProperties) {
        EntityManagerStorage entityManagerStorage = new EntityManagerStorage();
        entityManagerStorage.initPersistentThread(mongoProperties.getThreadNum());
        return entityManagerStorage;
    }

    @Bean
    @ConditionalOnMissingBean({MongoConverter.class})
    @ConditionalOnProperty(prefix = "mongodb", name = "closeClassKey")
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context, MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }

    @Bean
    @ConditionalOnProperty(prefix = "mongodb", name = "simplifyDoc")
    public SimplifyDocListener simplifyDoc() {
        return new SimplifyDocListener();
    }

}
