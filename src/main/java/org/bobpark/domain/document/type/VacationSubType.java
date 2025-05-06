package org.bobpark.domain.document.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum VacationSubType {

    AM_HALF_DAY_OFF("오전"),
    PM_HALF_DAY_OFF("오후");

    private final String displayName;
}
