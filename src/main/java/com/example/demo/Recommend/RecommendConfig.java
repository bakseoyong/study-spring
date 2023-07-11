package com.example.demo.Recommend;

import com.example.demo.Recommend.Service.RecommendRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendConfig {

    @Bean
    public FactoryBean<?> getRecommendServiceBean(){
        ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(RecommendRegistry.class);
        return bean;
    }
}
