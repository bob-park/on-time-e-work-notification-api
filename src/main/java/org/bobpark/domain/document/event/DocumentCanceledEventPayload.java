package org.bobpark.domain.document.event;

import com.malgn.cqrs.event.EventPayload;

import org.bobpark.domain.document.type.DocumentType;

public record DocumentCanceledEventPayload(Long id,
                                           DocumentType type,
                                           String userUniqueId)
    implements EventPayload {
}
