package io.github.zhangsen.dao.core.listener;

import io.github.zhangsen.dao.core.MyService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.PriorityQueue;

@Component
public class MyListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Field[] declaredFields = MyService.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            boolean accessible = declaredField.isAccessible();
            ReflectionUtils.makeAccessible(declaredField);
            System.out.println();
        }
        System.out.println(event.getClass().getCanonicalName());


    }
}
