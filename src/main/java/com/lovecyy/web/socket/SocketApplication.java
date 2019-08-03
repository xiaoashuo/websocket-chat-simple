package com.lovecyy.web.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SocketApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SocketApplication.class,args);
    }

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
