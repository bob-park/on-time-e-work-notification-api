package org.bobpark.domain.user.model;

public record UserResponse(String uniqueId,
                           String userId,
                           String username,
                           TeamResponse team,
                           PositionResponse position) {
}
