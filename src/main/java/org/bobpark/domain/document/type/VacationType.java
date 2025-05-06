package org.bobpark.domain.document.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public enum VacationType {

    GENERAL("연차"),
    COMPENSATORY("대체 휴가"),
    OFFICIAL("공가");

    private final String displayName;
}
