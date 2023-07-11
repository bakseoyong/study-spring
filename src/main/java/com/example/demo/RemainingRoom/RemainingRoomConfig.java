package com.example.demo.RemainingRoom;

import com.example.demo.RemainingRoom.Service.RemainingRoomRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemainingRoomConfig {
    public FactoryBean<?> getBean(){
        ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(RemainingRoomRegistry.class);
        return bean;
    }
}
