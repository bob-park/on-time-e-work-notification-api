package org.bobpark.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.malgn.cqrs.event.handler.DelegatingCommandHandler;

@Configuration
public class AppConfiguration {

    @Bean
    public DelegatingCommandHandler delegatingCommandHandler() {

        DelegatingCommandHandler handler = new DelegatingCommandHandler<>();

        return handler;
    }
}
