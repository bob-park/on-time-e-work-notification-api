package org.bobpark.domain.document.command.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.notification.client.NotificationClient;
import com.malgn.notification.model.SendNotificationMessageRequest;

import org.bobpark.domain.document.event.DocumentApprovedEventPayload;
import org.bobpark.domain.document.event.DocumentEventType;
import org.bobpark.domain.document.feign.DocumentFeignClient;
import org.bobpark.domain.document.model.VacationDocumentResponse;
import org.bobpark.domain.document.type.DocumentType;
import org.bobpark.domain.google.provider.GoogleCalendarProvider;
import org.bobpark.domain.user.feign.UserFeignClient;
import org.bobpark.domain.user.model.UserResponse;

@Slf4j
@RequiredArgsConstructor
public class DocumentApprovedCommandHandler implements CommandHandler<DocumentApprovedEventPayload> {

    private final NotificationClient notificationClient;
    private final UserFeignClient userClient;
    private final DocumentFeignClient documentClient;

    private final GoogleCalendarProvider calendarProvider;

    @Override
    public void handle(Event<DocumentApprovedEventPayload> event) {

        DocumentApprovedEventPayload payload = event.getPayload();

        DocumentType type = payload.type();

        // get user
        UserResponse user = userClient.getUser(payload.userUniqueId());

        notificationClient.sendUserNotification(
            payload.receiveUserUniqueId(),
            SendNotificationMessageRequest.builder()
                .displayMessage(displayMessage(type, user))
                .build());

        // vacation 인 경우 처리 google calendar 에 추가
        if (type == DocumentType.VACATION) {
            VacationDocumentResponse document = documentClient.getVacationDocument(payload.id());

            String displaySchedule =
                user.username() + " " + user.position().name() + " " + document.vacationType().getDisplayName();

            if (document.vacationSubType() != null) {
                displaySchedule += "(" + document.vacationSubType().getDisplayName() + ")";
            }

            try {
                calendarProvider.addEvent(displaySchedule, document.startDate(), document.endDate());
            } catch (Exception e) {
                log.warn("Failed add google calendar - {}", e.getMessage());
            }

        }

    }

    @Override
    public boolean supports(Event<DocumentApprovedEventPayload> event) {
        return event.getType() == DocumentEventType.DOCUMENT_APPROVED;
    }

    private String displayMessage(DocumentType type, UserResponse user) {

        StringBuilder builder = new StringBuilder();

        builder.append(user.group().name())
            .append(" ")
            .append(user.username())
            .append(" ")
            .append(user.position().name())
            .append("이(가) 신청하신 ")
            .append(type.getDocumentName())
            .append(" 문서가 승인되었습니다.");

        return builder.toString();
    }
}
