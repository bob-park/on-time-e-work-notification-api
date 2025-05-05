package org.bobpark.domain.document.event;

import com.malgn.cqrs.event.EventPayload;

import org.bobpark.domain.document.type.DocumentType;

public record DocumentApprovedEventPayload(Long id,
                                           DocumentType type,
                                           String userUniqueId,
                                           String receiveUserUniqueId)
    implements EventPayload {
}
