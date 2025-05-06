package org.bobpark.domain.document.model;

import java.time.LocalDate;

import org.bobpark.domain.document.type.DocumentType;

public record VacationDocumentResponse(Long id,
                                       DocumentType type,
                                       String userUniqueId,
                                       LocalDate startDate,
                                       LocalDate endDate) {
}
