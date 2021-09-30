package io.github.zhangsen.dao.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * 动态注入bean
 */
@Component
public class DynamicInjection implements BeanFactoryPostProcessor {



    @Autowired
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory
                = (DefaultListableBeanFactory) beanFactory;


        String[] beanDefinitionNames = defaultListableBeanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(beanDefinitionName);
            if (beanDefinition==null){
                continue;
            }
        }
/*        //注册Bean定义，容器根据定义返回bean
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(PersonManager.class);
        beanDefinitionBuilder.addPropertyReference("personDao", "personDao");
        BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        defaultListableBeanFactory.registerBeanDefinition("personManager1", personManagerBeanDefinition);

        //注册bean实例

        PersonDao personDao = beanFactory.getBean(PersonDao.class);
        PersonManager personManager = new PersonManager();
        personManager.setPersonDao(personDao);
        beanFactory.registerSingleton("personManager2", personManager);*/

    }
}
