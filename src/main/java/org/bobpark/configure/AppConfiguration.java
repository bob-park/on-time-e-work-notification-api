package org.bobpark.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.malgn.cqrs.event.handler.DelegatingCommandHandler;
import com.malgn.notification.client.NotificationClient;

import org.bobpark.domain.document.command.handler.DocumentNotificationCommandHandler;
import org.bobpark.domain.user.feign.UserFeignClient;

@RequiredArgsConstructor
@Configuration
public class AppConfiguration {

    private final NotificationClient notificationClient;
    private final UserFeignClient userClient;

    @Bean
    public DelegatingCommandHandler delegatingCommandHandler() {

        DelegatingCommandHandler handler = new DelegatingCommandHandler<>();

        handler.add(new DocumentNotificationCommandHandler(notificationClient, userClient));

        return handler;
    }
}
