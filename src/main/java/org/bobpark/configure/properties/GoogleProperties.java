package org.bobpark.configure.properties;

import org.springframework.core.io.Resource;

public record GoogleProperties(String redirectUrl,
                               String calendarId,
                               Resource authLocation) {
}
