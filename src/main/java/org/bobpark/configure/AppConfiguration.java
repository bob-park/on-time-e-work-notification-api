package org.bobpark.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.malgn.cqrs.event.handler.DelegatingCommandHandler;
import com.malgn.notification.client.NotificationClient;

import org.bobpark.configure.properties.AppProperties;
import org.bobpark.domain.document.command.handler.DocumentApprovedCommandHandler;
import org.bobpark.domain.document.command.handler.DocumentCanceledCommandHandler;
import org.bobpark.domain.document.command.handler.DocumentRejectedCommandHandler;
import org.bobpark.domain.document.command.handler.DocumentRequestedCommandHandler;
import org.bobpark.domain.document.feign.DocumentFeignClient;
import org.bobpark.domain.google.provider.GoogleCalendarProvider;
import org.bobpark.domain.user.feign.UserFeignClient;

@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class AppConfiguration {

    private final AppProperties properties;

    private final NotificationClient notificationClient;
    private final UserFeignClient userClient;
    private final DocumentFeignClient documentClient;

    @Bean
    public DelegatingCommandHandler delegatingCommandHandler() {

        DelegatingCommandHandler handler = new DelegatingCommandHandler<>();

        handler.add(new DocumentRequestedCommandHandler(notificationClient, userClient));
        handler.add(new DocumentApprovedCommandHandler(notificationClient, userClient, documentClient, googleCalendarProvider()));
        handler.add(new DocumentRejectedCommandHandler(notificationClient, userClient));
        handler.add(new DocumentCanceledCommandHandler(notificationClient, userClient));

        return handler;
    }

    @Bean
    public GoogleCalendarProvider googleCalendarProvider() {
        return new GoogleCalendarProvider(properties.google());
    }

}
