package org.bobpark.domain.document.command.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.notification.client.NotificationClient;
import com.malgn.notification.model.SendNotificationMessageRequest;

import org.bobpark.domain.document.event.DocumentEventType;
import org.bobpark.domain.document.event.DocumentRequestedEventPayload;
import org.bobpark.domain.document.type.DocumentType;
import org.bobpark.domain.user.feign.UserFeignClient;
import org.bobpark.domain.user.model.UserResponse;

@Slf4j
@RequiredArgsConstructor
public class DocumentRequestedCommandHandler implements CommandHandler<DocumentRequestedEventPayload> {

    private final NotificationClient notificationClient;
    private final UserFeignClient userClient;

    @Override
    public void handle(Event<DocumentRequestedEventPayload> event) {

        DocumentRequestedEventPayload payload = event.getPayload();

        DocumentType type = payload.type();

        // get user
        UserResponse user = userClient.getUser(payload.userUniqueId());

        notificationClient.sendUserNotification(
            payload.receiveUserUniqueId(),
            SendNotificationMessageRequest.builder()
                .displayMessage(displayMessage(type, user))
                .build());

    }

    @Override
    public boolean supports(Event<DocumentRequestedEventPayload> event) {
        return event.getType() == DocumentEventType.DOCUMENT_REQUESTED;
    }

    private String displayMessage(DocumentType type, UserResponse user) {

        StringBuilder builder = new StringBuilder();

        builder.append(user.group().name())
            .append(" ")
            .append(user.username())
            .append(" ")
            .append(user.position().name())
            .append("이(가) ")
            .append(type.getDocumentName())
            .append("을(를) 신청하였습니다.");

        return builder.toString();
    }

}
