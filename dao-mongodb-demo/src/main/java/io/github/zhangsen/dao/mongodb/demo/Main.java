package io.github.zhangsen.dao.mongodb.demo;


import io.github.zhangsen.dao.mongodb.demo.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        MyService bean = context.getBean(MyService.class);
        System.out.println();
    }

}
