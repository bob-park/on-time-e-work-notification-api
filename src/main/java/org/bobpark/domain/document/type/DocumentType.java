package org.bobpark.domain.document.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum DocumentType {

    VACATION("휴가계"),
    OVERTIME_WORK("휴일 근무 보고서");

    private final String documentName;
}
