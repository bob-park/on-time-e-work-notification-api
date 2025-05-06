package org.bobpark.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.malgn.cqrs.event.handler.DelegatingCommandHandler;
import com.malgn.notification.client.NotificationClient;

import org.bobpark.domain.document.command.handler.DocumentApprovedCommandHandler;
import org.bobpark.domain.document.command.handler.DocumentRejectedCommandHandler;
import org.bobpark.domain.document.command.handler.DocumentRequestedCommandHandler;
import org.bobpark.domain.document.feign.DocumentFeignClient;
import org.bobpark.domain.user.feign.UserFeignClient;

@RequiredArgsConstructor
@Configuration
public class AppConfiguration {

    private final NotificationClient notificationClient;
    private final UserFeignClient userClient;
    private final DocumentFeignClient documentClient;

    @Bean
    public DelegatingCommandHandler delegatingCommandHandler() {

        DelegatingCommandHandler handler = new DelegatingCommandHandler<>();

        handler.add(new DocumentRequestedCommandHandler(notificationClient, userClient));
        handler.add(new DocumentApprovedCommandHandler(notificationClient, userClient, documentClient));
        handler.add(new DocumentRejectedCommandHandler(notificationClient, userClient));

        return handler;
    }

}
