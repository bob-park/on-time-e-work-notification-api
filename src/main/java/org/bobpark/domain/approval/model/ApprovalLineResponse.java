package org.bobpark.domain.approval.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

import org.bobpark.domain.document.type.DocumentType;

@Builder
public record ApprovalLineResponse(Long id,
                                   List<ApprovalLineResponse> children,
                                   DocumentType documentType,
                                   Long teamId,
                                   String userUniqueId,
                                   String contents,
                                   String description,
                                   LocalDateTime createdDate,
                                   LocalDateTime lastModifiedDate) {
}
