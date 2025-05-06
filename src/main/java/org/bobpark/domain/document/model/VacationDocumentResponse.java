package org.bobpark.domain.document.model;

import java.time.LocalDate;

import org.bobpark.domain.document.type.DocumentType;
import org.bobpark.domain.document.type.VacationSubType;
import org.bobpark.domain.document.type.VacationType;

public record VacationDocumentResponse(Long id,
                                       DocumentType type,
                                       String userUniqueId,
                                       VacationType vacationType,
                                       VacationSubType vacationSubType,
                                       LocalDate startDate,
                                       LocalDate endDate) {
}
